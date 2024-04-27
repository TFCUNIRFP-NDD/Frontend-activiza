package com.fitness.proyecto_tfc.app.usecases.home;

import android.content.Context;
import android.content.Intent;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fitness.proyecto_tfc.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ControladorHome implements BottomNavigationView.OnItemSelectedListener{
    private Context context;

    public ControladorHome (Context context) {
        this.context = context;
    }

    //Navega entre actividades
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        // Handle navigation item clicks
        if (id == R.id.action_home) {
            // Start HomeActivity
            startActivity(Home.class);
            return true;
        }

        return false;
    }

    //Este método recoge las calses de la actividad del contexto y las inicializa
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }


}
