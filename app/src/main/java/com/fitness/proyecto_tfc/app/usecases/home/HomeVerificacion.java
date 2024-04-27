package com.fitness.proyecto_tfc.app.usecases.home;

public class HomeVerificacion {
    public static boolean tieneentrenamiento;
    public HomeVerificacion(){
        if(recogerEntrenamiento()){
            tieneentrenamiento = true;
        }else{
            tieneentrenamiento = false;
        }
    }
    //Este método recogera si existe entrenamiento de la base de datos y devolvera true a tieneentrenamiento si existe
    //y si no existe devolvera false a tieneentrenamiento
    public boolean recogerEntrenamiento(){
        //probamos si recoge datos que nos entre a la bbdd
        //Esto se tiene que cambiar por llamadas a la bbdd
        //Si es positivo muestra el entrenamiento sino la lista para que lo escojas
        boolean prueba = true;
        if(prueba){
            return true;
        }else{
            return false;
        }
    }
}
