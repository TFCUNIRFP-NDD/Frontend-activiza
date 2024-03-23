package com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.model.clases.Entrenamiento;
import com.fitness.proyecto_tfc.app.usecases.home.CustomAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home_home_segundario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home_home_segundario extends Fragment {

    private int id;
    private TextView objetivoTextView;
    private TextView duracionTextView;
    private TextView categoriaTextView;
    private TextView  textodisenorutinaTextView;
    private ImageView imagenentrenamientofondoImage;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public fragment_home_home_segundario() {
        // Constructor vacío requerido
    }

    public static fragment_home_home_segundario newInstance(int id) {
        fragment_home_home_segundario fragment = new fragment_home_home_segundario();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_home_segundario, container, false);

        objetivoTextView = view.findViewById(R.id.objetivo);
        duracionTextView = view.findViewById(R.id.duracion);
        categoriaTextView = view.findViewById(R.id.categoria);
        textodisenorutinaTextView = view.findViewById(R.id.textoDisenoRutina);
        imagenentrenamientofondoImage = view.findViewById(R.id.imagenEntrenamientoFondo);

        Entrenamiento entrenamiento = getDataFromDatabase(id);
        Toast.makeText(requireContext(), "ID de la rutina: " + id, Toast.LENGTH_SHORT).show();
        if (entrenamiento != null) {
            objetivoTextView.setText(entrenamiento.getDescripcion());
            duracionTextView.setText(String.valueOf("La duración de la rutina es de "+entrenamiento.getDuracion()+" días"));
            String resultado = TextUtils.join(", ", entrenamiento.getTipo());
            categoriaTextView.setText("Categoría: " + resultado);
            textodisenorutinaTextView.setText((entrenamiento.getNombre()));
            imagenentrenamientofondoImage.setImageResource(entrenamiento.getImagenId());
        }

        return view;
    }

    private Entrenamiento getDataFromDatabase(int id) {
        Entrenamiento entreno = null;
        if (id == 1) {
            entreno = new Entrenamiento();
            entreno.setImagenId(R.drawable.background_gimnasio);
            entreno.setNombre("Entrenamiento de musculación");
            List<String> tipo = new ArrayList<>();
            tipo.add("gym");
            tipo.add("mancuernas");
            entreno.setTipo(tipo);
            entreno.setDescripcion("La rutina es de musculación");
            entreno.setDuracion(33);
        }
        if (id == 2) {
            entreno = new Entrenamiento();
            entreno.setImagenId(R.drawable.plantillaonboarding);
            entreno.setNombre("Entrenamiento de calistenia");
            List<String> tipo = new ArrayList<>();
            tipo.add("casita");
            tipo.add("dormir");
            entreno.setTipo(tipo);
            entreno.setDescripcion("La rutina es de calistenia");
            entreno.setDuracion(30);
        }
        if (entreno != null) {
            entreno.setRutina_id(id); // Establecer el ID después de crear la instancia del objeto Entrenamiento
        }
        return entreno;
    }

}