package com.activiza.activiza.ui.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.activiza.activiza.R
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RutinasAdapter(
    private val rutinas: List<RutinaData>,
    private val onItemClick: (Int) -> Unit,)
    : RecyclerView.Adapter<RutinasAdapter.RutinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rutina, parent, false)
        return RutinaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = rutinas[position]
        holder.bind(rutina)
        holder.itemView.setOnClickListener {
            onItemClick(rutina.id) // Pasar el ID de la rutina al hacer clic en el elemento
        }
    }

    override fun getItemCount(): Int {
        return rutinas.size
    }

    inner class RutinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreRutina: TextView = itemView.findViewById(R.id.tvNameItemRutina)
        private val imagenRutina: ImageView = itemView.findViewById(R.id.ivBackgroundItemRutina)

        fun bind(rutina: RutinaData) {
            nombreRutina.text = rutina.nombre
            deUrlAImageView(rutina.media,imagenRutina)
        }
    }
    fun deUrlAImageView(url:String, imageView:ImageView){
        Picasso.get().load(url).into(imageView)
    }

}