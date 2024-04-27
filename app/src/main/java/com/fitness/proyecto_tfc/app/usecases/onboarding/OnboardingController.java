package com.fitness.proyecto_tfc.app.usecases.onboarding;

import android.content.Context;
import android.content.Intent;

import com.fitness.proyecto_tfc.app.usecases.onboarding.page.Onboarding;
import com.fitness.proyecto_tfc.app.usecases.onboarding.page.Onboarding2;
import com.fitness.proyecto_tfc.app.usecases.onboarding.page.Onboarding3;
import com.fitness.proyecto_tfc.app.usecases.onboarding.page.Onboarding4;
import com.fitness.proyecto_tfc.app.usecases.user.login.Login;


public class OnboardingController {
    private Context context;

    public OnboardingController(Context context){
        this.context = context;
    }

    public void imageViewClicked1() {
        try {
            Intent intent = new Intent(context, Onboarding.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void imageViewClicked2() {
        try {
            Intent intent = new Intent(context, Onboarding2.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void imageViewClicked3() {
        try {
            Intent intent = new Intent(context, Onboarding3.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void imageViewClicked4() {
        try {
            Intent intent = new Intent(context, Onboarding4.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void imageViewClickedLogin() {
        try {
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
