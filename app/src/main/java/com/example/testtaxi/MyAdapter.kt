package com.example.testtaxi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testtaxi.dto.JsonList

class MyAdapter(
    private val items: List<JsonList>,
    private val itemClickListener: (JsonList) -> Unit
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val idTextView: TextView = view.findViewById(R.id.id)
        private val titleTextView: TextView = view.findViewById(R.id.title)
        private val imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(item: JsonList) {
            idTextView.text = item.id.toString()
            titleTextView.text = item.title

                /*
                    * Glide to load an image, it performs the network request
                    * and image decoding on a background thread. The library then
                    * handles updating the UI on the main thread once the image is
                    * ready.
                */
            // Use Glide to load the image lazily
            Glide.with(itemView.context)
                .load(item.url) // Replace with the actual URL
                .into(imageView)

            itemView.setOnClickListener { itemClickListener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
