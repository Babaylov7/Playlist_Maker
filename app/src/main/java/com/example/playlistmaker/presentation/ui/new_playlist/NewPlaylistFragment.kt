package com.example.playlistmaker.presentation.ui.new_playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.CreateNewPlaylistFragmentBinding
import com.example.playlistmaker.presentation.ui.BindingFragment

class NewPlaylistFragment: BindingFragment<CreateNewPlaylistFragmentBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): CreateNewPlaylistFragmentBinding {
        return CreateNewPlaylistFragmentBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }





//    private fun startPlayerActivity(track: Track) {              //Запустили активити с плеером
//        Intent(requireContext(), PlayerActivity::class.java).apply {
//            putExtra(SearchFragment.TRACK_KEY, track)
//            startActivity(this)
//        }
//    }
}