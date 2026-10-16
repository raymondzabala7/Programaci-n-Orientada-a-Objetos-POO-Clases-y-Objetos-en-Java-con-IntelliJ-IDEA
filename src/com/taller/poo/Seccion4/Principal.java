package com.taller.poo.Seccion4;

import com.taller.poo.Seccion1.CuentaBancaria;
import com.taller.poo.Seccion1.Estudiante;
import com.taller.poo.Seccion1.Vehiculo;
import com.taller.poo.Seccion2.ConversorMoneda;

import java.util.Locale;

public class Principal {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Vehiculo[] vehiculos = {
                new Vehiculo(),
                new Vehiculo("Toyota", "Corolla", 15000.50, 180, true),
                new Vehiculo("Renault", "Logan", 32000.00, 170, false)
        };

        Estudiante[] estudiantes = {
                new Estudiante("E001", "Laura Martínez", 4.35, true),
                new Estudiante("E002", "Juan Rodríguez", 3.80, true),
                new Estudiante("E003", "Sofía Herrera", 4.70, false)
        };

        CuentaBancaria cuenta1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.0);
        CuentaBancaria cuenta2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria cuenta3 = new CuentaBancaria();

        boolean dep1 = cuenta1.depositar(250.75);
        boolean dep2 = cuenta2.depositar(50.00);
        boolean dep3 = cuenta3.depositar(-10.00);
        boolean ret1 = cuenta1.retirar(0.00);
        boolean ret2 = cuenta2.retirar(500.00);
        boolean ret3 = cuenta3.retirar(0.01);

        System.out.printf("=== OPERACIONES BANCARIAS ===%n");
        System.out.printf("Depósito cuenta 1001: %b | Retiro cuenta 1001: %b%n", dep1, ret1);
        System.out.printf("Depósito cuenta 1002: %b | Retiro cuenta 1002: %b%n", dep2, ret2);
        System.out.printf("Depósito cuenta 000000: %b | Retiro cuenta 000000: %b%n%n", dep3, ret3);

        double euros = ConversorMoneda.convertirDolaresAEuros(100.0, 0.92);
        System.out.printf("=== CONVERSOR DE MONEDA ===%n");
        System.out.printf("100 USD = %s EUR%n%n", ConversorMoneda.formatearMoneda(euros));

        System.out.printf("=== REPORTE DE VEHÍCULOS ===%n");
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo v = vehiculos[i];
            System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                    i + 1, v.getMarca(), v.getModelo(), v.getKilometraje(), v.getVelocidadMaxima(), v.isEncendido());
        }

        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        for (Estudiante e : estudiantes) {
            System.out.printf("%s%n", e.obtenerReporteAcademicos());
        }

        CuentaBancaria[] cuentas = {cuenta1, cuenta2, cuenta3};
        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        for (CuentaBancaria c : cuentas) {
            System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: %s | Crédito: %s%n",
                    c.getNumeroCuenta(), c.getTitular(),
                    ConversorMoneda.formatearMoneda(c.getSaldo()),
                    ConversorMoneda.formatearMoneda(c.getLimiteCredito()));
        }
    }
}