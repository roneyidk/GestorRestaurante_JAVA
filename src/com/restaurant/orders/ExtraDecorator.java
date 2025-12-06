/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public abstract class ExtraDecorator implements Platillo {

    protected Platillo platillo;  
    protected String nombreExtra;
    protected double precioExtra;

    public ExtraDecorator(Platillo platillo) {
        this.platillo = platillo;
    }

    @Override
    public double getPrecio() {
        return platillo.getPrecio() + precioExtra;
    }

    @Override
    public String getDescripcion() {
        return platillo.getDescripcion() + " + " + nombreExtra;
    }

    public String getKey() {
        return nombreExtra;  // ✔ Aquí NO dará error
    }
}



