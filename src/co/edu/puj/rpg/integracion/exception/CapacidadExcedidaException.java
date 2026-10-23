package co.edu.puj.rpg.integracion.exception;

/** Verificada: se intenta agregar un elemento a un contenedor ya lleno. */
public class CapacidadExcedidaException extends JuegoException {

    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }

    public CapacidadExcedidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
