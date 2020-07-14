package com.fernandopretell.pruebanisum.ui.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.model.model.SongResult
import com.fernandopretell.pruebanisum.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import com.fernandopretell.pruebanisum.util.MaterialPlayPauseButton

/**
 * Created by Fernando Pretell on 13/07/2020.
 * fernandopretell93@gmail.com
 *
 * Lima, Peru.
 **/
class ResultSongsAdapter(private val context: Context, private val listener: SongItemListener ) : RecyclerView.Adapter<ResultSongsAdapter.ResultSongsViewHolder>() {

    private val item: ArrayList<SongResult> = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ResultSongsViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_song_result, viewGroup, false)
        return ResultSongsViewHolder(v)
    }

    override fun onBindViewHolder(resultSongsViewHolder: ResultSongsViewHolder, position: Int) {

        val song = item[position]
        val f: NumberFormat = DecimalFormat("00")
        resultSongsViewHolder.ivPlayPause.setColor(Color.BLACK);
        resultSongsViewHolder.ivPlayPause.setAnimDuration(300);
        val requestOptionsProfile = RequestOptions()
            .placeholder(R.drawable.placeholder_user)
            .error(R.drawable.placeholder_user)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context).load(song.artworkUrl60).apply(requestOptionsProfile).into(resultSongsViewHolder.ivImageSong)
        resultSongsViewHolder.ivIndex.text = f.format(position+1)
        resultSongsViewHolder.tvNameSong.text = song.trackName
        resultSongsViewHolder.tvNameArtist.text = song.artistName
        if (song.playing){
            resultSongsViewHolder.ivPlayPause.setToPause()
            resultSongsViewHolder.ltAnimation.visibility = View.VISIBLE
            resultSongsViewHolder.ivIndex.visibility = View.GONE
        } else {
            resultSongsViewHolder.ivPlayPause.setToPlay()
            resultSongsViewHolder.ltAnimation.visibility = View.GONE
            resultSongsViewHolder.ivIndex.visibility = View.VISIBLE
        }
        resultSongsViewHolder.ivPlayPause.setOnClickListener {
            listener.pressedPlayPause(song, position)
            if(resultSongsViewHolder.ivPlayPause.state == MaterialPlayPauseButton.PLAY){
                resultSongsViewHolder.ivPlayPause.setToPause()
                resultSongsViewHolder.ltAnimation.visibility = View.VISIBLE
                resultSongsViewHolder.ivIndex.visibility = View.GONE
            }else {
                resultSongsViewHolder.ivPlayPause.setToPlay()
                resultSongsViewHolder.ltAnimation.visibility = View.GONE
                resultSongsViewHolder.ivIndex.visibility = View.VISIBLE
            }
        }
    }

    fun updateData(newData: List<SongResult>) {
        item.clear()
        item.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class ResultSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivIndex: TextView
        val ivImageSong: ImageView
        val ltAnimation: LottieAnimationView
        val ivPlayPause: MaterialPlayPauseButton
        val tvNameSong: TextView
        val tvNameArtist: TextView

        init {
            ivIndex = itemView.findViewById(R.id.tvIndexSong)
            ivImageSong = itemView.findViewById(R.id.ivImageSong)
            ltAnimation = itemView.findViewById(R.id.lottieAnimationView)
            ivPlayPause = itemView.findViewById(R.id.materialPlayPauseButton)
            tvNameSong = itemView.findViewById(R.id.tvNameSong)
            tvNameArtist = itemView.findViewById(R.id.tvNameArtist)
        }
    }
}

interface SongItemListener{

    fun pressedPlayPause(song: SongResult, position: Int)

}
