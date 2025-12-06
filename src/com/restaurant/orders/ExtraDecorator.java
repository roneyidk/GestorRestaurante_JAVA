/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public abstract class ExtraDecorator implements Platillo {
    protected Platillo platillo;

    public ExtraDecorator(Platillo platillo) {
        this.platillo = platillo;
    }

    @Override
    public abstract double getPrecio();

    @Override
    public abstract String getDescripcion();
}

