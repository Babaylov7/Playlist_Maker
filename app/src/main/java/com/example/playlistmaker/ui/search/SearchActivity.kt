package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.Creator
import com.example.playlistmaker.app.AppSharedPreferences
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.SearchStatus
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.local.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.ItunesApi
import com.example.playlistmaker.databinding.SearchActivityBinding
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.presentation.track.TrackAdapter
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.ui.player.PlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.Consumer

class SearchActivity : AppCompatActivity(), Consumer<List<Track>> {

    private var editTextValue = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var binding: SearchActivityBinding
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { sendRequest() }

    private lateinit var tracks: ArrayList<Track>
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var adapterHistory: TrackAdapter

    private var searchHistoryInteractorImpl = Creator.provideSearchHistoryInteractor()
    private var tracksHistory = searchHistoryInteractorImpl.getTracksHistory()
    private var trackInteractor = Creator.provideTrackInteractor()

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val onClick: (track: Track) -> Unit = {
        if (clickDebounce()) {
            searchHistoryInteractorImpl.addTrack(it)
            adapterHistory.notifyDataSetChanged()
            Intent(this, PlayerActivity::class.java).apply {
                putExtra("track", it)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tracks = ArrayList<Track>()
        adapterSearch = TrackAdapter(tracks, onClick)
        adapterHistory = TrackAdapter(tracksHistory, onClick)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonUpdate.setOnClickListener {
            searchDebounce()
        }

        binding.clearButton.setOnClickListener {
            binding.editText.setText("")
            hideKeyboard()
            changeStateWhenSearchBarIsEmpty()
        }

        binding.cleanHistoryButton.setOnClickListener {         //Очистка истории поиска
            showAndHideHistoryLayout(false)                      //Скрываем HistoryLayout
            searchHistoryInteractorImpl.clean()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                binding.historyLayout.visibility =
                    if (binding.editText.hasFocus()         //Есть фокус
                        && s?.isEmpty() == true             //Строка поиска пуста
                        && tracksHistory.isNotEmpty()       //Список треков не пустой
                    ) View.VISIBLE else View.GONE           //отображение Layout при изменении текста в строке поиска

                editTextValue = binding.editText.text.toString()
                if (editTextValue.isEmpty()) {
                    changeStateWhenSearchBarIsEmpty()
                } else {
                    searchDebounce()
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
                && tracksHistory.isNotEmpty()       //Список треков не пустой
            ) {
                updateRecyclerViewSearchHistory()
            } else {
                showAndHideHistoryLayout(false)
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
        super.onDestroy()
    }

    private fun sendRequest() {
        hideErrorElements()
        if (binding.editText.text.isNotEmpty()) {
            showAndHideProgressBar(true)
            itunesService.search(binding.editText.text.toString())
                .enqueue(object : Callback<TrackSearchResponse> {
                    override fun onResponse(                                //Ответ
                        call: Call<TrackSearchResponse>,
                        response: Response<TrackSearchResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapterSearch.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                hideRecyclerView()
                                showImageError(SearchStatus.LIST_IS_EMPTY)
                            }
                        }
                        showAndHideProgressBar(false)
                    }

                    override fun onFailure(
                        call: Call<TrackSearchResponse>,
                        t: Throwable
                    ) {                                                 //Возврат ошибки
                        showAndHideProgressBar(false)
                        hideRecyclerView()
                        showImageError(SearchStatus.NETWORK_ERROR)
                    }
                })
            true
        }
    }

    private fun testSearch(){
        hideErrorElements()
        if(binding.editText.text.isNotEmpty()){
            trackInteractor.searchTracks(binding.editText.text.toString(), this)
        }
    }

    private fun updateRecyclerViewSearchHistory() {                     //Обновление RecyclerView с историей поиска
        showAndHideHistoryLayout(true)
        tracksHistory = searchHistoryInteractorImpl.getTracksHistory()
        adapterHistory.notifyDataSetChanged()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {          //видимость кнопки "Очистить" в строке поиска
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, editTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextValue = savedInstanceState.getString(SEARCH_TEXT, "")
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
        if (this.isNightModeOn()) {
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
        if (this.isNightModeOn()) {
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
        tracks.clear()
        adapterSearch.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {                                          //В момент вызова функции searchDebounce() мы удаляем
        handler.removeCallbacks(searchRunnable)                             //последнюю запланированную отправку запроса и тут же,
        handler.postDelayed(
            searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )                                                                   //используя метод postDelayed(), планируем запуск этого же
    }                                                                       //Runnable через две секунды.

    private fun changeStateWhenSearchBarIsEmpty() {                                            //при пустой строке поиска выполняем следующие действия
        hideErrorElements()
        hideRecyclerView()
        updateRecyclerViewSearchHistory()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
    }

    private fun showAndHideHistoryLayout(action: Boolean) {
        if (action && tracksHistory.isNotEmpty()) {
            binding.historyLayout.visibility = View.VISIBLE
        } else {
            binding.historyLayout.visibility = View.GONE
        }
    }

    private fun showAndHideProgressBar(action: Boolean) {
        if (action) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    override fun accept(p0: List<Track>) {
        TODO("Not yet implemented")
    }

}



