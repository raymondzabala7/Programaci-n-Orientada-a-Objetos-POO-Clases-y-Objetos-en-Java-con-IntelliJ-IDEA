package co.edu.puj.rpg.integracion.model.enums;

/** Rareza de los ítems; el multiplicador escala sus atributos y su precio. */
public enum RaridadItem {
    COMUN(1.0),
    RARO(1.5),
    LEGENDARIO(2.5);

    private final double multiplicadorStat;

    RaridadItem(double multiplicadorStat) {
        this.multiplicadorStat = multiplicadorStat;
    }

    public double getMultiplicadorStat() {
        return multiplicadorStat;
    }
}
