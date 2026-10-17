package com.taller.poo.Seccion5;

import com.taller.poo.Seccion1.CuentaBancaria;
import com.taller.poo.Seccion2.ConversorMoneda;

import java.util.Locale;
import java.util.Scanner;

public class SistemaGestionCuentas {

    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numeroCuenta) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return cuentas[i];
            }
        }
        return null;
    }

    private static int leerEntero(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double leerMonto(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Luis Torres", 300.00, 200.0);

        int opcion;
        do {
            System.out.printf("%n=== SISTEMA DE GESTIÓN DE CUENTAS ===%n");
            System.out.printf("1. Depositar%n");
            System.out.printf("2. Retirar%n");
            System.out.printf("3. Ver reporte consolidado%n");
            System.out.printf("4. Salir%n");
            System.out.printf("Seleccione una opción: ");
            opcion = leerEntero(scanner);

            switch (opcion) {
                case 1:
                    System.out.printf("Número de cuenta: ");
                    String numDeposito = scanner.nextLine().trim();
                    CuentaBancaria cuentaDeposito = buscarCuenta(cuentas, numDeposito);
                    if (cuentaDeposito == null) {
                        System.out.printf("Error: la cuenta %s no existe.%n", numDeposito);
                    } else {
                        System.out.printf("Monto a depositar: ");
                        double montoDeposito = leerMonto(scanner);
                        if (montoDeposito <= 0) {
                            System.out.printf("Error: el monto debe ser mayor a cero.%n");
                        } else if (cuentaDeposito.depositar(montoDeposito)) {
                            System.out.printf("Depósito exitoso. Nuevo saldo: %s%n",
                                    ConversorMoneda.formatearMoneda(cuentaDeposito.getSaldo()));
                        } else {
                            System.out.printf("No se pudo realizar el depósito.%n");
                        }
                    }
                    break;
                case 2:
                    System.out.printf("Número de cuenta: ");
                    String numRetiro = scanner.nextLine().trim();
                    CuentaBancaria cuentaRetiro = buscarCuenta(cuentas, numRetiro);
                    if (cuentaRetiro == null) {
                        System.out.printf("Error: la cuenta %s no existe.%n", numRetiro);
                    } else {
                        System.out.printf("Monto a retirar: ");
                        double montoRetiro = leerMonto(scanner);
                        if (montoRetiro <= 0) {
                            System.out.printf("Error: el monto debe ser mayor a cero.%n");
                        } else if (cuentaRetiro.retirar(montoRetiro)) {
                            System.out.printf("Retiro exitoso. Nuevo saldo: %s%n",
                                    ConversorMoneda.formatearMoneda(cuentaRetiro.getSaldo()));
                        } else {
                            System.out.printf("Fondos insuficientes para realizar el retiro.%n");
                        }
                    }
                    break;
                case 3:
                    StringBuilder detalle = new StringBuilder();
                    double total = 0;
                    for (int i = 0; i < cuentas.length; i++) {
                        CuentaBancaria c = cuentas[i];
                        detalle.append(String.format("Cuenta: %s | Titular: %s | Saldo: %s | Crédito: %s%n",
                                c.getNumeroCuenta(), c.getTitular(),
                                ConversorMoneda.formatearMoneda(c.getSaldo()),
                                ConversorMoneda.formatearMoneda(c.getLimiteCredito())));
                        total += c.getSaldo();
                    }
                    System.out.printf("%n=== REPORTE CONSOLIDADO ===%n");
                    System.out.printf("%s", detalle);
                    System.out.printf("Saldo total acumulado: %s%n", ConversorMoneda.formatearMoneda(total));
                    System.out.printf("Cuentas registradas: %d%n", cuentas.length);
                    break;
                case 4:
                    System.out.printf("Saliendo del sistema...%n");
                    break;
                default:
                    System.out.printf("Opción inválida. Intente de nuevo.%n");
            }
        } while (opcion != 4);

        scanner.close();
    }
}