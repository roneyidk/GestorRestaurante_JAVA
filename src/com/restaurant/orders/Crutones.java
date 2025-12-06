/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public class Crutones extends ExtraDecorator {
    public Crutones(Platillo platillo) {
        super(platillo);
    }

    @Override
    public double getPrecio() {
        return platillo.getPrecio() + 1.20;
    }

    @Override
    public String getDescripcion() {
        return platillo.getDescripcion() + " + Crutones";
    }
}

