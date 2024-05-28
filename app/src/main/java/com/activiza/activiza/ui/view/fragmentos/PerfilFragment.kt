package com.activiza.activiza.ui.view.fragmentos

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentPerfilBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
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

    private val handler = android.os.Handler(Looper.getMainLooper())
    private val resetStepsTask = object : Runnable {
        override fun run() {
            // Reiniciar los pasos y guardar la nueva hora de reinicio
            stepsToday = 0
            saveStepsResetTime(Calendar.getInstance().time)
            // Programa la próxima ejecución del reinicio de pasos en 24 horas
            handler.postDelayed(this, 24 * 60 * 60 * 1000) // 24 horas en milisegundos
        }
    }

    //Para seleccionar imagen de perfil
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivPerfil.setImageURI(uri)
            //saveImageUri(uri.toString())
        } else {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val rootView = binding.root

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        // Inicializa la base de datos y obtiene los detalles del usuario
        db = ActivizaDataBaseHelper(requireContext())
        detallesUsuarioData = db.getDetallesUsuario()
        peso = detallesUsuarioData?.peso ?: 0.0
        altura = detallesUsuarioData?.altura ?: 0.0// Asigna 0.0 si el peso es nulo
        caloriesPorKm =
            0.05 * peso // Calcula las calorías por kilómetro basadas en el peso del usuario

        // Actualiza los TextView con los valores del usuario
        binding.tvPeso.text = peso.toString() + " kg"
        binding.tvAltura.text = altura.toInt().toString() + " cm"


        // Obtiene y muestra el nombre del usuario
        val usuarioData: UsuarioData? = db.getUsuario()
        usuarioData.let {
            val nombreUsuario = it?.nombre

            binding.tvNombre.text = nombreUsuario
        }

        // Carga la imagen guardada, si existe
        //loadImageUri()

        //carga los pasos y verifica si necesita reiniciarlos
        loadUserDetailsAndUpdateUI()
        updateProgressBars(peso, altura)
        initUI()
        loadSteps()
        checkStepsReset()



        if (stepSensor != null) {
            stepSensor?.let {
                sensorManager.registerListener(
                    stepCounterListener,
                    it,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Este dispositivo no tiene sensor de pasos",
                Toast.LENGTH_SHORT
            ).show()
        }

        return rootView
    }


    // -----FUNCIONES----
    private fun initUI() {

        binding.ivPerfil.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.tvReset.setOnClickListener {
            mostrarDialogoPesoAltura()
        }
        binding.llImc.visibility = View.GONE
        binding.pbImc.visibility = View.GONE



        binding.btnImc.setOnClickListener {
            detallesUsuarioData = db.getDetallesUsuario()
            peso = detallesUsuarioData?.peso ?: 0.0
            altura = detallesUsuarioData?.altura ?: 0.0
            val imc = calcularIMC(peso, altura)
            if (imc != null) {
                mostrarIMC(imc)
            } else {
                binding.llImc.visibility = View.GONE
                Toast.makeText(requireContext(), "error al calcular el imc", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    //Funciones para guardar y cargar la URI de la imagen
//    private fun saveImageUri(uri: String) {
//        val sharedPreferences = requireContext().getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("profile_image_uri", uri)
//        editor.apply()
//    }
//
//    private fun loadImageUri() {
//        val sharedPreferences = requireContext().getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE)
//        val uriString = sharedPreferences.getString("profile_image_uri", null)
//        if (uriString != null) {
//            val uri = Uri.parse(uriString)
//            binding.ivPerfil.setImageURI(uri)
//        }
//    }

    // Funciones para el sensor de contador de pasos y Calorias

    // Maneja el sensor de contador de pasos
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


    //Calcula las calorias quemadas
    private fun calculateCaloriesBurned(steps: Int): Double {
        // Estimación promedio de la distancia por paso (en kilómetros)
        val distancePerStep = 0.762
        // Convertir pasos en distancia (en kilómetros)
        val distanceWalked = steps * distancePerStep
        // Calcular las calorías quemadas
        return caloriesPorKm * distanceWalked
    }


    //Actualiza las calorias
    private fun updateCaloriesBurned(caloriesBurned: Double) {
        // Actualiza la interfaz de usuario con las calorías quemadas
        binding.tvCalorias.text = "Calorías quemadas: $caloriesBurned"
    }


    //Actualiza los pasos
    private fun updateStepCount(steps: Int) {
        // Actualiza la interfaz de usuario con el nuevo recuento de pasos
        binding.tvCountSteps.text = "Pasos: $steps"
    }

    //Carga los pasos
    private fun loadSteps() {
        val savedResetTime = getSavedResetTime()
        val currentTime = Calendar.getInstance().time
        if (savedResetTime != null && isMoreThan24HoursAgo(savedResetTime, currentTime)) {
            // Si han pasado más de 24 horas desde el último reinicio, reinicia los pasos
            stepsToday = 0
            saveStepsResetTime(currentTime)
        }
    }

    //Guarda la hora de reinicio de los pasos
    private fun saveStepsResetTime(currentTime: Date) {
        // Guardar la hora de reinicio de los pasos en la base de datos
        val currentTime = Calendar.getInstance().time

    }

    //Carga la hora de reinicio de los pasos
    private fun getSavedResetTime(): Date? {
        return null
    }

    // Verifica si han pasado más de 24 horas desde el previousTime hasta el currentTime
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

    // Verifica si han pasado más de 24 horas desde el previousTime hasta el currentTime
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

    // Carga los detalles del usuario y actualiza las barras de progreso
    private fun loadUserDetailsAndUpdateUI() {
        // Carga los detalles del usuario desde la base de datos
        detallesUsuarioData = db.getDetallesUsuario()
        peso = detallesUsuarioData?.peso ?: 0.0
        altura = detallesUsuarioData?.altura ?: 0.0

        // Actualiza las barras de progreso con los valores del usuario
        updateProgressBars(peso, altura)
    }


    // Actualiza las barras de progreso con los valores del usuario
    private fun updateProgressBars(peso: Double, altura: Double) {
        // Calcula el progreso para las barras de progreso de peso y altura
        val progresoPeso = ((peso - 30) / (250 - 40) * 100).toInt()
        val progresoAltura = ((altura - 100) / (230 - 120) * 100).toInt()

        // Establece el progreso en las barras de progreso
        binding.pbPeso.progress = progresoPeso.coerceIn(0, 100)
        binding.pbAltura.progress = progresoAltura.coerceIn(0, 100)
    }

    // Muestra el diálogo para introducir el peso y la altura
    fun mostrarDialogoPesoAltura() {
        val usuarioData: UsuarioData? = db.getUsuario()
        val token = usuarioData?.token
        val detallesUsuario: DetallesUsuarioData? = token?.let { db.getDetallesUsuarioPorToken(it) }

        if (detallesUsuario != null) {
            // Utiliza los detalles del usuario para mostrar los datos en el diálogo
            val builder = AlertDialog.Builder(requireContext())

            // Configura el diseño del AlertDialog
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.dialogo_personalizado_peso_altura, null)
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

                //Mostramos y ocultamos los elementos correspondientes
                binding.btnImc.visibility = View.VISIBLE
                binding.llImc.visibility = View.GONE
                binding.pbImc.visibility = View.GONE
                binding.tvCalculoImc.visibility = View.GONE

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
                        if (nuevoPeso !in 30.0..250.0) append("Peso debe estar entre 30 y 250. ")
                        if (nuevaAltura !in 100.0..230.0) append("Altura debe estar entre 120 y 230.")
                    }.toString()

                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancelar") { dialog, which -> }

            builder.show()
        } else {
            // Maneja el caso en el que no se encuentren detalles de usuario para el token dado
            Toast.makeText(
                requireContext(),
                "No se encontraron detalles de usuario",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    //Calcula el Imc
    private fun calcularIMC(peso: Double, altura: Double): Double {
        // Fórmula para calcular el IMC: peso (kg) / (altura (m) * altura (m))
        if (altura == 0.0) {
            return 0.0 // Evitar división por cero
        }
        val alturaMetros = altura / 100 // Convertir altura de centímetros a metros
        return peso / (alturaMetros * alturaMetros)
    }

    //Muestra el Imc
    private fun mostrarIMC(imc: Double) {
        // Muestra el IMC en el CardView y lo hace visible
        binding.llImc.visibility = View.VISIBLE
        binding.btnImc.visibility = View.GONE
        binding.pbImc.visibility = View.VISIBLE

        // Definir el rango de IMC mínimo y máximo
        val minImc = 0.0
        val maxImc = 40.0

        // Convierte el IMC actual al rango de 0 a 100 (porcentaje)
        val progress = ((imc - minImc) / (maxImc - minImc) * 100).toInt()

        // Establece el progreso del ProgressBar
        binding.pbImc.progress = progress

        when (imc) {
            in 0.00..18.50 -> {
                binding.tvCalculoImc.text = "Tienes un peso bajo"
                binding.tvImc.text = "%.2f".format(imc)
            }

            in 18.51..24.99 -> {
                binding.tvCalculoImc.text = "Tienes un peso normal"
                binding.tvImc.text = "%.2f".format(imc)
            }

            in 25.00..29.99 -> {
                binding.tvCalculoImc.text = "Tienes sobrepeso"
                binding.tvImc.text = "%.2f".format(imc)
            }

            in 30.00..39.99 -> {
                binding.tvCalculoImc.text = "Tienes obesidad"
                binding.tvImc.text = "%.2f".format(imc)
            }

            else -> binding.tvCalculoImc.text = "Error al calcular tu IMC"
        }
    }
}