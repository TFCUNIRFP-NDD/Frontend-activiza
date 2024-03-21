package com.fitness.proyecto_tfc.app.usecases.onboarding.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.usecases.onboarding.OnboardingController;

public class Onboarding2 extends AppCompatActivity {

    private OnboardingController onboardingController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding2);
        // Inicializa OnboardingController pasando el contexto de la actividad actual
        onboardingController = new OnboardingController(this);
    }
    // Este método debe ser llamado cuando el ImageView es clickeado
    public void imageViewClicked1(View view) {
        onboardingController.imageViewClicked1();
        overridePendingTransition(R.anim.deslizar_izquierda_inversa, R.anim.deslizar_derecha_inversa);
    }
    public void imageViewClicked2(View view) {
        onboardingController.imageViewClicked2();
        overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
    }
    public void imageViewClicked3(View view) {
        onboardingController.imageViewClicked3();
        overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
    }
    public void imageViewClicked4(View view) {
        onboardingController.imageViewClicked4();
        overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
    }
    public void buttonNext(View view){
        onboardingController.imageViewClicked3();
        overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
    }
    public void buttonPrevious(View view){
        onboardingController.imageViewClicked1();
        overridePendingTransition(R.anim.deslizar_izquierda_inversa, R.anim.deslizar_derecha_inversa);
    }
    public void buttonSkip(View view){
        onboardingController.imageViewClicked4();
        overridePendingTransition(R.anim.deslizar_derecha, R.anim.deslizar_izquierda);
    }
}