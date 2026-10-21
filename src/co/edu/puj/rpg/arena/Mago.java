package co.edu.puj.rpg.arena;

public class Mago extends Personaje {

    private static final int COSTO_HECHIZO = 10;
    private static final int REGENERACION_MANA = 5;

    protected int puntosMana;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.puntosMana = puntosMana;
    }

    /** Bola de fuego: consume maná y duplica el ataque base. Sin maná, ataca débilmente. */
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (puntosMana >= COSTO_HECHIZO) {
            puntosMana -= COSTO_HECHIZO;
            System.out.printf("%s lanza una BOLA DE FUEGO contra %s (maná restante: %d).%n",
                    nombre, objetivo.getNombre(), puntosMana);
            objetivo.defender(ataqueBase * 2);
        } else {
            System.out.printf("%s no tiene maná suficiente y usa un golpe de bastón.%n", nombre);
            objetivo.defender(Math.max(1, ataqueBase / 3));
        }
    }

    /** Recupera un poco de maná al comenzar cada turno. */
    @Override
    public void iniciarTurno() {
        puntosMana += REGENERACION_MANA;
    }

    @Override
    public char getSimbolo() {
        return 'M';
    }

    @Override
    public String getResumen() {
        return super.getResumen() + String.format(" | maná %d", puntosMana);
    }

    public int getPuntosMana() {
        return puntosMana;
    }
}
