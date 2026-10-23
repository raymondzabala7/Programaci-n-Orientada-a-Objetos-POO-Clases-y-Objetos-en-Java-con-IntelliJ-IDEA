package co.edu.puj.rpg.integracion.exception;

/** Verificada: se intenta lanzar un hechizo sin el maná requerido. */
public class SinManaException extends JuegoException {

    public SinManaException(String mensaje) {
        super(mensaje);
    }

    public SinManaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
