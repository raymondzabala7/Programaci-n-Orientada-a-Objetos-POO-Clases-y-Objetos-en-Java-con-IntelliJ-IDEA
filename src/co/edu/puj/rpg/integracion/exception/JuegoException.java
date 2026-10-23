package co.edu.puj.rpg.integracion.exception;

/**
 * Clase base abstracta de las excepciones verificadas del dominio.
 * Todas las excepciones de reglas de negocio del juego heredan de aquí.
 */
public abstract class JuegoException extends Exception {

    protected JuegoException(String mensaje) {
        super(mensaje);
    }

    /** Conserva la causa raíz para no perder el rastro de la pila. */
    protected JuegoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
