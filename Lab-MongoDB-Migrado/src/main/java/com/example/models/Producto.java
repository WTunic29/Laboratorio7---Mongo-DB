package com.example.models;

import java.util.Date;

/**
 * Producto almacenado en la colección productos.
 */
public class Producto {

    private String id;
    private String nombre;
    private String marca;
    private Date fechaCompra;

    public Producto() {
    }

    public Producto(String nombre, String marca, Date fechaCompra) {
        this.nombre = nombre;
        this.marca = marca;
        this.fechaCompra = fechaCompra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}
