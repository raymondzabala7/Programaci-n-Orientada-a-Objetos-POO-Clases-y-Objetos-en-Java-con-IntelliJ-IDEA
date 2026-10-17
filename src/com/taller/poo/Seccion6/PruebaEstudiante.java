package com.taller.poo.Seccion6;

import com.taller.poo.Seccion1.Estudiante;

public class PruebaEstudiante {
    public static void main(String[] args) {
        Estudiante estudiante1=null;

        // Intento de invocación sobre una variable no instanciada
        String reporte = estudiante1.obtenerReporteAcademicos();
        System.out.println(reporte);
    }
}

/*
CASO DE ESTUDIO 1: NullPointerException (PruebaEstudiante)

1. ¿Qué error indica el compilador de Java o qué excepción se lanza en tiempo de ejecución?
   El compilador de Java no deja compilar el código y marca:
   "variable estudiante1 might not have been initialized"
   Esto ocurre porque las variables locales no reciben un valor por defecto.
   Si la variable se inicializara explícitamente con null (Estudiante estudiante1 = null;),
   el código sí compilaría, pero en tiempo de ejecución se lanzaría:
   java.lang.NullPointerException

2. ¿Por qué estudiante1 no puede invocar el metodo de instancia?
   Porque "Estudiante estudiante1;" solo declara una variable de referencia; no crea ningún
   objeto en memoria. Un metodo de instancia necesita un objeto real que tenga su propio
   estado (identificacion, nombreCompleto, promedioCalificaciones, estaMatriculado) sobre el
   cual ejecutarse. Sin objeto, la referencia no apunta a nada y no hay sobre qué ejecutar el metodo.

3. Instrucción para instanciar correctamente el objeto:
   estudiante1 = new Estudiante("E001", "Laura Martínez", 4.35, true);

   (También se puede declarar e instanciar en una sola línea:
   Estudiante estudiante1 = new Estudiante("E001", "Laura Martínez", 4.35, true);)
*/