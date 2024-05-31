package com.activiza.activiza.ui.view.fragmentos

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.activiza.activiza.R
import com.activiza.activiza.data.DetallesUsuarioData
import com.activiza.activiza.data.UsuarioData
import com.activiza.activiza.databinding.FragmentPerfilBinding
import com.activiza.activiza.domain.ActivizaDataBaseHelper
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date


class PerfilFragment : Fragment(), SensorEventListener{

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var isCounterSensorPresent = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    private val PERMISSION_REQUEST_CODE = 101

    lateinit var db: ActivizaDataBaseHelper
    private var detallesUsuarioData: DetallesUsuarioData? = null
    private var peso: Double = 0.0
    private var altura: Double = 0.0
    private var caloriesPorKm: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val rootView = binding.root


        // Verificar si los permisos están concedidos
        try {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
                // Si los permisos no están concedidos, solicitarlos
                requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), PERMISSION_REQUEST_CODE)
            } else {
                // Si los permisos están concedidos, inicializar el sensor
                initSensor()
            }

        }catch (e: Exception){
            Log.e("pasos,","permisos denegados $e")
            Toast.makeText(requireContext(), "Permisos denegados", Toast.LENGTH_SHORT).show()
        }

        // Establece el color de la imagen en funcion del modo oscuro
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.ivRun.setImageResource(R.drawable.img_run_white)

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.ivRun.setImageResource(R.drawable.img_run_svg)
            }
        }


        // Inicializa la base de datos y obtiene los detalles del usuario
        db = ActivizaDataBaseHelper(requireContext())
        detallesUsuarioData = db.getDetallesUsuario()
        peso = detallesUsuarioData?.peso ?: 0.0
        altura = detallesUsuarioData?.altura ?: 0.0// Asigna 0.0 si el peso es nulo
        caloriesPorKm = 0.05 * peso // Calcula las calorías por kilómetro basadas en el peso del usuario

        // Actualiza los TextView con los valores del usuario
        binding.tvPeso.text = "$peso kg"
        binding.tvAltura.text = "${altura.toInt()} cm"


        // Obtiene y muestra el nombre del usuario
        val usuarioData: UsuarioData? = db.getUsuario()
        usuarioData.let {
            val nombreUsuario = it?.nombre
            binding.tvNombre.text = nombreUsuario
        }


        loadUserDetailsAndUpdateUI()
        updateProgressBars(peso, altura)
        initUI()

        return rootView
    }

    // -----FUNCIONES----
    private fun initUI() {
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

    private fun initSensor() {
        try {
            // Inicializar el SensorManager y el sensor
            sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
            stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            if (stepSensor != null) {
                isCounterSensorPresent = true
            } else {
                Toast.makeText(requireContext(), "Sensor no disponible", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al inicializar el sensor", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                // Verificar si el usuario concedió los permisos
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si los permisos están concedidos, inicializar el sensor
                    initSensor()
                } else {
                    // Si los permisos no están concedidos, informar al usuario y tomar medidas alternativas (opcional)
                    Toast.makeText(requireContext(), "Los permisos necesarios para contador", Toast.LENGTH_SHORT).show()
                    // Puedes deshabilitar funcionalidades relacionadas con el sensor o pedir permisos nuevamente
                }
            }
        }catch(e: Exception){
            Log.e("pasos,","error al pedir permisos $e")
            Toast.makeText(requireContext(), "Permisos denegados", Toast.LENGTH_SHORT).show()
        }

    }

    //Calcula las calorias quemadas
    private fun calculateCaloriesBurned(steps: Int): Double {
        val detallesUsuario = db.getDetallesUsuario()
        val altura = detallesUsuario?.altura ?: 0
        val peso = detallesUsuario?.peso ?: 0.0

        // Calculo de la zancada
        val zancada = (altura.toDouble()/100) * 0.414

        // Calculo de la distancia total recorrida
        val distancia = zancada * steps

        // Velocidad promedio en m/s
        val velocidad = 0.9

        // Calculo del tiempo total caminando en segundos
        val tiempoSegundos = distancia / velocidad

        // Convertir el tiempo a horas
        val tiempoHoras = tiempoSegundos / 3600

        // MET promedio para caminar a un ritmo moderado
        val met = 3.5

        // Calculo de las calorías quemadas
        val calorias = met * peso * tiempoHoras
        return calorias
    }




    //Actualiza las calorias
    private fun updateCaloriesBurned(caloriesBurned: Double) {
        val decimalFormat = DecimalFormat("#.##")
        val formattedCaloriesBurned = decimalFormat.format(caloriesBurned)
        binding.tvCalorias.text = "$formattedCaloriesBurned kcal"
    }

    // Carga los detalles del usuario y actualiza las barras de progreso
    private fun loadUserDetailsAndUpdateUI() {
        detallesUsuarioData = db.getDetallesUsuario()
        peso = detallesUsuarioData?.peso ?: 0.0
        altura = detallesUsuarioData?.altura ?: 0.0
        updateProgressBars(peso, altura)
    }

    // Actualiza las barras de progreso con los valores del usuario
    private fun updateProgressBars(peso: Double, altura: Double) {
        val progresoPeso = ((peso - 30) / (250 - 40) * 100).toInt()
        val progresoAltura = ((altura - 100) / (230 - 120) * 100).toInt()
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
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog_Dark)

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
                if(nuevoPeso in 30.0..250.0 && nuevaAltura in 100.0..230.0) {

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

            // Establece el tema del diálogo en función del modo oscuro
            val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isNightMode = nightMode == Configuration.UI_MODE_NIGHT_YES
            if (isNightMode) {
                builder.setInverseBackgroundForced(false) // Forzar el fondo inverso en modo oscuro
            }


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

    override fun onResume() {
        super.onResume()
        try {
            if (isCounterSensorPresent) {
                sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            }
        } catch (e: Exception) {
            Log.e("pasos","error al registrar el sensor $e")
            Toast.makeText(requireContext(), "Error al registrar el sensor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            if (isCounterSensorPresent) {
                sensorManager?.unregisterListener(this)
            }
        } catch (e: Exception) {
            Log.e("pasos","error al desregistrar el sensor $e")
            Toast.makeText(requireContext(), "Error al desregistrar el sensor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
                if (previousTotalSteps == 0f) {
                    previousTotalSteps = event.values[0]
                }

                val currentSteps = event.values[0]
                val stepsSinceReset = currentSteps - previousTotalSteps
                binding.tvCountSteps.text = stepsSinceReset.toInt().toString()

                // Puedes actualizar las calorías quemadas aquí
                val caloriesBurned = calculateCaloriesBurned(stepsSinceReset.toInt())
                updateCaloriesBurned(caloriesBurned)
            }
        } catch (e: Exception) {
            Log.e("pasos","error al actualizar los pasos $e")
            Toast.makeText(requireContext(), "Error al actualizar los pasos del sensor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }


    override fun onDestroyView() {
        super.onDestroyView()
        try {
            // Detiene la escucha del sensor cuando el fragmento se destruye
//            sensorManager.unregisterListener(stepCounterListener)
            _binding = null
        } catch (e: Exception) {
            Log.e("pasos","error al destruir la vista $e")
            Toast.makeText(requireContext(), "Error al destruir la vista", Toast.LENGTH_SHORT).show()
        }
    }
}