package co.edu.puj.rpg.integracion.model.enums;

/**
 * Arquetipos de héroe. Cada constante lleva bonificadores inmutables
 * (porcentajes en forma decimal: 0.20 = +20%).
 */
public enum TipoPersonaje {
    GUERRERO(0.20, 0.30),
    MAGO(0.35, 0.05),
    ARQUERO(0.25, 0.10);

    private final double bonificadorAtaqueBase;
    private final double bonificadorDefensaBase;

    private TipoPersonaje(double bonificadorAtaqueBase, double bonificadorDefensaBase) {
        this.bonificadorAtaqueBase = bonificadorAtaqueBase;
        this.bonificadorDefensaBase = bonificadorDefensaBase;
    }

    public double getBonificadorAtaqueBase() {
        return bonificadorAtaqueBase;
    }

    public double getBonificadorDefensaBase() {
        return bonificadorDefensaBase;
    }
}
