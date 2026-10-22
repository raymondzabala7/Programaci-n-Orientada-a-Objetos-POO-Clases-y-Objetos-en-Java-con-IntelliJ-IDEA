package co.edu.puj.rpg.relaciones;

/**
 * Arma equipable. No conoce a su dueño (asociación unidireccional).
 */
public class Arma {

    private String nombre;
    private int danioBase;

    public Arma(String nombre, int danioBase) {
        this.nombre = nombre;
        this.danioBase = Math.max(0, danioBase);
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(" (daño +").append(danioBase).append(")");
        return sb.toString();
    }
}
