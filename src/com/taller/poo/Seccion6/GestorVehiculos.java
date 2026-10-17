package com.taller.poo.Seccion6;

public class GestorVehiculos {

    public void encenderMotor() {
        System.out.println("El motor del vehículo ha sido encendido.");
    }

    public static void main(String[] args) {
        // Intento de llamada directa a método de instancia desde el método estático main
        encenderMotor();
    }
}

/*
CASO DE ESTUDIO 2: Contexto de instancia desde contexto estático (GestorVehiculos)

1. ¿Cuál es el mensaje de error emitido por el compilador en la línea encenderMotor()?
   non-static method encenderMotor() cannot be referenced from a static context

2. ¿Por qué un método static no puede invocar un método de instancia?
   Un método static pertenece a la clase y se puede ejecutar sin que exista ningún objeto
   (main se ejecuta antes de que se cree cualquier instancia). Un método de instancia, en
   cambio, trabaja sobre un objeto concreto: usa implícitamente la referencia "this" y puede
   leer o modificar los atributos de ese objeto. Dentro de un contexto estático no existe
   "this", por lo que Java no sabe sobre qué objeto debería ejecutarse encenderMotor().

3. Dos alternativas de corrección:
   a) Instanciar un objeto y llamar al método desde él:
          public static void main(String[] args) {
              GestorVehiculos gestor = new GestorVehiculos();
              gestor.encenderMotor();
          }

   b) Declarar el método como estático:
          public static void encenderMotor() {
              System.out.println("El motor del vehículo ha sido encendido.");
          }
      De esta forma main puede llamarlo directamente con encenderMotor();
*/