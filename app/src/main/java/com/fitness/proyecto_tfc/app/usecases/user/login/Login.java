package com.fitness.proyecto_tfc.app.usecases.user.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.usecases.home.Home;
import com.fitness.proyecto_tfc.app.usecases.user.register.Register;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void signIn(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }
    public void login(View view){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
    }
}