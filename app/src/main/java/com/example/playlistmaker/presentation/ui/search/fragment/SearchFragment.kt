package com.example.playlistmaker.presentation.ui.search.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.presentation.ui.search.track.TrackAdapter
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.player.activity.PlayerActivity
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<SearchFragmentBinding>() {

    private var editTextValue = ""
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var tracks: ArrayList<Track>

    private val viewModel by viewModel<SearchViewModel>()

    private val onClick: (track: Track) -> Unit = {
        if (viewModel.clickDebounce()) {
            viewModel.addTrackInSearchHistory(it)
            adapterHistory.notifyDataSetChanged()
            startPlayerActivity(it)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SearchFragmentBinding {
        return SearchFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tracks = ArrayList<Track>()
        adapterSearch = TrackAdapter(tracks, onClick)
        adapterHistory = TrackAdapter(viewModel.getTracksHistory().value!!, onClick)

        viewModel.getFoundTracks().observe(viewLifecycleOwner) { it ->
            processingSearchStatus(it)
        }

        viewModel.getTracksHistory().observe(viewLifecycleOwner) { it ->
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
                binding.clearButton.visibility = clearButtonVisibility(s)
                binding.historyLayout.visibility =
                    if (binding.editText.hasFocus()         //Есть фокус
                        && s?.isEmpty() == true             //Строка поиска пуста
                        && viewModel.getTracksHistory().value!!.isNotEmpty()       //Список треков не пустой
                    ) View.VISIBLE else View.GONE           //отображение Layout при изменении текста в строке поиска

                editTextValue = binding.editText.text.toString()
                if (editTextValue.isEmpty()) {
                    changeStateWhenSearchBarIsEmpty()
                    viewModel.removeCallbacks()

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
                && viewModel.getTracksHistory().value!!.isNotEmpty()       //Список треков не пустой
            ) {
                updateRecyclerViewSearchHistory()
            } else {
                showAndHideHistoryLayout(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateTrackHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCallbacks()
    }

    private fun startPlayerActivity(track: Track) {              //Запустили активити с плеером
        Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(TRACK_KEY, track)
            startActivity(this)
        }
    }

    private fun updateRecyclerViewSearchHistory() {                     //Обновление RecyclerView с историей поиска
        showAndHideHistoryLayout(true)
        viewModel.updateTrackHistory()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {          //видимость кнопки "Очистить" в строке поиска
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showImageError(typeError: SearchStatus) {
        if (typeError == SearchStatus.LIST_IS_EMPTY) {                      //Ничего не нашлось
            showErrorNothingFound()
        } else {                                                    //Проблемы с сетью
            showErrorNetworkError()
            binding.buttonUpdate.visibility = View.VISIBLE
        }
        binding.messageImage.visibility = View.VISIBLE
        binding.textViewMessageError.visibility = View.VISIBLE
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
        binding.messageImage.visibility = View.GONE
        binding.textViewMessageError.visibility = View.GONE
        binding.buttonUpdate.visibility = View.GONE
    }

    private fun hideRecyclerView() {
        binding.recyclerView.visibility = View.GONE
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
        if (action && viewModel.getTracksHistory().value!!.isNotEmpty() && binding.editText.hasFocus()) {
            binding.historyLayout.visibility = View.VISIBLE
        } else {
            binding.historyLayout.visibility = View.GONE
        }
    }

    private fun processingSearchStatus(trackSearchResult: TrackSearchResult) {
        tracks.clear()
        hideRecyclerView()
        hideErrorElements()
        when (trackSearchResult.status) {
            SearchStatus.RESPONSE_RECEIVED -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                tracks.addAll(trackSearchResult.results)
            }

            SearchStatus.LIST_IS_EMPTY -> {
                binding.progressBar.visibility = View.GONE
                showImageError(SearchStatus.LIST_IS_EMPTY)
            }

            SearchStatus.NETWORK_ERROR -> {
                binding.progressBar.visibility = View.GONE
                showImageError(SearchStatus.NETWORK_ERROR)
            }

            SearchStatus.DEFAULT -> {
                binding.progressBar.visibility = View.GONE
            }

            SearchStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val TRACK_KEY = "track"
    }
}



