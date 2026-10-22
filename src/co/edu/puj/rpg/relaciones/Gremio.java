package co.edu.puj.rpg.relaciones;

import java.util.ArrayList;
import java.util.List;

/**
 * Gremio: extremo "1" de la asociación bidireccional Personaje <-> Gremio.
 */
public class Gremio {

    private String nombre;
    private List<Personaje> miembros;   // 0..*

    public Gremio(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    /**
     * Registra al personaje y sincroniza el otro extremo.
     * La validación contains() evita la recursión infinita.
     */
    public void agregarMiembro(Personaje p) {
        if (p == null) {
            return;
        }
        if (!miembros.contains(p)) {
            miembros.add(p);
        }
        if (p.getGremio() != this) {
            p.unirseAGremio(this);
        }
    }

    /** Retira al personaje y sincroniza el otro extremo. */
    public void removerMiembro(Personaje p) {
        if (p == null) {
            return;
        }
        if (miembros.remove(p) && p.getGremio() == this) {
            p.salirDeGremio();
        }
    }

    public String getNombre() {
        return nombre;
    }

    /** Copia defensiva: quien la recorra no puede romper la sincronización. */
    public List<Personaje> getMiembros() {
        return new ArrayList<>(miembros);
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }

    /**
     * Solo imprime NOMBRES de los miembros. Llamar a Personaje.toString()
     * aquí provocaría StackOverflowError (ver sección 5 del taller).
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gremio: ").append(nombre)
          .append(" (").append(miembros.size()).append(" miembros)");
        for (Personaje p : miembros) {
            sb.append(System.lineSeparator()).append("  - ").append(p.getNombre());
        }
        return sb.toString();
    }
}
