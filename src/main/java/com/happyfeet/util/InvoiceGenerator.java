package com.happyfeet.util;

import com.happyfeet.model.entities.Factura;
import com.happyfeet.model.entities.ItemFactura;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceGenerator {
    public static String generatePlainInvoice(Factura f, List<ItemFactura> items, String outputPath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.println("Happy Feet - Clínica Veterinaria");
            pw.println("Fecha: " + LocalDateTime.now());
            pw.println("----------------------------------------");
            pw.println("Factura ID: " + f.getId());
            pw.println("Cliente ID: " + f.getDuenoId());
            pw.println("----------------------------------------");
            double subtotal = 0.0;
            for (ItemFactura it : items) {
                double precio = it.getPrecioUnitario()!=null? it.getPrecioUnitario().doubleValue():0.0;
                double cant = it.getCantidad()!=null? it.getCantidad().doubleValue():1.0;
                double total = precio * cant;
                subtotal += total;
                pw.printf("%s x%.2f  %.2f%n", it.getDescripcion(), cant, total);
            }
            pw.println("----------------------------------------");
            pw.printf("Subtotal: %.2f%n", subtotal);
            double impuestos = subtotal * 0.12;
            pw.printf("Impuestos (12%%): %.2f%n", impuestos);
            pw.printf("Total: %.2f%n", subtotal + impuestos);
            return outputPath;
        } catch (Exception e) {
            AppLogger.error("Error generating invoice: " + e.getMessage());
            return null;
        }
    }
}
