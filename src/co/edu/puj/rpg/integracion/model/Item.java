package co.edu.puj.rpg.integracion.model;

import co.edu.puj.rpg.integracion.model.enums.RaridadItem;

import java.util.Objects;

/** Ítem de equipamiento cuyos atributos se escalan según su rareza. */
public class Item {

    private final String nombre;
    private final int danioBase;
    private final double precioBase;
    private final RaridadItem rareza;

    public Item(String nombre, int danioBase, double precioBase, RaridadItem rareza) {
        this.nombre = Objects.requireNonNull(nombre);
        this.danioBase = danioBase;
        this.precioBase = precioBase;
        this.rareza = Objects.requireNonNull(rareza);
    }

    public int getDanioEfectivo() {
        return (int) Math.round(danioBase * rareza.getMultiplicadorStat());
    }

    public double getPrecio() {
        return precioBase * rareza.getMultiplicadorStat();
    }

    public String getNombre()    { return nombre; }
    public RaridadItem getRareza() { return rareza; }

    // Contrato equals/hashCode por nombre, por si el ítem se usa dentro de Set o Map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return nombre.equals(((Item) o).nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] daño %d, precio %.2f", nombre, rareza, getDanioEfectivo(), getPrecio());
    }
}
