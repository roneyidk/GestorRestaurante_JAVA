/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int contador = 1;
    private int id;
    private String cliente;
    private List<PedidoItem> items;
    private EstadoPedido estado;

    public Pedido(String cliente) {
        this.id = contador++;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public List<PedidoItem> getItems() { return items; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public void agregarItem(PedidoItem item) {
        items.add(item);
    }

    public double getTotal() {
        double suma = 0;
        for (PedidoItem it : items) suma += it.getSubtotal();
        return suma;
    }
}
