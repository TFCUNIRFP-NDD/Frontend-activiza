package com.fitness.proyecto_tfc.app.usecases.home.fragmentosPrincipales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.model.clases.Entrenamiento;
import com.fitness.proyecto_tfc.app.usecases.home.CustomAdapter;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragment_home_home_segundario;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
public class Home_fragment_home extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Home_fragment_home() {
        // Required empty public constructor
    }
    public static Home_fragment_home newInstance(String param1, String param2) {
        Home_fragment_home fragment = new Home_fragment_home();
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

    private ListView listView;
    private CustomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_home, container, false);
        listView = view.findViewById(R.id.home_Entrenamientos);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Entrenamiento> dataList = getDataFromDatabase(); // Obtener datos de la base de datos
        adapter = new CustomAdapter(dataList, requireContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Navegación al fragmento específico
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Aquí obtienes el elemento de la lista que se ha clicado
                int rutinaId = dataList.get(position).getRutina_id();

                // Utiliza el método newInstance para crear una nueva instancia de fragment_home_home_segundario con el ID de la rutina
                fragment_home_home_segundario fragment = fragment_home_home_segundario.newInstance(rutinaId);

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }

    private ArrayList<Entrenamiento> getDataFromDatabase() {
        // Aquí se supone que obtienes los datos de la base de datos y los conviertes en una lista de objetos DataModel
        // Por simplicidad, aquí simplemente se crea una lista de ejemplo
        ArrayList<Entrenamiento> dataList = new ArrayList<>();
        Entrenamiento entreno = new Entrenamiento();
        entreno.setImagenId(R.drawable.background_gimnasio);
        entreno.setNombre("Entrenamiento de musculación");
        entreno.setRutina_id(1);
        dataList.add(entreno);
        Entrenamiento entreno1 = new Entrenamiento();
        entreno1.setRutina_id(2);
        entreno1.setImagenId(R.drawable.plantillaonboarding);
        entreno1.setNombre("Entrenamiento de calistenia");
        dataList.add(entreno1);
        // Agregar más datos según lo necesario
        return dataList;
    }

}