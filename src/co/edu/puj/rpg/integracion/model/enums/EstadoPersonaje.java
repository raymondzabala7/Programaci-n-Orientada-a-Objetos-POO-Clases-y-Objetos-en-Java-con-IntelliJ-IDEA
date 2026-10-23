package co.edu.puj.rpg.integracion.model.enums;

/**
 * Ciclo de vida y alteraciones de estado del héroe en combate.
 * Cada constante sabe si el personaje puede actuar y cuánto daño sufre por turno.
 */
public enum EstadoPersonaje {
    VIVO(true, 0),
    DERROTADO(false, 0),
    ENVENENADO(true, 5),   // actúa, pero pierde vida en cada fase de actualización
    ATURDIDO(false, 0);    // pierde su turno de ataque

    private final boolean puedeActuar;
    private final int danoPorTurno;

    EstadoPersonaje(boolean puedeActuar, int danoPorTurno) {
        this.puedeActuar = puedeActuar;
        this.danoPorTurno = danoPorTurno;
    }

    public boolean puedeActuar() {
        return puedeActuar;
    }

    public int getDanoPorTurno() {
        return danoPorTurno;
    }
}
