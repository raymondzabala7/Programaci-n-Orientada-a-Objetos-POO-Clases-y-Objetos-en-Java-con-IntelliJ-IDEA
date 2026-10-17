package com.taller.poo.Seccion1;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    /*
     * PREGUNTAS DE ANÁLISIS:
     * 1. ¿Qué diferencia ocurre a nivel de memoria y asignación por defecto al
     *    declarar 'saldo' como double primitivo frente a 'limiteCredito' como Wrapper Double?
     *
     *    RESPUESTA:
     *    'saldo' (double) guarda el valor numérico directamente dentro del objeto
     *    CuentaBancaria: ocupa 8 bytes fijos y, si no se inicializa, la JVM le asigna
     *    por defecto 0.0.
     *    'limiteCredito' (Double) no guarda el número, sino una referencia a un objeto
     *    Double aparte en el heap. Esto implica más memoria (la referencia más el
     *    objeto Double con su encabezado, aproximadamente 16 bytes o más según la JVM)
     *    y un acceso indirecto. Si no se inicializa, su valor por defecto es null,
     *    es decir, todavía no existe ningún objeto Double asociado.
     *
     * 2. ¿Por qué una operación aritmética sobre un atributo no inicializado de
     *    tipo Double provoca una NullPointerException, mientras que sobre un double
     *    primitivo opera con 0.0?
     *
     *    RESPUESTA:
     *    Para operar con un Double, Java hace unboxing automático: internamente llama
     *    a limiteCredito.doubleValue(). Si la referencia es null, no hay ningún objeto
     *    sobre el cual invocar ese método, y se lanza NullPointerException.
     *    Un double primitivo no es un objeto ni puede ser null: siempre contiene un
     *    valor, y como atributo de clase arranca en 0.0, por lo que la operación
     *    aritmética se ejecuta con normalidad.
     */

    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.titular = "Sin Nombre";
        this.saldo = 0.0;
        this.limiteCredito = 0.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;
        this.limiteCredito = 100.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        double disponible = this.saldo;
        if (this.limiteCredito != null) {
            disponible += this.limiteCredito;
        }
        if (monto > 0 && disponible >= monto) {
            this.saldo -= monto;
            return true;
        } else {
            return false;
        }
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }
}