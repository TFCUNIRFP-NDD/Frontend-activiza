package com.activiza.activiza.ui.view.fragmentos

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentPerfilBinding
import com.activiza.activiza.databinding.FragmentSettingsBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date


class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null


    lateinit var db: ActivizaDataBaseHelper
    private var detallesUsuarioData: DetallesUsuarioData? = null
    private var peso: Double = 0.0
    private var altura: Double = 0.0
    private var caloriesPorKm: Double = 0.0
    private var stepsToday: Int = 0

    private val handler = android.os.Handler()
    private val resetStepsTask = object : Runnable {
        override fun run() {
            // Reiniciar los pasos y guardar la nueva hora de reinicio
            stepsToday = 0
            saveStepsResetTime(Calendar.getInstance().time)
            // Programar la próxima ejecución del reinicio de pasos en 24 horas
            handler.postDelayed(this, 24 * 60 * 60 * 1000) // 24 horas en milisegundos
        }
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
        if(uri!= null){
            binding.ivPerfil.setImageURI(uri)
        }else{
            //No imagen
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentPerfilBinding.inflate(inflater, container, false)
        val rootView = binding.root

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        // Inicializa la base de datos y obtiene los detalles del usuario
        db = ActivizaDataBaseHelper(requireContext())
        detallesUsuarioData = db.getDetallesUsuario()
        peso = detallesUsuarioData?.peso ?: 0.0
        altura = detallesUsuarioData?.altura ?: 0.0// Asigna 0.0 si el peso es nulo
        caloriesPorKm = 0.05 * peso // Calcula las calorías por kilómetro basadas en el peso del usuario

        // Actualiza los TextView con los valores del usuario
        binding.tvPeso.text = peso.toString() +" kg"
        binding.tvAltura.text = altura.toInt().toString() + " cm"



        // Obtiene y muestra el nombre del usuario
        val usuarioData: UsuarioData? = db.getUsuario()
        usuarioData.let {
            val nombreUsuario = it?.nombre

            binding.tvNombre.text = nombreUsuario
        }



        //carga los pasos y verifica si necesita reiniciarlos
        loadUserDetailsAndUpdateUI()
        updateProgressBars(peso, altura)
        initUI()
        loadSteps()
        checkStepsReset()



        if (stepSensor != null) {
            sensorManager.registerListener(stepCounterListener, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            // Maneja la situación cuando el dispositivo no tiene el sensor de contador de pasos
        }

        return rootView
    }

    private fun initUI() {



        binding.ivPerfil.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tvReset.setOnClickListener {
            mostrarDialogoPesoAltura()
        }
        binding.cvImc.visibility = View.GONE

        binding.btnCLose.setOnClickListener {
            binding.cvImc.visibility = View.GONE
            binding.tvReset.visibility = View.VISIBLE
            binding.btnImc.visibility = View.VISIBLE
        }

        binding.btnImc.setOnClickListener {
            detallesUsuarioData = db.getDetallesUsuario()
            peso = detallesUsuarioData?.peso ?: 0.0
            altura = detallesUsuarioData?.altura ?: 0.0
            val imc = calcularIMC(peso, altura)
            if(imc != null){
                mostrarIMC(imc)
            }else{
                binding.cvImc.visibility = View.GONE
                Toast.makeText(requireContext(), "error al calcular el imc", Toast.LENGTH_SHORT).show()
            }

        }
    }



    private val stepCounterListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            // Verifica si el evento no es nulo y es del tipo correcto (contador de pasos)
            event?.let {
                if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    // Actualiza la interfaz de usuario con el nuevo recuento de pasos
                    val steps = it.values[0].toInt()
                    updateStepCount(steps)
                    val caloriesBurned = calculateCaloriesBurned(steps)
                    updateCaloriesBurned(caloriesBurned)

                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }


    private fun calculateCaloriesBurned(steps: Int): Double {
        // Estimación promedio de la distancia por paso (en kilómetros)
        val distancePerStep = 0.762 // Puedes ajustar este valor según tu contexto
        // Convertir pasos en distancia (en kilómetros)
        val distanceWalked = steps * distancePerStep
        // Calcular las calorías quemadas
        return caloriesPorKm * distanceWalked
    }

    private fun updateCaloriesBurned(caloriesBurned: Double) {
        // Actualiza la interfaz de usuario con las calorías quemadas
        binding.tvCalorias.text = "Calorías quemadas: $caloriesBurned"
    }

    private fun updateStepCount(steps: Int) {
        // Actualiza la interfaz de usuario con el nuevo recuento de pasos
        // Por ejemplo, actualiza un TextView con el recuento de pasos
        binding.tvCountSteps.text = "Pasos: $steps"
    }

    private fun loadSteps() {
        val savedResetTime = getSavedResetTime()
        val currentTime = Calendar.getInstance().time
        if (savedResetTime != null && isMoreThan24HoursAgo(savedResetTime, currentTime)) {
            // Si han pasado más de 24 horas desde el último reinicio, reinicia los pasos
            stepsToday = 0
            saveStepsResetTime(currentTime)
        }
    }

    private fun saveStepsResetTime(currentTime: Date) {
        // Guardar la hora de reinicio de los pasos en la base de datos
        // Aquí deberías implementar la lógica para guardar la hora de reinicio de los pasos
        // Por ahora, simplemente imprimimos la hora de reinicio en el registro
        val currentTime = Calendar.getInstance().time
        println("Se reiniciaron los pasos a las: $currentTime")
    }

    private fun getSavedResetTime(): Date? {
        // Obtener la hora de reinicio de los pasos guardada en la base de datos
        // Aquí deberías implementar la lógica para obtener la hora de reinicio de los pasos
        // Por ahora, simplemente devolvemos null
        return null
    }

    private fun checkStepsReset() {
        val savedResetTime = getSavedResetTime()
        val currentTime = Calendar.getInstance().time
        if (savedResetTime == null || isMoreThan24HoursAgo(savedResetTime, currentTime)) {
            // Si han pasado más de 24 horas desde el último reinicio, reinicia los pasos
            stepsToday = 0
            saveStepsResetTime(currentTime)
        } else {
            // Si no ha pasado más de 24 horas, programa el reinicio de los pasos para después de 24 horas
            val timeDifference = savedResetTime.time + (24 * 60 * 60 * 1000) - currentTime.time
            handler.postDelayed(resetStepsTask, timeDifference)
        }
    }

    private fun isMoreThan24HoursAgo(previousTime: Date, currentTime: Date): Boolean {
        // Verificar si ha pasado más de 24 horas desde el previousTime hasta el currentTime
        val diff = currentTime.time - previousTime.time
        val hours = diff / (1000 * 60 * 60)
        return hours >= 24
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Detiene la escucha del sensor cuando el fragmento se destruye
        sensorManager.unregisterListener(stepCounterListener)
        _binding = null
    }

    private fun loadUserDetailsAndUpdateUI() {
        // Carga los detalles del usuario desde la base de datos
        detallesUsuarioData = db.getDetallesUsuario()
        peso = detallesUsuarioData?.peso ?: 0.0
        altura = detallesUsuarioData?.altura ?: 0.0

        // Actualiza las barras de progreso con los valores del usuario
        updateProgressBars(peso,altura)
    }

    private fun updateProgressBars(peso: Double, altura: Double) {
        // Calcula el progreso para las barras de progreso de peso y altura
        val progresoPeso = ((peso - 40) / (200 - 40) * 100).toInt()
        val progresoAltura = ((altura - 120) / (230 - 120) * 100).toInt()

        // Establece el progreso en las barras de progreso
        binding.pbPeso.progress = progresoPeso.coerceIn(0, 100)
        binding.pbAltura.progress = progresoAltura.coerceIn(0, 100)
    }

    fun mostrarDialogoPesoAltura() {
        val usuarioData: UsuarioData? = db.getUsuario()
        val token = usuarioData?.token
        val detallesUsuario: DetallesUsuarioData? = token?.let { db.getDetallesUsuarioPorToken(it) }

        if (detallesUsuario != null) {
            // Utiliza los detalles del usuario para mostrar los datos en el diálogo
            val builder = AlertDialog.Builder(requireContext())

            // Configura el diseño del AlertDialog
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialoo_personalizado_peso_altura, null)
            builder.setView(dialogView)

            // Configura los EditTexts para que el usuario introduzca el peso y la altura
            val etPeso = dialogView.findViewById<EditText>(R.id.etPeso)
            val etAltura = dialogView.findViewById<EditText>(R.id.etAltura)

            // Muestra los datos actuales del usuario en los EditTexts
            etPeso.setText(detallesUsuario.peso.toString())
            etAltura.setText(detallesUsuario.altura.toInt().toString())

            // Configura los botones de "Aceptar" y "Cancelar"
            builder.setPositiveButton("Aceptar") { dialog, which ->
                // Obtiene los valores ingresados por el usuario y los asigna a las variables
                val nuevoPeso = etPeso.text.toString().toDoubleOrNull() ?: 0.0
                val nuevaAltura = etAltura.text.toString().toDoubleOrNull() ?: 0.0

                // Validaciones de peso y altura
                if (peso in 40.0..200.0 && altura in 120.0..230.0) {
                    // Actualiza los textos de la interfaz de usuario
                    binding.tvPeso.text = "$nuevoPeso"
                    binding.tvAltura.text = "$nuevaAltura"

                    // Actualiza los detalles del usuario en la base de datos
                    token?.let { db.updateDetallesUsuarioPorToken(it, nuevoPeso, nuevaAltura) }

                    // Actualiza los TextView con los nuevos valores
                    binding.tvPeso.text = nuevoPeso.toString() + " Kg"
                    binding.tvAltura.text = nuevaAltura.toInt().toString() + " cm"

                    // Actualiza las barras de progreso con los nuevos valores
                    updateProgressBars(nuevoPeso, nuevaAltura)
                } else {
                    // Muestra un mensaje de error si los valores están fuera del rango permitido
                    val errorMsg = StringBuilder().apply {
                        if (nuevoPeso !in 40.0..200.0) append("Peso debe estar entre 40 y 200. ")
                        if (nuevaAltura !in 120.0..230.0) append("Altura debe estar entre 120 y 230.")
                    }.toString()

                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancelar") { dialog, which -> }

            builder.show()
        } else {
            // Maneja el caso en el que no se encuentren detalles de usuario para el token dado
            Toast.makeText(requireContext(), "No se encontraron detalles de usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calcularIMC(peso: Double, altura: Double): Double {
        // Fórmula para calcular el IMC: peso (kg) / (altura (m) * altura (m))
        if (altura == 0.0) {
            return 0.0 // Evitar división por cero
        }
        val alturaMetros = altura / 100 // Convertir altura de centímetros a metros
        return peso / (alturaMetros * alturaMetros)
    }

    private fun mostrarIMC(imc: Double) {
        // Muestra el IMC en el CardView y lo hace visible
        binding.cvImc.visibility = View.VISIBLE
        binding.tvReset.visibility = View.GONE
        binding.btnImc.visibility = View.GONE

        when(imc){
            in 0.00..18.50 -> binding.calculoImc.text = "Tu IMC es de %.2f".format(imc) +", tienes un peso bajo"

            in 18.51..24.99 -> binding.calculoImc.text = "Tu IMC es de %.2f".format(imc) +", tienes un peso normal"

            in 25.00..29.99 -> binding.calculoImc.text = "Tu IMC es de %.2f".format(imc) +", tienes sobrepeso"

            in 30.00..39.99 -> binding.calculoImc.text = "Tu IMC es de %.2f".format(imc) +", tienes obesidad"

            else -> binding.calculoImc.text = "Error al calcular tu IMC"
        }
    }
}