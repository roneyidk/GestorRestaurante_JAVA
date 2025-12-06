/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public class Queso extends ExtraDecorator {
    public Queso(Platillo platillo) {
        super(platillo);
    }

    @Override
    public double getPrecio() {
        return platillo.getPrecio() + 1.50;
    }

    @Override
    public String getDescripcion() {
        return platillo.getDescripcion() + " + Queso";
    }
}

