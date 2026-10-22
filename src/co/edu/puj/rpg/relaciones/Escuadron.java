package co.edu.puj.rpg.relaciones;

import java.util.ArrayList;
import java.util.List;

/**
 * Agregación (0..*): los personajes se crean fuera del escuadrón y siguen
 * existiendo aunque el escuadrón se disuelva.
 */
public class Escuadron {

    private String nombre;
    private List<Personaje> miembros; // Agregación (0..*)

    public Escuadron(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>(); // Inicialización de la colección
    }

    public void reclutarPersonaje(Personaje p) {
        if (p == null) {
            System.out.println("No se puede reclutar un personaje nulo.");
            return;
        }
        if (miembros.contains(p)) {
            System.out.printf("%s ya pertenece al escuadrón %s.%n", p.getNombre(), nombre);
            return;
        }
        miembros.add(p); // el objeto original no se copia ni se modifica
        System.out.printf("%s fue reclutado en el escuadrón %s.%n", p.getNombre(), nombre);
    }

    public void expulsarPersonaje(Personaje p) {
        if (p != null && miembros.remove(p)) {
            // p conserva su estado, vida y objetos intactos en memoria
            System.out.printf("%s fue expulsado del escuadrón %s.%n", p.getNombre(), nombre);
        } else {
            System.out.println("Ese personaje no pertenece al escuadrón.");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return new ArrayList<>(miembros);
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Escuadrón: ").append(nombre)
          .append(" (").append(miembros.size()).append(" miembros)");
        for (Personaje p : miembros) {
            sb.append(System.lineSeparator())
              .append("  - ").append(p.getNombre())
              .append(" | Nv ").append(p.getNivel())
              .append(" | ataque total ").append(p.calcularAtaqueTotal());
        }
        return sb.toString();
    }
}
