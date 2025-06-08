package com.example.jeaauth.dto;

import javax.persistence.Column;

public class AccesoDto {
    private String nombre;
    private String url;
    private String icono;

    public AccesoDto() {
    }

    public AccesoDto(String nombre, String url, String icono) {
        this.nombre = nombre;
        this.url = url;
        this.icono = icono;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
