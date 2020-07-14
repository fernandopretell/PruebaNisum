package com.fernandopretell.pruebanisum.ui.main

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fernandopretell.componentes.button.SearchBar
import com.fernandopretell.model.model.ResponseItunesSong
import com.fernandopretell.model.model.SongResult
import com.fernandopretell.pruebanisum.R
import com.fernandopretell.pruebanisum.base.BaseActivity
import com.fernandopretell.pruebanisum.viewmodel.SearchSongsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var viewModel: SearchSongsViewModel
    private var conneted: Boolean? = null
    private var snackBar: Snackbar? = null
    private lateinit var adapter: ResultSongsAdapter
    private var mediaPlayer: MediaPlayer? = null
    private var itemList: List<SongResult>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        conneted = isNetworkVConnected(this)
        viewModel = ViewModelProviders.of(this).get(SearchSongsViewModel::class.java)
        viewModel.popularSongsLiveData.observe(this, Observer {
            itemList = it.results
            it.results.let {
                adapter.updateData(it)
                showLoading(false)
                search_bar.clearText()
            }
        })

    }

    override fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(nsContainer, "", Snackbar.LENGTH_LONG)

            val layout = snackBar?.getView() as Snackbar.SnackbarLayout
            layout.setBackgroundColor(
                ContextCompat.getColor(
                    layout.context,
                    android.R.color.transparent
                )
            )
            layout.setPadding(0, 0, 0, 0)

            val snackView =
                LayoutInflater.from(this@MainActivity).inflate(R.layout.snack_bar, null) as View

            layout.addView(snackView, 0)
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()

        } else {
            snackBar?.dismiss()
        }
    }

    private fun initView() {
        search_bar?.apply {
            textAppearance = R.style.TextAppearance_SearchBar
            showIcon = true
            hintColor = getColor(R.color.gray_4)
            textColor = getColor(R.color.black)
            hint = "Buscar canción"
            iconClose = R.drawable.ic_close
            iconSearch = R.drawable.ic_search
            onTextChanged(object : SearchBar.OnTextChanged {
                override fun onTextChanged(text: String) {
                    println("onTextChanged: $text")
                }
            })
            onItemSelected(object : SearchBar.OnItemSelected {
                override fun onItemSelected(element: Any?, position: Int) {
                    println("onItemSelected ${element as String}")
                    showLoading(true)
                    viewModel.fetchSongs(element)
                }
            })
        }

        val lim = LinearLayoutManager(this)
        adapter = ResultSongsAdapter(this, listener)
        recyclerViewMain.layoutManager = lim
        recyclerViewMain.adapter = adapter
    }

    private val listener = object : SongItemListener {
        override fun pressedPlayPause(song: SongResult, position: Int) {
            if(!song.playing) {
                itemList?.forEach {
                    it.playing = false
                }
                itemList?.get(position)?.playing = true
                adapter.notifyDataSetChanged()
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(song.previewUrl)
                    prepare()
                    start()
                }
            }else{
                itemList?.get(position)?.playing = false
                adapter.notifyItemChanged(position)
                mediaPlayer?.stop()

            }

            mediaPlayer?.setOnCompletionListener {
                itemList?.get(position)?.playing = false
                adapter.notifyItemChanged(position)
            }
        }
    }


}