package com.activiza.activiza.ui.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.activiza.activiza.R
import com.activiza.activiza.data.PexelsResponse
import com.activiza.activiza.data.Photo
import com.squareup.picasso.Picasso

class ImagenesAdapter(
    private val photos: List<Photo>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ImagenesAdapter.ImagenesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_imagen, parent, false)
        return ImagenesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagenesViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
        holder.itemView.setOnClickListener {
            onItemClick(photo.src.original)
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class ImagenesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagenSeleccionar: ImageView = itemView.findViewById(R.id.ivBackgroundItemRutina)

        fun bind(photo: Photo) {
            deUrlAImageView(photo.src.original, imagenSeleccionar)
        }
    }

    private fun deUrlAImageView(url: String, imageView: ImageView) {
        Picasso.get().load(url).fit().centerCrop().into(imageView)
    }
}
