package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), ClickListenerForRecyclerView {

    private var editTextValue = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var searchHistory: SearchHistory
    private lateinit var tracks: ArrayList<Track>
    private lateinit var tracksHistory: ArrayList<Track>
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var adapterHistory: TrackAdapter
    private lateinit var buttonBack: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageImage: ImageView
    private lateinit var textViewMessageError: TextView
    private lateinit var buttonUpdate: Button
    private lateinit var historyLayout: LinearLayout
    private lateinit var recyclerViewSearchHistory: RecyclerView
    private lateinit var cleanHistoryButton: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        searchHistory = SearchHistory(applicationContext as AppSharedPreferences)
        tracks = ArrayList<Track>()
        tracksHistory = searchHistory.tracks
        adapterSearch = TrackAdapter(tracks, this)
        adapterHistory = TrackAdapter(tracksHistory, this)
        buttonBack = findViewById(R.id.button_back)
        inputEditText = findViewById(R.id.edit_text)
        clearButton = findViewById(R.id.clear_button)
        recyclerView = findViewById(R.id.recycler_view)
        messageImage = findViewById(R.id.message_image)
        textViewMessageError = findViewById(R.id.text_view_message_error)
        buttonUpdate = findViewById(R.id.button_update)
        historyLayout = findViewById(R.id.history_layout)
        recyclerViewSearchHistory = findViewById(R.id.recycler_view_search_history)
        cleanHistoryButton = findViewById(R.id.clean_history_button)

        buttonBack.setOnClickListener {
            finish()
        }
        buttonUpdate.setOnClickListener {
            sendRequeat()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideErrorElements()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            hideRecyclerView()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                historyLayout.visibility =
                    if (inputEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE    //отображение Layout при изменении текста в строке поиска
            }

            override fun afterTextChanged(s: Editable?) {
                editTextValue = inputEditText.text.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        recyclerView.adapter = adapterSearch
        recyclerViewSearchHistory.adapter = adapterHistory

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequeat()
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->      //отображение Layout при фокусе строки поиска
            if (hasFocus && inputEditText.text.isEmpty()) {
                historyLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                tracksHistory = searchHistory.tracks
                adapterHistory.notifyDataSetChanged()
            } else {
                historyLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

//            historyLayout.visibility =
//                if (hasFocus && inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
//            tracksHistory = searchHistory.tracks
//            adapterHistory.notifyDataSetChanged()
            //наполнить recycleView
            //скрыть остальное


        }
    }

    private fun sendRequeat() {
        hideErrorElements()
        if (inputEditText.text.isNotEmpty()) {
            itunesService.search(inputEditText.text.toString())
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
        if (typeError.equals("List is empty")) {                      //Ничего не нашлось
            showErrorNothingFound()
        } else {                                                    //Проблемы с сетью
            showErrorNetworkError()
            buttonUpdate.visibility = View.VISIBLE
            buttonUpdate.isEnabled = true
        }
        messageImage.visibility = View.VISIBLE
        textViewMessageError.visibility = View.VISIBLE
    }

    private fun showErrorNothingFound() {
        textViewMessageError.text = getString(R.string.nothing_found)
        if (this.isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_dark)
                .into(messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_light)
                .into(messageImage)
        }
    }

    private fun showErrorNetworkError() {
        textViewMessageError.text = getString(R.string.network_error)
        if (this.isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.network_problems_dark)
                .into(messageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.network_problems_light)
                .into(messageImage)
        }
    }

    private fun hideErrorElements() {
        messageImage.visibility = View.GONE
        textViewMessageError.visibility = View.GONE
        buttonUpdate.visibility = View.GONE
        buttonUpdate.isEnabled = false
    }

    private fun hideRecyclerView() {
        tracks.clear()
        adapterSearch.notifyDataSetChanged()
    }

    override fun onClick(track: Track) {
        searchHistory.addTrack(track)
    }

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

}



