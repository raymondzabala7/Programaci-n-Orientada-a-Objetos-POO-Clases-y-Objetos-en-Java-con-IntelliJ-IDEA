package com.taller.poo.Seccion6;

public class Vehiculo {
    private int velocidadMaxima;
    private boolean encendido;

    public Vehiculo(int velocidadMaxima, boolean encendido) {
        // Asignación de variables omitiendo la referencia 'this'
        velocidadMaxima = velocidadMaxima;
        encendido = encendido;
    }

    public void imprimirEstado() {
        System.out.printf("Velocidad Máxima: %d | Encendido: %b%n", velocidadMaxima, encendido);
    }
}

/*
CASO DE ESTUDIO 4: Sombreado de variables por ausencia de this (Vehiculo)

1. ¿Qué valores se proyectarán en la consola y por qué?
   Se imprime:
   Velocidad Máxima: 0 | Encendido: false
   Dentro del constructor, los nombres velocidadMaxima y encendido hacen referencia a los
   parámetros, no a los atributos. Las líneas "velocidadMaxima = velocidadMaxima;" y
   "encendido = encendido;" se asignan el parámetro a sí mismo, y los atributos de la
   instancia nunca se modifican. Quedan con sus valores por defecto: 0 para int y false
   para boolean. imprimirEstado() lee los atributos, por eso muestra esos valores y no 200 ni true.

2. Fenómeno de sombreado de variables (variable shadowing):
   Ocurre cuando una variable local o un parámetro tiene el mismo nombre que un atributo de
   la clase. Dentro de ese alcance, el nombre se refiere a la variable más cercana (el
   parámetro) y "oculta" o "sombrea" al atributo. Cuando el nombre del parámetro coincide
   con el del atributo y no se usa this, la asignación se hace entre el parámetro y él
   mismo, así que el estado del objeto no se actualiza. El compilador no marca error, por lo
   que es un fallo lógico silencioso. La solución es usar this para indicar explícitamente
   que se habla del atributo de la instancia.

3. Constructor corregido con this:
       public Vehiculo(int velocidadMaxima, boolean encendido) {
           this.velocidadMaxima = velocidadMaxima;
           this.encendido = encendido;
       }
*/