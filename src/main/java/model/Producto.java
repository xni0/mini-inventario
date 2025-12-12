package model;

import java.math.BigDecimal;

public class Producto {
    private Integer id;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private int stock;
    private boolean agotado;

    public Producto() {
    }

    public Producto(Integer id, String nombre, String categoria, BigDecimal precio, int stock, boolean agotado) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.agotado = agotado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : null;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria != null ? categoria.trim().toLowerCase() : null;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isAgotado() {
        return agotado;
    }

    public void setAgotado(boolean agotado) {
        this.agotado = agotado;
    }
    
    
}
