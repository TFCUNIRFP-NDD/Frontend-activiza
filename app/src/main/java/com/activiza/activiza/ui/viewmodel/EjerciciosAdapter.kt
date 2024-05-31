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

class EjerciciosAdapter(
    private val ejercicios: List<EjerciciosData>,
    private val numeroTotal: Int,
    private val onItemClick: (Int) -> Unit,
) : RecyclerView.Adapter<EjerciciosAdapter.EjerciciosViewHolder>() {

    private val selectedIds = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio_anadir, parent, false)
        return EjerciciosViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val ejercicio = ejercicios[position]
        holder.bind(ejercicio)
        holder.itemView.setOnClickListener {
            val ejercicioId = ejercicio.id

            // Agregar o eliminar el ID del ejercicio de la lista de selección
            if (ejercicioId in selectedIds) {
                selectedIds.remove(ejercicioId)
            } else {
                if(numeroTotal != selectedIds.size) {
                    selectedIds.add(ejercicioId)
                }
            }
            // Llamar al listener con el ID del ejercicio
            onItemClick(ejercicioId)
            // Notificar al adaptador que los datos han cambiado después de que se complete el evento de clic
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return ejercicios.size
    }

    inner class EjerciciosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repeticiones: TextView = itemView.findViewById(R.id.tvRepeticionesAnadirEjercicio)
        private val descanso: TextView = itemView.findViewById(R.id.tvDescansoAnadirEjercicio)
        private val duracion: TextView = itemView.findViewById(R.id.tvMinutosAnadirEjercicio)
        private val imagenEjercicio: ImageView = itemView.findViewById(R.id.ivBackgroundItemAnadirEjercicio)
        private val nombre: TextView = itemView.findViewById(R.id.tvNombreEjercicioItem)

        @SuppressLint("ResourceType")
        fun bind(ejercicio: EjerciciosData) {
            repeticiones.text = ejercicio.repeticiones.toString()
            descanso.text = ejercicio.descanso.toString()
            duracion.text = ejercicio.duracion.toString()
            nombre.text = ejercicio.nombre
            deUrlAImageView(ejercicio.media, imagenEjercicio)
            if (ejercicio.id in selectedIds) {
                if(numeroTotal >= selectedIds.size) {
                    itemView.setBackgroundColor(itemView.context.getColor(R.color.green))
                }
            }else{
                itemView.setBackgroundColor(itemView.context.getColor(R.color.transparent))
            }
            Log.d("messageDuracion",duracion.text.toString())
            if(duracion.text.equals("null") || duracion.text.isNullOrEmpty()){
                duracion.text = "NO"
            }
            if(descanso.text.equals("null") || descanso.text.isNullOrEmpty()){
                duracion.text = "NO"
            }
        }
    }

    fun deUrlAImageView(url: String, imageView: ImageView) {
        Picasso.get().load(url).into(imageView)
    }


}