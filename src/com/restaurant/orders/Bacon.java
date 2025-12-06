/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public class Bacon extends ExtraDecorator {
    public Bacon(Platillo platillo) {
        super(platillo);
    }

    @Override
    public double getPrecio() {
        return platillo.getPrecio() + 2.00;
    }

    @Override
    public String getDescripcion() {
        return platillo.getDescripcion() + " + Bacon";
    }
}

