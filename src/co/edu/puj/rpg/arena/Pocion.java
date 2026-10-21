package co.edu.puj.rpg.arena;

public class Pocion {

    private final String nombre;
    private final int curacion;

    public Pocion(String nombre, int curacion) {
        this.nombre = nombre;
        this.curacion = curacion;
    }

    public String getNombre() { return nombre; }
    public int getCuracion()  { return curacion; }

    @Override
    public String toString() {
        return String.format("%s (+%d PV)", nombre, curacion);
    }
}
