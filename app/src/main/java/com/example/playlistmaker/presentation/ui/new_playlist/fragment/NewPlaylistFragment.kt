package com.example.playlistmaker.presentation.ui.new_playlist.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.CreateNewPlaylistFragmentBinding
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.new_playlist.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class NewPlaylistFragment : BindingFragment<CreateNewPlaylistFragmentBinding>() {

    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var albumImageUri: Uri? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): CreateNewPlaylistFragmentBinding {
        return CreateNewPlaylistFragmentBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                binding.bCreatePlaylist.isEnabled = !p0.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        binding.tietName.addTextChangedListener(simpleTextWatcher)

        binding.ivButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.ivAlbum.setImageURI(uri)
                    albumImageUri = uri
                } else {}
            }

        binding.ivAlbum.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.bCreatePlaylist.setOnClickListener {
            val albumName = binding.tietName.text.toString()
            val albumDescription = binding.tietDescription.text.toString()
            var albumImageName = ""
            if (albumImageUri != null) {
                albumImageName = albumName + Calendar.getInstance().time.toString() + ".jpg"
                saveAlbumImageToPrivateStorage(albumImageName)
            }
            viewModel.createNewPlayList(albumName, albumDescription, albumImageName)

            Toast.makeText(
                requireContext(),
                String.format("Плейлист $albumName создан"),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }

    }


    private fun saveAlbumImageToPrivateStorage(albumImageName: String){

        val filePath =      //создаём экземпляр класса File, который указывает на нужный каталог
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                DIRECTORY
            )
        if (!filePath.exists()) {       //создаем каталог, если он не создан
            filePath.mkdirs()
        }

        val file = File(filePath, albumImageName)           //создаём экземпляр класса File, который указывает на файл внутри каталога
        val inputStream = requireActivity().contentResolver.openInputStream(albumImageUri!!)        // создаём входящий поток байтов из выбранной картинки
        val outputStream = FileOutputStream(file)       // создаём исходящий поток байтов в созданный выше файл
        BitmapFactory           // записываем картинку с помощью BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)
        inputStream!!.close()
        outputStream.close()
    }

    companion object{
        private const val DIRECTORY = "album_images"
        private const val QUALITY = 100
    }


}