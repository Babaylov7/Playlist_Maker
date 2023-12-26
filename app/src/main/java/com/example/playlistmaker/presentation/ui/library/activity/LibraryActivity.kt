package com.example.playlistmaker.presentation.ui.library.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LibraryActivityBinding
import com.example.playlistmaker.presentation.ui.library.LibraryViewPageAdapter
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: LibraryActivityBinding
    private lateinit var tabMediator: TabLayoutMediator


    private val viewModel by viewModel<LibraryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LibraryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpLibrary.adapter = LibraryViewPageAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tlLibrary, binding.vpLibrary){
            tab, position ->
                when(position) {
                    0 -> tab.text = getString(R.string.featured_tracks)
                    1 -> tab.text = getString(R.string.playlists)
                }
        }
        tabMediator.attach()

        binding.ivButtonBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}