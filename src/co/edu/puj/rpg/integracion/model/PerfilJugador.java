package co.edu.puj.rpg.integracion.model;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/** Perfil global del jugador con su registro de logros (únicos y ordenados). */
public class PerfilJugador {

    private final String nombre;
    private final Set<String> logros = new TreeSet<>();   // orden alfabético natural

    public PerfilJugador(String nombre) {
        this.nombre = nombre;
    }

    /** @return true si es un logro nuevo; false si ya estaba desbloqueado. */
    public boolean registrarLogro(String logro) {
        if (logro == null || logro.isBlank()) {
            return false;
        }
        return logros.add(logro);
    }

    /** Vista no modificable; la iteración sigue el orden alfabético del TreeSet. */
    public Set<String> listarLogrosOrdenados() {
        return Collections.unmodifiableSet(logros);
    }

    public String getNombre() {
        return nombre;
    }
}
