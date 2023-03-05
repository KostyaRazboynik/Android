package com.konstantinmuzhik.audiorecorder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Adapter(private var records: ArrayList<AudioRecord>, var listener: OnItemClickListener): RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvFilename: TextView = itemView.findViewById(R.id.tvFilename)
        val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.onItemClickListener(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val record = records[position]

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date = Date(record.timestamp)
            val strDate = sdf.format(date)

            holder.tvFilename.text = record.filename
            holder.tvMeta.text = "${record.duration} $strDate"
        }
    }
}