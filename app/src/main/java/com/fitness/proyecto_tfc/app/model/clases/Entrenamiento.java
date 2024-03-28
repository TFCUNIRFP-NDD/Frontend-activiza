package com.fitness.proyecto_tfc.app.model.clases;

import java.util.List;

public class Entrenamiento {
    private int rutina_id;
    private String nombre;
    private String descripcion;
    private int duracion;
    private List<String> tipo;
    private int entrenador_id;
    private int gimnasio_id;
    private int imagenId;
    private List<Ejercicio> ejercicios;

    public Entrenamiento(int rutina_id, String nombre, String descripcion, int duracion, List<String> tipo, int entrenador_id, int gimnasio_id, int imagenId) {
        this.rutina_id = rutina_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.tipo = tipo;
        this.entrenador_id = entrenador_id;
        this.gimnasio_id = gimnasio_id;
        this.imagenId = imagenId;
    }

    public Entrenamiento() {
    }

    // Getters y Setters
    public int getImagenId() {
        return imagenId;
    }
    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }
    public int getRutina_id() {
        return rutina_id;
    }

    public void setRutina_id(int rutina_id) {
        this.rutina_id = rutina_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public List<String> getTipo() {
        return tipo;
    }

    public void setTipo(List<String> tipo) {
        this.tipo = tipo;
    }

    public int getEntrenador_id() {
        return entrenador_id;
    }

    public void setEntrenador_id(int entrenador_id) {
        this.entrenador_id = entrenador_id;
    }

    public int getGimnasio_id() {
        return gimnasio_id;
    }

    public void setGimnasio_id(int gimnasio_id) {
        this.gimnasio_id = gimnasio_id;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
