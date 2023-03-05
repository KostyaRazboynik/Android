package com.konstantinmuzhik.audiorecorder

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GalleryActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var records: ArrayList<AudioRecord>
    private lateinit var mAdapter: Adapter
    private lateinit var db: AppDatabase
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        records = ArrayList()

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "audioRecords"
        ).build()

        mAdapter = Adapter(records, this)
        recyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fetchAll()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAll() {
        GlobalScope.launch {
            records.clear()
            records.addAll(db.audioRecordDao().getAll())

            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClickListener(position: Int) {

        mediaPlayer = MediaPlayer().apply {
            setDataSource(records[position].filePath)
            prepare()
        }
        playPausePlayer()
    }

    private fun playPausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        } else {
            mediaPlayer.start()
        }
    }
}