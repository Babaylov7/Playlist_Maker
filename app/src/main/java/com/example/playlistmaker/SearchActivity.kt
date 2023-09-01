package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
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

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        buttonBack = findViewById(R.id.button_back)
        inputEditText = findViewById(R.id.editText)
        clearButton = findViewById(R.id.clearButton)
        recyclerView = findViewById(R.id.recyclerView)
        messageImage = findViewById(R.id.messageImage)
        textViewMessageError = findViewById(R.id.textViewMessageError)

        buttonBack.setOnClickListener {
            finish()
        }

        var nightModeOnValue = modeNightOn()

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
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
                hideErrorElements()
                if (inputEditText.text.isNotEmpty()) {
                    itunesService.search(inputEditText.text.toString())
                        .enqueue(object : Callback<TrackResponse> {
                            override fun onResponse(            //Ответ
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
                                        showImageError(nightModeOnValue, "List is empty")
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "Ничего не нашлось",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<TrackResponse>,
                                t: Throwable
                            ) {                                         //Возврат ошибки
                                showImageError(nightModeOnValue, "Network error")
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Ошибка сети",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        })
                    true
                }
            }
            false
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

    private fun modeNightOn(): Boolean{
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> return true
            Configuration.UI_MODE_NIGHT_NO -> return false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> return false
            else -> return false
        }
    }

    private fun showImageError(nightModeOn: Boolean, typeError: String){
        if(typeError.equals("List is empty")){
            textViewMessageError.text = "Ничего не нашлось"
            if (nightModeOn){
                Glide.with(this)
                    .load(R.drawable.nothing_was_found_dark)
                    .into(messageImage)
            }else{
                Glide.with(this)
                    .load(R.drawable.nothing_was_found_light)
                    .into(messageImage)
            }
        } else {
            textViewMessageError.text = "Проблемы со связью \n\nЗагрузка не удалась. Проверьте подключение к интернету"
            if(nightModeOn){
                Glide.with(this)
                    .load(R.drawable.network_problems_dark)
                    .into(messageImage)
            } else {
                Glide.with(this)
                    .load(R.drawable.network_problems_light)
                    .into(messageImage)
            }
        }
        messageImage.visibility = View.VISIBLE
        textViewMessageError.visibility = View.VISIBLE
    }

    private fun hideErrorElements() {
        messageImage.visibility = View.GONE
        textViewMessageError.visibility = View.GONE
    }
}