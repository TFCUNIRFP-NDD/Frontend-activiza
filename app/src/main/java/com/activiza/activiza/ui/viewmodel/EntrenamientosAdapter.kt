package com.activiza.activiza.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.activiza.activiza.R
import com.activiza.activiza.data.EjerciciosData
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntrenamientosAdapter(private val ejercicios: List<EjerciciosData>,
private val onItemClick: (Int) -> Unit,)
: RecyclerView.Adapter<EntrenamientosAdapter.EjerciciosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio, parent, false)
        return EjerciciosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val ejercicio = ejercicios[position]
        //var db: ActivizaDataBaseHelper
        //db = ActivizaDataBaseHelper(holder.itemView.context)
        //var entrenamientoId = db.obtenerIdEntrenamientoPorIdEjercicio(ejercicio.id)
        holder.bind(ejercicio)
        holder.itemView.setOnClickListener {
            onItemClick(ejercicio.id) // Pasar el ID del entrenamiento al hacer clic en el elemento
        }
    }

    override fun getItemCount(): Int {
        return ejercicios.size
    }

    inner class EjerciciosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreEjercicio: TextView = itemView.findViewById(R.id.tvNombreEjercicioItem)
        private val imagenEjercicio: ImageView = itemView.findViewById(R.id.ivImagenEjercicioItem)

        @SuppressLint("ResourceType")
        fun bind(ejercicio: EjerciciosData) {
            nombreEjercicio.text = ejercicio.nombre
            deUrlAImageView(ejercicio.media,imagenEjercicio)
            var db: ActivizaDataBaseHelper
            db = ActivizaDataBaseHelper(nombreEjercicio.context)
            var entrenamientoId = db.obtenerIdEntrenamientoPorIdEjercicio(ejercicio.id)
            var ejercicioBooleano =db.obtenerEstadoDeEntrenamiento(entrenamientoId,obtenerFechaActual())

            if(ejercicioBooleano){
                itemView.setBackgroundColor(itemView.context.getColor(R.color.green))
            }
        }
    }
    fun deUrlAImageView(url:String, imageView: ImageView){
        Picasso.get().load(url).into(imageView)
    }
    fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

}