package com.pharmasoft.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Producto {
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private int cantidad;
    private LocalDate fechaVencimiento;

    public Producto() {
    }

    public Producto(int id, String codigo, String nombre, String categoria,
                    BigDecimal precio, int cantidad, LocalDate fechaVencimiento) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Producto(String codigo, String nombre, String categoria,
                    BigDecimal precio, int cantidad, LocalDate fechaVencimiento) {
        this(0, codigo, nombre, categoria, precio, cantidad, fechaVencimiento);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " - Cantidad: " + cantidad;
    }
}
