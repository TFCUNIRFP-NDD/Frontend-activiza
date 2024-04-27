package com.fitness.proyecto_tfc.app.usecases.user.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.usecases.home.Home;
import com.fitness.proyecto_tfc.app.usecases.user.login.Login;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }
    public void signUp(View view){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }
    public void register(View view){
        Intent intent = new Intent(Register.this, Home.class);
        startActivity(intent);
    }
}