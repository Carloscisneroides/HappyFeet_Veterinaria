package com.happyfeet.view;

import javax.swing.JOptionPane;

public class MascotaView {

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Happy Feet Veterinaria - Mascotas",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error - Happy Feet Veterinaria",
                JOptionPane.ERROR_MESSAGE);
    }

    public boolean mostrarConfirmacion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje,
                "Confirmación - Happy Feet Veterinaria",
                JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public void mostrarMascotaDetalles(String detalles) {
        JOptionPane.showMessageDialog(null, detalles, "Detalles de Mascota",
                JOptionPane.INFORMATION_MESSAGE);
    }
}