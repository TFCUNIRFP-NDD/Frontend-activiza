package com.fitness.proyecto_tfc.app.usecases.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fitness.proyecto_tfc.app.usecases.home.fragmentosPrincipales.Home_fragment_home;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosPrincipales.Home_fragment_reel;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosPrincipales.Home_fragment_settings;
import com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_home_con_entrenamiento.fragment_home_training;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.fitness.proyecto_tfc.R;


public class Home extends AppCompatActivity {
    HomeVerificacion verificacion = new HomeVerificacion();
    boolean tieneentrenamiento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Aquí asignamos el valor correcto a tieneentrenamiento
        tieneentrenamiento = HomeVerificacion.tieneentrenamiento;

        //LLamamos al principio de crear el fragmento al fragmento 1
        cambiarFragmento(1);

        BottomNavigationView navView = findViewById(R.id.nav_view_home);
        navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    cambiarFragmento(1);
                    return true;
                case R.id.action_manual:
                    cambiarFragmento(2);
                    return true;
                case R.id.action_mas:
                    cambiarFragmento(3);
                    return true;
                default:
                    return false;
            }
        });

    }
    private void cambiarFragmento(int opcion) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment;

        switch (opcion) {
            case 1:
                if(tieneentrenamiento){
                    fragment = new fragment_home_training();
                }else {
                    fragment = new Home_fragment_home();
                }
                break;
            case 2:
                fragment = new Home_fragment_reel();
                break;
            case 3:
                fragment = new Home_fragment_settings();
                break;
            default:
                // Fragmento predeterminado, si no se selecciona ninguna opción válida
                if(tieneentrenamiento){
                    fragment = new fragment_home_training();
                }else {
                    fragment = new Home_fragment_home();
                }
                break;
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}