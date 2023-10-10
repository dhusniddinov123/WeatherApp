package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R

class HourAdapter (var array: MutableList<Hour>, )
    : RecyclerView.Adapter<HourAdapter.MyHolder>() {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var temp = itemView.findViewById<TextView>(R.id.temp)
        var time = itemView.findViewById<TextView>(R.id.time)
        var wind = itemView.findViewById<TextView>(R.id.wind)
        var img = itemView.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_temp, parent, false)
        return MyHolder(view)
    }



    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var position = array.get(position)



        holder.temp.text = position.temp +" °C"
        holder.img.load("http:" + position.image)
        holder.wind.text = position.wind
        holder.time.text = position.time

    }
}

