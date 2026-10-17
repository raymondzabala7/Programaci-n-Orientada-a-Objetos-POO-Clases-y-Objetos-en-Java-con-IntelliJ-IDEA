package com.taller.poo.Seccion2;

public class ConversorMoneda {

    private ConversorMoneda() {}

    public static double convertirDolaresAEuros(double montoDolares, double tasaCambio) {
        if (montoDolares <= 0 || tasaCambio <= 0) {
            return 0.0;
        }
        return montoDolares * tasaCambio;
    }

    public static String formatearMoneda(double monto) {
        return String.format("$%.2f", monto);
    }
}