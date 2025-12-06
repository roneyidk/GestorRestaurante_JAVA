/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.orders;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class EstadoRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Object estadoObj = table.getValueAt(row, 3); // columna Estado (índice 3)
        String estado = estadoObj != null ? estadoObj.toString() : "";

        // Reset default background depending on selection
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
            return c;
        } else {
            c.setForeground(Color.BLACK);
        }

        switch (estado) {
            case "CANCELADO" -> c.setBackground(new Color(255, 150, 150));
            case "ENTREGADO" -> c.setBackground(new Color(180, 255, 180));
            case "EN_PREPARACION" -> c.setBackground(new Color(255, 255, 150));
            case "ENVIADO" -> c.setBackground(new Color(180, 240, 255));
            case "LISTO" -> c.setBackground(new Color(255, 230, 180));
            default -> c.setBackground(Color.WHITE);
        }
        return c;
    }
}

