package co.edu.puj.rpg.arena;

public class Guerrero extends Personaje {

    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.fuerzaEscudo = fuerzaEscudo;
    }

    /** Golpe de escudo: daño base + fuerza del escudo. */
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        int dano = ataqueBase + fuerzaEscudo;
        System.out.printf("%s lanza un GOLPE DE ESCUDO contra %s.%n", nombre, objetivo.getNombre());
        objetivo.defender(dano);
    }

    /** El escudo mitiga la mitad de su fuerza (el daño mínimo recibido es 1). */
    @Override
    public void defender(int danoRecibido) {
        int mitigacion = fuerzaEscudo / 2;
        int danoReal = Math.max(1, danoRecibido - mitigacion);
        System.out.printf("   (el escudo de %s absorbe %d de daño)%n", nombre, danoRecibido - danoReal);
        recibirDano(danoReal);
    }

    @Override
    public char getSimbolo() {
        return 'G';
    }

    @Override
    public String getResumen() {
        return super.getResumen() + String.format(" | escudo %d", fuerzaEscudo);
    }
}
