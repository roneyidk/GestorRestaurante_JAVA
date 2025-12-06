/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

import java.util.ArrayList;
import java.util.List;

public class GestorPedidos {
    private List<Pedido> pedidos = new ArrayList<>();

    public void agregarPedido(Pedido p) {
        pedidos.add(p);
    }

    public List<Pedido> obtenerPedidos() {
        return pedidos;
    }

    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}
