package com.example.playlistmaker.presentation.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.presentation.ui.search.track.TrackAdapter
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.main.MainActivity
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<SearchFragmentBinding>() {

    private var editTextValue = ""
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var tracks: ArrayList<Track>

    private val viewModel by viewModel<SearchViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SearchFragmentBinding {
        return SearchFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracks = ArrayList<Track>()
        adapterSearch = TrackAdapter(tracks)
        adapterHistory = TrackAdapter(viewModel.tracksHistory().value!!)

        adapterSearch.itemClickListener = { track ->
            if (viewModel.clickDebounce()) {
                viewModel.addTrackInSearchHistory(track)
                adapterHistory.notifyDataSetChanged()
                startPlayerFragment(track)
            }
        }
        adapterHistory.itemClickListener = { track ->
            if (viewModel.clickDebounce()) {
                viewModel.addTrackInSearchHistory(track)
                adapterHistory.notifyDataSetChanged()
                44
            }
        }

        viewModel.foundTracks().observe(viewLifecycleOwner) { it ->
            processingSearchStatus(it)
        }

        viewModel.tracksHistory().observe(viewLifecycleOwner) { it ->
            adapterHistory.notifyDataSetChanged()
        }

        binding.buttonUpdate.setOnClickListener {
            viewModel.changeRequestText(binding.editText.text.toString())
            viewModel.searchDebounce()
        }

        binding.clearButton.setOnClickListener {
            binding.editText.setText("")
            viewModel.deleteFoundTracks()
            hideKeyboard()
            changeStateWhenSearchBarIsEmpty()
        }

        binding.cleanHistoryButton.setOnClickListener {         //Очистка истории поиска
            showAndHideHistoryLayout(false)                      //Скрываем HistoryLayout
            viewModel.cleanHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.isVisible = !s.isNullOrEmpty()
                binding.historyLayout.isVisible =
                    (binding.editText.hasFocus()         //Есть фокус
                            && s?.isEmpty() == true             //Строка поиска пуста
                            && viewModel.tracksHistory().value!!.isNotEmpty()       //Список треков не пустой
                            )            //отображение Layout при изменении текста в строке поиска

                editTextValue = binding.editText.text.toString()
                if (editTextValue.isEmpty()) {
                    changeStateWhenSearchBarIsEmpty()
                    viewModel.onDestroy()

                } else {
                    viewModel.changeRequestText(binding.editText.text.toString())
                    viewModel.searchDebounce()
                    hideErrorElements()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.editText.addTextChangedListener(simpleTextWatcher)

        binding.recyclerView.adapter = adapterSearch
        binding.recyclerViewSearchHistory.adapter = adapterHistory

        binding.editText.setOnFocusChangeListener { _, hasFocus ->      //отображение Layout при фокусе строки поиска
            if (hasFocus                            //Есть фокус
                && binding.editText.text.isEmpty()     //Строка поиска пуста
                && viewModel.tracksHistory().value!!.isNotEmpty()       //Список треков не пустой
            ) {
                updateRecyclerViewSearchHistory()
            } else {
                showAndHideHistoryLayout(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showNawBar()
        viewModel.updateTrackHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroy()
    }

    private fun startPlayerFragment(track: Track) {              //Запустили активити с плеером

        (activity as? MainActivity)?.hideNavBar()

        val bundle = Bundle()
        bundle.putParcelable(TRACK_KEY, track)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment, bundle)

    }

    private fun updateRecyclerViewSearchHistory() {                     //Обновление RecyclerView с историей поиска
        showAndHideHistoryLayout(true)
        viewModel.updateTrackHistory()
    }

    private fun showImageError(typeError: SearchStatus) {
        if (typeError == SearchStatus.LIST_IS_EMPTY) {                      //Ничего не нашлось
            showErrorNothingFound()
        } else {                                                    //Проблемы с сетью
            showErrorNetworkError()
            binding.buttonUpdate.isVisible = true
        }
        binding.messageImage.isVisible = true
        binding.textViewMessageError.isVisible = true
    }

    private fun showErrorNothingFound() {
        binding.textViewMessageError.text = getString(R.string.nothing_found)
        if (requireContext().isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_dark)
                .into(binding.messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_light)
                .into(binding.messageImage)
        }
    }

    private fun showErrorNetworkError() {
        binding.textViewMessageError.text = getString(R.string.network_error)
        if (requireContext().isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.network_problems_dark)
                .into(binding.messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.network_problems_light)
                .into(binding.messageImage)
        }
    }

    private fun hideErrorElements() {
        binding.messageImage.isVisible = false
        binding.textViewMessageError.isVisible = false
        binding.buttonUpdate.isVisible = false
    }

    private fun hideRecyclerView() {
        binding.recyclerView.isVisible = false
    }

    private fun changeStateWhenSearchBarIsEmpty() {                                            //при пустой строке поиска выполняем следующие действия
        hideErrorElements()
        hideRecyclerView()
        updateRecyclerViewSearchHistory()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
    }

    private fun showAndHideHistoryLayout(action: Boolean) {
        binding.historyLayout.isVisible =
            action && viewModel.tracksHistory().value!!.isNotEmpty() && binding.editText.hasFocus()
     }

    private fun processingSearchStatus(trackSearchResult: TrackSearchResult) {
        tracks.clear()
        hideRecyclerView()
        hideErrorElements()
        when (trackSearchResult.status) {
            SearchStatus.RESPONSE_RECEIVED -> {
                binding.progressBar.isVisible = false
                binding.recyclerView.isVisible = true
                tracks.addAll(trackSearchResult.results)
            }

            SearchStatus.LIST_IS_EMPTY -> {
                binding.progressBar.isVisible = false
                showImageError(SearchStatus.LIST_IS_EMPTY)
            }

            SearchStatus.NETWORK_ERROR -> {
                binding.progressBar.isVisible = false
                showImageError(SearchStatus.NETWORK_ERROR)
            }

            SearchStatus.DEFAULT -> {
                binding.progressBar.isVisible = false
            }

            SearchStatus.LOADING -> {
                binding.progressBar.isVisible = true
            }
        }
    }

    companion object {
        private const val TRACK_KEY = "track"
    }
}



