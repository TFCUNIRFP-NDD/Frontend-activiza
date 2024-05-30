package com.activiza.activiza.ui.view.fragmentos

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.activiza.activiza.R
import com.activiza.activiza.data.RutinaData
import com.activiza.activiza.databinding.FragmentEntrenamientosBinding
import com.activiza.activiza.databinding.FragmentPanelDeControlBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
class PanelDeControlFragment : Fragment() {

    private var _binding: FragmentPanelDeControlBinding? = null
    private val binding get() = _binding!!
    lateinit var rutina: RutinaData
    lateinit var db: ActivizaDataBaseHelper
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPanelDeControlBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // Establece colores en funcion del modo oscuro
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            // Modo Oscuro
            //binding.tvNameRutinaPanelControl.setTextColor(resources.getColor(R.color.white,null))
            binding.btnPanelDeControl.background = resources.getDrawable(R.drawable.button_custom, null)


        } else {
            // Modo Claro
            //binding.tvNameRutinaPanelControl.setTextColor(resources.getColor(R.color.black,null))
        }

        initUI()
        return rootView



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        recogerRutina()
        comprobarRutina()
        pintarDatos()
        calendarioModificado()
        inicializarEventos()
    }

    private fun comprobarRutina() {
        if(db.obtenerFecha() != obtenerFechaActual() || db.obtenerFecha().isNullOrEmpty()){
            db.cambiarFechaActual(obtenerFechaActual(), rutina.id)
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calendarioModificado() {
        val textViewMonthYear = binding.textViewMonthYear
        val gridViewCalendar = binding.gridViewCalendar

        val today = LocalDate.now()
        val currentMonth = YearMonth.now()

        textViewMonthYear.text = currentMonth.month.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        ) + " " + currentMonth.year

        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value

        val daysArray = arrayOfNulls<String>(42)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDayIndex = firstDayOfWeek + today.dayOfMonth - 2

        var currentDay = 1
        for (i in 0 until daysArray.size) {
            if (i < firstDayOfWeek - 1 || i >= daysInMonth + firstDayOfWeek - 1) {
                daysArray[i] = ""
            } else {
                daysArray[i] = currentDay.toString()
                currentDay++
            }
        }

        // Obtener el calendario de entrenamiento desde la base de datos
        val calendarioEntrenamiento = db.obtenerCalendarioEntrenamiento(rutina.id)

        // Define un ArrayAdapter personalizado para cambiar el color de fondo de los días
        val adapter = object : ArrayAdapter<String>(
            binding.tvDescripcionPanelDeControl.context,
            R.layout.grid_item_day,
            daysArray
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val dayString = getItem(position)

                // Restablece el color de fondo predeterminado
                view.setBackgroundColor(Color.TRANSPARENT)

                if (!dayString.isNullOrEmpty()) {
                    val day = dayString.toInt()
                    val date = currentMonth.atDay(day).format(dateFormatter)

                    val entrenamiento = calendarioEntrenamiento.find { it.fecha == date }

                    if (entrenamiento != null) {
                        when {
                            entrenamiento.completado -> {
                                view.setBackgroundResource(R.color.green)
                                //Compruebo que la fecha coincide con la actual
                                if(LocalDate.parse(date).isEqual(today)){
                                    //Deshabilito el boton y le digo que este en color gris
                                    binding.btnComenzarEntrenamiento.setBackgroundColor(ContextCompat.getColor(binding.btnComenzarEntrenamiento.context, R.color.green))
                                    binding.btnComenzarEntrenamiento.isClickable = false
                                }
                            }
                            entrenamiento.tocaEntrenar -> view.setBackgroundResource(R.color.splash_background)
                            else -> {
                                view.setBackgroundColor(Color.TRANSPARENT)
                                //Compruebo que la fecha coincide con la actual
                                if(LocalDate.parse(date).isEqual(today)){
                                    //Deshabilito el boton y le digo que este en color gris
                                    binding.btnComenzarEntrenamiento.setBackgroundColor(ContextCompat.getColor(binding.btnComenzarEntrenamiento.context, R.color.gray_dark))
                                    binding.btnComenzarEntrenamiento.isClickable = false
                                }
                            }
                        }
                    } else if (LocalDate.parse(date).isBefore(today)) {
                        view.setBackgroundColor(Color.GRAY)
                    }
                }
                return view
            }
        }

        gridViewCalendar.adapter = adapter
    }


    private fun inicializarEventos() {
        binding.btnEntrenamientos.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnEliminarRutina.setOnClickListener{
            db.borrarEjerciciosYRutinas()
            findNavController().popBackStack()
        }
        binding.btnComenzarEntrenamiento.setOnClickListener {
            findNavController().navigate(R.id.action_panelDeControlFragment_to_comenzarEntrenamientoFragment)
        }
    }

    private fun pintarDatos() {
        binding.tvNameRutinaPanelControl.text = rutina.nombre
        binding.tvDescripcionPanelDeControl.text = rutina.descripcion
        deUrlAImageView(rutina.media,binding.ivRutinaPanelControl)
    }
    fun deUrlAImageView(url:String, imageView: ImageView){
        Picasso.get().load(url).into(imageView)
    }

    private fun recogerRutina() {
        db = ActivizaDataBaseHelper(binding.tvDescripcionPanelDeControl.context)
        rutina = db.obtenerPrimeraRutina()!!
    }
    fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}