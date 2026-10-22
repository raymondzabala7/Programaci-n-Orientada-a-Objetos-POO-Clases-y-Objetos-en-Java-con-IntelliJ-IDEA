package co.edu.puj.rpg.relaciones;

/**
 * Núcleo de atributos vitales del Personaje (Composición 1..1).
 * Solo Personaje lo instancia: no existe corazón sin personaje.
 */
public class CorazonRPG {

    private int saludBase;
    private int manaBase;

    public CorazonRPG(int saludBase, int manaBase) {
        this.saludBase = saludBase;
        this.manaBase = manaBase;
    }

    public int getSaludBase() {
        return saludBase;
    }

    public int getManaBase() {
        return manaBase;
    }

    /** Resta salud sin bajar de cero. */
    public void recibirDano(int cantidad) {
        if (cantidad > 0) {
            saludBase = Math.max(0, saludBase - cantidad);
        }
    }

    /** Gasta maná solo si hay suficiente. @return true si se pudo gastar. */
    public boolean gastarMana(int cantidad) {
        if (cantidad > 0 && cantidad <= manaBase) {
            manaBase -= cantidad;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CorazonRPG[salud=").append(saludBase)
          .append(", maná=").append(manaBase).append("]");
        return sb.toString();
    }
}
