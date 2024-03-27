package com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_home_con_entrenamiento;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.proyecto_tfc.R;

import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home_training#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home_training extends Fragment {

    private CalendarView calendarView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView titulo;
    private TextView categorias;
    private TextView entrenamientosCompletados;

    private TextView diaEntrenamiento1;
    private TextView diaEntrenamiento1_porcentaje;
    private TextView diaEntrenamiento2;
    private TextView diaEntrenamiento2_porcentaje;
    private TextView diaEntrenamiento3;
    private TextView diaEntrenamiento3_porcentaje;


    public fragment_home_training() {
        // Required empty public constructor
    }
    public static fragment_home_training newInstance(String param1, String param2) {
        fragment_home_training fragment = new fragment_home_training();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_training, container, false);

        // Inicializar vistas
        titulo = rootView.findViewById(R.id.textoDisenoRutina);
        categorias = rootView.findViewById(R.id.textoDisenoRutinaCategoria);
        entrenamientosCompletados = rootView.findViewById(R.id.textoDisenoRutinaCompletado);
        //cardview 1 recojemos texto y porcentaje
        diaEntrenamiento1 = rootView.findViewById(R.id.cardViewTextoEntrenamiento1_1);
        diaEntrenamiento1_porcentaje = rootView.findViewById(R.id.cardViewTextoEntrenamiento1_2);
        //cardview 2 recojemos texto y porcentaje
        diaEntrenamiento2 = rootView.findViewById(R.id.cardViewTextoEntrenamiento2_1);
        diaEntrenamiento2_porcentaje = rootView.findViewById(R.id.cardViewTextoEntrenamiento2_2);
        //cardview 3 recojemos texto y porcentaje
        diaEntrenamiento3 = rootView.findViewById(R.id.cardViewTextoEntrenamiento3_1);
        diaEntrenamiento3_porcentaje = rootView.findViewById(R.id.cardViewTextoEntrenamiento3_2);

        // Aquí puedes acceder a tu base de datos y obtener los datos necesarios
        // Por ahora, simplemente estableceremos datos ficticios

        // Cambiar los datos para el FrameLayout
        titulo.setText("Nuevo texto de entrenamiento");
        categorias.setText("Nueva categoría");
        entrenamientosCompletados.setText("Entrenamiento completados: 5");

        //Lógica para ver si los porcentajes estan en rojo, naranja verde según el porcentaje de
        // entrenamiento (Datos de la bbdd)
        // Cambiar los datos para los CardViews
        diaEntrenamiento1.setText("Nuevo entrenamiento 1");
        diaEntrenamiento1_porcentaje.setText("50%");
        diaEntrenamiento2.setText("Nuevo entrenamiento 2");
        diaEntrenamiento2_porcentaje.setText("75%");
        diaEntrenamiento3.setText("Nuevo entrenamiento 3");
        diaEntrenamiento3_porcentaje.setText("100%");

        return rootView;
    }

}