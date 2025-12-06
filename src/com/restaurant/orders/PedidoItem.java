/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public class PedidoItem {
    private Platillo platillo;
    private int cantidad;

    public PedidoItem(Platillo platillo, int cantidad) {
        this.platillo = platillo;
        this.cantidad = cantidad;
    }

    public Platillo getPlatillo() {
        return platillo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return platillo.getPrecio() * cantidad;
    }

    public String getDescripcionCompleta() {
        return cantidad + " x " + platillo.getDescripcion() + " = " + String.format("$%.2f", getSubtotal());
    }
}

