/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

public class Pizza implements Platillo {
    @Override
    public double getPrecio() {
        return 12.99;
    }

    @Override
    public String getDescripcion() {
        return "Pizza";
    }
}

