package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val NETWORK_ERROR =
            "Проблемы со связью \n\nЗагрузка не удалась. Проверьте подключение к интернету"
        const val NOTHING_FOUND = "Ничего не нашлось"
    }

    private var editTextValue = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)
    private lateinit var buttonBack: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageImage: ImageView
    private lateinit var textViewMessageError: TextView
    private lateinit var buttonUpdate: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        buttonBack = findViewById(R.id.button_back)
        inputEditText = findViewById(R.id.edit_text)
        clearButton = findViewById(R.id.clear_button)
        recyclerView = findViewById(R.id.recycler_view)
        messageImage = findViewById(R.id.message_image)
        textViewMessageError = findViewById(R.id.text_view_message_error)
        buttonUpdate = findViewById(R.id.button_update)

        var nightModeOnValue = modeNightOn()

        buttonBack.setOnClickListener {
            finish()
        }
        buttonUpdate.setOnClickListener {
            sendRequeat(nightModeOnValue)
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            hideRecyclerView()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                editTextValue = inputEditText.text.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        recyclerView.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequeat(nightModeOnValue)
            }
            false
        }
    }

    private fun sendRequeat(nightModeOnValue: Boolean) {
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
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                hideRecyclerView()
                                showImageError(nightModeOnValue, "List is empty")
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<TrackResponse>,
                        t: Throwable
                    ) {                                                 //Возврат ошибки
                        hideRecyclerView()
                        showImageError(nightModeOnValue, "Network error")
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

    private fun modeNightOn(): Boolean {                         //Вопрос ревьюеру - Может ли эта функция неправильно отработать? Есть ли в ней недостатки?
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> return true
            Configuration.UI_MODE_NIGHT_NO -> return false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> return false
            else -> return false
        }
    }

    private fun showImageError(nightModeOn: Boolean, typeError: String) {
        if (typeError.equals("List is empty")) {                      //Ничего не нашлось
            textViewMessageError.text = NOTHING_FOUND
            if (nightModeOn) {
                Glide.with(this)
                    .load(R.drawable.nothing_was_found_dark)
                    .into(messageImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.nothing_was_found_light)
                    .into(messageImage)
            }
        } else {                                                    //Проблемы с сетью
            textViewMessageError.text = NETWORK_ERROR
            if (nightModeOn) {
                Glide.with(this)
                    .load(R.drawable.network_problems_dark)
                    .into(messageImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.network_problems_light)
                    .into(messageImage)
            }
            buttonUpdate.visibility = View.VISIBLE
            buttonUpdate.isEnabled = true
        }
        messageImage.visibility = View.VISIBLE
        textViewMessageError.visibility = View.VISIBLE
    }

    private fun hideErrorElements() {
        messageImage.visibility = View.GONE
        textViewMessageError.visibility = View.GONE
        buttonUpdate.visibility = View.GONE
        buttonUpdate.isEnabled = false
    }

    private fun hideRecyclerView() {
        tracks.clear()
        adapter.notifyDataSetChanged()
    }
}