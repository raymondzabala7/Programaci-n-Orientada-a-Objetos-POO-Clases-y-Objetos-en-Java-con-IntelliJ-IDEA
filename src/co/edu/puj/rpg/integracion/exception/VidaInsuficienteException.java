package co.edu.puj.rpg.integracion.exception;

/** No verificada: una acción de costo supera los puntos de vida actuales. */
public class VidaInsuficienteException extends RuntimeException {

    public VidaInsuficienteException(String mensaje) {
        super(mensaje);
    }

    public VidaInsuficienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
