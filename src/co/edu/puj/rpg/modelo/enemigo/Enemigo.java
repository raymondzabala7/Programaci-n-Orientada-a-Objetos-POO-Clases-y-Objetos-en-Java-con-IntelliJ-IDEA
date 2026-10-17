package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

/**
 * Enemigo de la arena. Vive en un subpaquete distinto al de Main para
 * verificar el aislamiento de acceso (private / package-private).
 */
public class Enemigo {

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private boolean estaVivo;

    public Enemigo(String nombre, int nivel, int vidaMaxima, int puntosAtaque) {
        this.nombre = nombre;
        this.nivel = Math.max(nivel, 1);
        this.vidaMaxima = Math.max(vidaMaxima, 1);
        this.puntosAtaque = puntosAtaque;
        this.estaVivo = true;
        setPuntosVida(this.vidaMaxima);
    }

    /** @return daño aplicado al héroe */
    public int atacar(Personaje heroe) {
        heroe.recibirDano(this.puntosAtaque);
        return this.puntosAtaque;
    }

    public void recibirDano(int cantidadDano) {
        setPuntosVida(this.puntosVida - cantidadDano);
    }

    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    // Recompensas por derrotarlo
    public int getRecompensaXp()       { return 25 * nivel; }
    public double getRecompensaOro()   { return 20.0 * nivel; }

    public String getNombre()    { return nombre; }
    public int getPuntosVida()   { return puntosVida; }
    public int getVidaMaxima()   { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public int getNivel()        { return nivel; }
    public boolean isEstaVivo()  { return estaVivo; }
}