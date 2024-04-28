package com.activiza.activiza.ui.viewmodel

import android.content.Context
import android.view.View

class OnboardingFunctions {
    //Esta clase se utiliza para que la barra de carga del onboarding se vaya rellenando conforme se pase
    fun pintarBarraNaranja(view: View, percentage: Int) {
        val screenWidth = view.resources.displayMetrics.widthPixels
        val lineWidth = (percentage / 100.0 * screenWidth).toInt()
        val layoutParams = view.layoutParams
        layoutParams.width = lineWidth
        view.layoutParams = layoutParams
    }
}