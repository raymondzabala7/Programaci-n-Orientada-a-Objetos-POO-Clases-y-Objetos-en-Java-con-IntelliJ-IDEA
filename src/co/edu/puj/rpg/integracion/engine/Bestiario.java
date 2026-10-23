package co.edu.puj.rpg.integracion.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Cuenta cuántas veces se ha derrotado a cada tipo de enemigo.
 * HashMap: lecturas y escrituras en O(1).
 */
public class Bestiario {

    private final Map<String, Integer> derrotados = new HashMap<>();

    /**
     * Suma una victoria contra el enemigo.
     *
     * @return true si es la primera victoria contra ese enemigo
     */
    public boolean registrarVictoria(String enemigo) {
        boolean primera = !derrotados.containsKey(enemigo);           // verificación de clave
        derrotados.put(enemigo, derrotados.getOrDefault(enemigo, 0) + 1); // lectura segura + actualización
        return primera;
    }

    public boolean contiene(String enemigo) {
        return derrotados.containsKey(enemigo);
    }

    public int getVictorias(String enemigo) {
        return derrotados.getOrDefault(enemigo, 0);
    }

    public int getTotalVictorias() {
        int total = 0;
        for (int cantidad : derrotados.values()) {
            total += cantidad;
        }
        return total;
    }

    /** Vista no modificable para recorrerla con entrySet(). */
    public Map<String, Integer> getDerrotados() {
        return Collections.unmodifiableMap(derrotados);
    }
}
