package com.fitness.proyecto_tfc.app.model.clases;

import java.util.ArrayList;
import java.util.List;

public class Ejercicio{
        private int id;//Ej: 1
        private String nombre;//Ej: Cinta de correr
        private int imagen;//Ej: una chica caminando
        private int repeticiones;//Ej: 0
        private int series;//Ej: 0
        private double minutos;//Ej: 8
        private double descanso;//Ej: 30segundos
        private boolean compledado;//Ej: true

    public Ejercicio(int id, String nombre, int imagen, int repeticiones, int series, double minutos, double descanso, boolean compledado) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.repeticiones = repeticiones;
        this.series = series;
        this.minutos = minutos;
        this.descanso = descanso;
        this.compledado = compledado;
    }
    public Ejercicio(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public double getMinutos() {
        return minutos;
    }

    public void setMinutos(double minutos) {
        this.minutos = minutos;
    }

    public double getDescanso() {
        return descanso;
    }

    public void setDescanso(double descanso) {
        this.descanso = descanso;
    }

    public boolean isCompledado() {
        return compledado;
    }

    public void setCompledado(boolean compledado) {
        this.compledado = compledado;
    }
}
