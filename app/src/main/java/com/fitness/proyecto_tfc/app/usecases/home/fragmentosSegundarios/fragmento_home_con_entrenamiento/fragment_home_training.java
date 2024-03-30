package com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_home_con_entrenamiento;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragment_home_home_segundario;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_dia_entrenamiento.fragment_dia_rutina;

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
    private CardView cardViewEntrenamiento1;
    private CardView cardViewEntrenamiento2;
    private CardView cardViewEntrenamiento3;

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

        // Inicializar CardViews
        cardViewEntrenamiento1 = rootView.findViewById(R.id.cardViewEntrenamiento1);
        cardViewEntrenamiento2 = rootView.findViewById(R.id.cardViewEntrenamiento2);
        cardViewEntrenamiento3 = rootView.findViewById(R.id.cardViewEntrenamiento3);

        // Establecer OnClickListener para cada CardView
        cardViewEntrenamiento1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String porcentaje1 = ((TextView) cardViewEntrenamiento1.findViewById(R.id.cardViewTextoEntrenamiento1_2)).getText().toString();
                if (isNumeric(porcentaje1)) {
                    // Si el porcentaje es un número, ejecutar la acción deseada aquí
                    // Inicia una transacción de fragmentos
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Reemplaza el fragmento actual con NuevoFragmento
                    fragment_dia_rutina fragmentoRutina = new fragment_dia_rutina();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentoRutina);

                    // Opcional: puedes agregar la transacción a la pila para permitir el retroceso
                    fragmentTransaction.addToBackStack(null);

                    // Completa la transacción
                    fragmentTransaction.commit();

                }
            }
        });

        cardViewEntrenamiento2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String porcentaje2 = ((TextView) cardViewEntrenamiento2.findViewById(R.id.cardViewTextoEntrenamiento2_2)).getText().toString();
                if (isNumeric(porcentaje2)) {
                    // Si el porcentaje es un número, ejecutar la acción deseada aquí
                    // Inicia una transacción de fragmentos
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Reemplaza el fragmento actual con NuevoFragmento
                    fragment_dia_rutina fragmentoRutina = new fragment_dia_rutina();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentoRutina);

                    // Opcional: puedes agregar la transacción a la pila para permitir el retroceso
                    fragmentTransaction.addToBackStack(null);

                    // Completa la transacción
                    fragmentTransaction.commit();
                }
            }
        });

        cardViewEntrenamiento3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String porcentaje3 = ((TextView) cardViewEntrenamiento3.findViewById(R.id.cardViewTextoEntrenamiento3_2)).getText().toString();
                if (isNumeric(porcentaje3)) {
                    // Navegación al fragmento específico
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Aquí obtienes el elemento de la lista que se ha clicado
                    int ejercicioId = v.getId();

                    // Utiliza el método newInstance para crear una nueva instancia de fragment_home_home_segundario con el ID de la rutina
                    fragment_home_home_segundario fragment = fragment_home_home_segundario.newInstance(ejercicioId);

                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });


        return rootView;
    }
    // Método para verificar si una cadena es numérica
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        // Eliminar el último carácter si existe
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}