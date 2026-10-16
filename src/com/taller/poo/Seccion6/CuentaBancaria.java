package com.taller.poo.Seccion6;

public class CuentaBancaria {
    private String numeroCuenta;
    private double saldo;

    // Declaración errónea del constructor
    public void CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }
}

/*
CASO DE ESTUDIO 3: Firma incorrecta en la declaración de un constructor (CuentaBancaria)

1. ¿Por qué "void" altera la naturaleza del método?
   Un constructor se distingue porque tiene el mismo nombre de la clase y NO declara tipo de
   retorno. Al escribir "void", Java interpreta la declaración como un método de instancia
   común que casualmente se llama igual que la clase (es válido, aunque es mala práctica).
   Por eso deja de ser un constructor y no se ejecuta al usar "new".

2. ¿Por qué no se encuentra un constructor adecuado en
   new CuentaBancaria("A-101", 500.0);?
   Como el supuesto constructor en realidad es un método, la clase no tiene ningún constructor
   declarado explícitamente y Java le agrega solo el constructor por defecto, sin parámetros.
   Al invocar new con (String, double), no existe un constructor con esa firma y el compilador
   reporta:
   constructor CuentaBancaria in class CuentaBancaria cannot be applied to given types;

3. Firma y declaración correcta (sin tipo de retorno):
       public CuentaBancaria(String numeroCuenta, double saldoInicial) {
           this.numeroCuenta = numeroCuenta;
           this.saldo = saldoInicial;
       }
*/