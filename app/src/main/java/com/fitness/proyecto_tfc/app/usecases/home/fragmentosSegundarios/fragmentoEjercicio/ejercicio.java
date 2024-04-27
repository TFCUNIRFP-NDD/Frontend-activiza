package com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmentoEjercicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.model.clases.Ejercicio;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_dia_entrenamiento.fragment_dia_rutina;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ejercicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ejercicio extends Fragment {

    TextView titulo;
    TextView descripcion;
    TextView repeticiones;
    TextView descanso;
    TextView minutos;
    TextView tiempo;
    TextInputEditText minutosAnadir;
    Button iniciar;
    private CountDownTimer countDownTimer;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ejercicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ejercicio.
     */
    // TODO: Rename and change types and number of parameters
    public static ejercicio newInstance(String param1, String param2) {
        ejercicio fragment = new ejercicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ejercicio, container, false);
        // Inflate the layout for this fragment
        titulo = view.findViewById(R.id.nombreEjercicio);
        descripcion = view.findViewById(R.id.descripcionEjercicio);
        repeticiones = view.findViewById(R.id.repeticionesTexto);
        descanso = view.findViewById(R.id.DescansoTexto);
        minutos = view.findViewById(R.id.minutosTexto);
        iniciar = view.findViewById(R.id.botonIniciar);
        minutosAnadir = view.findViewById(R.id.anadirMinutos);
        tiempo = view.findViewById(R.id.tiempoTexto);
        Ejercicio e = getEjercicioBBDD();

        titulo.setText(e.getNombre());
        descripcion.setText(e.getDescripcion());
        repeticiones.setText(""+e.getRepeticiones());
        descanso.setText(""+e.getDescanso());
        minutos.setText(""+(int) e.getMinutos());
        tiempo.setText(""+(int) e.getMinutos());

            iniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!tiempo.getText().toString().equalsIgnoreCase("00:00")) {
                        iniciarContadorTiempo();
                        Toast.makeText(getActivity(), "Boton clicado", Toast.LENGTH_SHORT).show();
                    }else{
                        // Si el porcentaje es un número, ejecutar la acción deseada aquí
                        // Inicia una transacción de fragmentos
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        // Reemplaza el fragmento actual con NuevoFragmento
                        fragment_dia_rutina fragmentoRutina = new fragment_dia_rutina();
                        fragmentTransaction.replace(R.id.fragment_container, fragmentoRutina);

                        // Opcional: puedes agregar la transacción a la pila para permitir el retroceso
                        fragmentTransaction.addToBackStack(null);

                        // Elimina el fragmento actual del back stack
                        fragmentManager.popBackStack();

                        // Agrega la transacción a la pila de retroceso (opcional)
                        fragmentTransaction.addToBackStack(null);

                        // Completa la transacción
                        fragmentTransaction.commit();
                    }
                }
            });
        return view;
    }

    public Ejercicio getEjercicioBBDD(){
        Ejercicio e = new Ejercicio();
        e.setId(1);
        e.setNombre("Press banca");
        e.setDescripcion("de 8 a 10 repeticiones");
        e.setDescanso(1.30);
        e.setRepeticiones(8);
        e.setMinutos(1);
        e.setSeries(8);
        e.setImagen(R.drawable.cinta_correr);
        e.setCompledado(false);
        return e;
    }
    // Método para iniciar el contador de tiempo
    private void iniciarContadorTiempo() {
        // Obtener el tiempo inicial en milisegundos (en este caso, 10 minutos)
        int t = Integer.parseInt(tiempo.getText().toString());
        long tiempoInicialMillis = t * 60 * 1000;

        // Crear y comenzar el CountDownTimer
        countDownTimer = new CountDownTimer(tiempoInicialMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Convertir los milisegundos restantes a formato de minutos:segundos
                int minutos = (int) (millisUntilFinished / 1000) / 60;
                int segundos = (int) (millisUntilFinished / 1000) % 60;

                // Formatear el tiempo restante en "mm:ss"
                String tiempoRestante = String.format("%02d:%02d", minutos, segundos);

                // Establecer el tiempo restante en el TextView
                tiempo.setText("Tiempo restante: " + tiempoRestante);
            }

            @Override
            public void onFinish() {
                // Cuando el tiempo llegue a cero
                iniciar.setText("Completar");
                tiempo.setText("00:00");
                Toast.makeText(getActivity(), "Tiempo finalizado", Toast.LENGTH_SHORT).show();
            }
        }.start(); // Comenzar el CountDownTimer
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detener el CountDownTimer al salir del fragmento para evitar fugas de memoria
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}