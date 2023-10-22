package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.SearchActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), ClickListenerForRecyclerView {

    private var editTextValue = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var binding: SearchActivityBinding
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var searchHistory: SearchHistory
    private lateinit var tracks: ArrayList<Track>
    private lateinit var tracksHistory: ArrayList<Track>
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var adapterHistory: TrackAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchHistory = SearchHistory(applicationContext as AppSharedPreferences)
        tracks = ArrayList<Track>()
        tracksHistory = searchHistory.getTracksHistory()
        adapterSearch = TrackAdapter(tracks, this)
        adapterHistory = TrackAdapter(tracksHistory, this)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonUpdate.setOnClickListener {
            sendRequest()
        }

        binding.clearButton.setOnClickListener {
            binding.editText.setText("")
            hideErrorElements()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.editText.windowToken, 0)
            hideRecyclerView()
            updateRecyclerViewSearchHistory()
        }

        binding.cleanHistoryButton.setOnClickListener {
            hideHistoryLayout()
            searchHistory.clean()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                binding.historyLayout.visibility =
                    if (binding.editText.hasFocus()        //Есть фокус
                        && s?.isEmpty() == true         //Строка поиска пуста
                        && tracksHistory.isNotEmpty()   //Список треков не пустой
                    ) View.VISIBLE else View.GONE    //отображение Layout при изменении текста в строке поиска
            }

            override fun afterTextChanged(s: Editable?) {
                editTextValue = binding.editText.text.toString()
            }
        }
        binding.editText.addTextChangedListener(simpleTextWatcher)

        binding.recyclerView.adapter = adapterSearch
        binding.recyclerViewSearchHistory.adapter = adapterHistory

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->      //отображение Layout при фокусе строки поиска
            if (hasFocus                            //Есть фокус
                && binding.editText.text.isEmpty()     //Строка поиска пуста
                && tracksHistory.isNotEmpty()       //Список треков не пустой
            ) {
                updateRecyclerViewSearchHistory()
            } else {
                hideHistoryLayout()
            }
        }
    }

    private fun sendRequest() {
        hideErrorElements()
        if (binding.editText.text.isNotEmpty()) {
            itunesService.search(binding.editText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(                                //Ответ
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapterSearch.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                hideRecyclerView()
                                showImageError("List is empty")
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<TrackResponse>,
                        t: Throwable
                    ) {                                                 //Возврат ошибки
                        hideRecyclerView()
                        showImageError("Network error")
                    }
                })
            true
        }
    }

    private fun updateRecyclerViewSearchHistory() {
        binding.historyLayout.visibility = View.VISIBLE
        tracksHistory = searchHistory.getTracksHistory()
        adapterHistory.notifyDataSetChanged()
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
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

    private fun showImageError(typeError: String) {
        if (typeError == "List is empty") {                      //Ничего не нашлось
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

    private fun hideHistoryLayout() {
        binding.historyLayout.visibility = View.GONE
    }

    override fun onClick(track: Track) {
        if(clickDebounce()) {
            searchHistory.addTrack(track)
            adapterHistory.notifyDataSetChanged()
            Intent(this, PlayerActivity::class.java).apply{
                putExtra("track", track)
                startActivity(this)
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}



