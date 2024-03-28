package com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_dia_entrenamiento;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.model.clases.Ejercicio;
import com.fitness.proyecto_tfc.app.model.clases.Entrenamiento;
import com.fitness.proyecto_tfc.app.usecases.home.CustomAdapter;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragment_home_home_segundario;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_dia_rutina#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_dia_rutina extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_dia_rutina() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_dia_rutina.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_dia_rutina newInstance(String param1, String param2) {
        fragment_dia_rutina fragment = new fragment_dia_rutina();
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
    private CustomAdapterRutina adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dia_rutina, container, false);
        listView = view.findViewById(R.id.listViewEjercicios);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Ejercicio> dataList = getDataFromDatabase(); // Obtener datos de la base de datos
        adapter = new CustomAdapterRutina(dataList, requireContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Navegación al fragmento específico
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Aquí obtienes el elemento de la lista que se ha clicado
                int ejercicioId = dataList.get(position).getId();

                // Utiliza el método newInstance para crear una nueva instancia de fragment_home_home_segundario con el ID de la rutina
                fragment_home_home_segundario fragment = fragment_home_home_segundario.newInstance(ejercicioId);

                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }

    private ArrayList<Ejercicio> getDataFromDatabase() {
        // Aquí se supone que obtienes los datos de la base de datos y los conviertes en una lista de objetos DataModel
        // Por simplicidad, aquí simplemente se crea una lista de ejemplo
        ArrayList<Ejercicio> dataList = new ArrayList<>();
        // Agrega los datos que quieras mostrar en los CardViews de la BBDD
        Ejercicio e = new Ejercicio();
        e.setId(1);
        e.setNombre("Cinta de correr");
        e.setImagen(R.drawable.entrenador);
        dataList.add(e);
        Ejercicio e2 = new Ejercicio();
        e2.setId(2);
        e2.setNombre("muscle up");
        e2.setImagen(R.drawable.face_ico);
        dataList.add(e2);
        Ejercicio e3 = new Ejercicio();
        e3.setId(3);
        e3.setNombre("dominadas");
        e3.setImagen(R.drawable.face_ico);
        dataList.add(e3);

        // Agregar más datos según lo necesario
        return dataList;
    }
}