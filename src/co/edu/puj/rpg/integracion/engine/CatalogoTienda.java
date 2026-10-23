package co.edu.puj.rpg.integracion.engine;

import co.edu.puj.rpg.integracion.model.Item;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Catálogo de precios ordenado alfabéticamente por nombre de ítem.
 * TreeMap: las entradas se recorren siempre en orden de clave.
 */
public class CatalogoTienda {

    private final Map<String, Double> precios = new TreeMap<>();

    public void agregarItem(Item item) {
        precios.put(item.getNombre(), item.getPrecio());
    }

    public boolean contiene(String nombre) {
        return precios.containsKey(nombre);
    }

    /** @return el precio del ítem, o -1.0 si no existe en el catálogo. */
    public double obtenerPrecio(String nombre) {
        if (!precios.containsKey(nombre)) {
            return -1.0;
        }
        return precios.get(nombre);
    }

    public Map<String, Double> getPrecios() {
        return Collections.unmodifiableMap(precios);
    }
}
