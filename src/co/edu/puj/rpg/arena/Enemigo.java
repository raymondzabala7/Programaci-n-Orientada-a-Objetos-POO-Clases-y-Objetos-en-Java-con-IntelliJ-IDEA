package co.edu.puj.rpg.arena;

/**
 * Enemigo controlado por la IA. Es una subclase concreta porque Personaje
 * es abstracta y no puede instanciarse directamente.
 */
public class Enemigo extends Personaje {

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY) {
        super(nombre, vida, ataqueBase, posX, posY);
    }

    /** Golpe salvaje: daño base + 5. */
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        System.out.printf("%s ejecuta un GOLPE SALVAJE contra %s.%n", nombre, objetivo.getNombre());
        objetivo.defender(ataqueBase + 5);
    }

    @Override
    public char getSimbolo() {
        return 'E';
    }
}
