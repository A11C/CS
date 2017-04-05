package com.fiuady.db;

public class Assembly {
    private int id;
    private String descripcion;

    public Assembly(int id, String descripcion){
        this.id = id;
        this.descripcion=descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}