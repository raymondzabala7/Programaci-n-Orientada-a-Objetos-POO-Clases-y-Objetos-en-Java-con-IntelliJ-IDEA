package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

/**
 * Héroe del juego. Todos los atributos son privados: el exterior solo
 * puede interactuar mediante la API pública (getters, setters validados
 * y métodos de comportamiento).
 */
public class Personaje {

    // ---------- Sección 1: atributos privados ----------
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    // Atributos adicionales para los retos de expansión
    private int experiencia;
    private int pociones;
    private boolean ultimoGolpeCritico;

    private static final double PROBABILIDAD_CRITICO = 0.15;

    // ---------- Sección 1: constructores ----------

    /** Constructor completo. Usa 'this' para desambiguar parámetros. */
    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = Math.max(vidaMaxima, 1);
        this.puntosAtaque = puntosAtaque;
        this.nivel = 1;
        this.experiencia = 0;
        this.pociones = 2;          // inventario inicial
        this.estaVivo = true;
        setOro(oro);                // pasa por la validación
        setPuntosVida(this.vidaMaxima); // empieza con la vida llena
    }

    /** Constructor por defecto: delega en el completo con valores base. */
    public Personaje() {
        this("Héroe", 100, 10, 0.0);
    }

    // ---------- Sección 1: API pública de comportamiento ----------

    /**
     * Ataca a un enemigo. Con 15% de probabilidad el golpe es crítico
     * y el daño se duplica (Reto 2).
     *
     * @return daño realmente aplicado
     */
    public int atacar(Enemigo enemigo) {
        int dano = this.puntosAtaque;
        this.ultimoGolpeCritico = Math.random() < PROBABILIDAD_CRITICO;
        if (this.ultimoGolpeCritico) {
            dano *= 2;
        }
        enemigo.recibirDano(dano);
        return dano;
    }

    public void usarPocion(int cantidadRestaurada) {
        setPuntosVida(this.puntosVida + cantidadRestaurada);
    }

    public void recibirDano(int cantidadDano) {
        setPuntosVida(this.puntosVida - cantidadDano);
    }

    // ---------- Reto 1: inventario de pociones ----------

    public void agregarPocion() {
        this.pociones++;
    }

    /** Consume una poción del inventario. @return false si no quedan. */
    public boolean gastarPocion() {
        if (this.pociones > 0) {
            this.pociones--;
            return true;
        }
        return false;
    }

    // ---------- Reto 2: experiencia y subida de nivel ----------

    /**
     * Suma experiencia y sube de nivel cada vez que se supera el umbral.
     * Es la única vía para cambiar el nivel desde fuera del paquete,
     * ya que setNivel() es protected.
     *
     * @return cantidad de niveles ganados
     */
    public int ganarExperiencia(int puntos) {
        if (puntos <= 0) {
            return 0;
        }
        this.experiencia += puntos;
        int nivelesGanados = 0;
        while (this.experiencia >= getExperienciaParaSubir()) {
            this.experiencia -= getExperienciaParaSubir();
            setNivel(this.nivel + 1);
            this.puntosAtaque += 2;
            this.vidaMaxima += 10;
            setPuntosVida(this.vidaMaxima); // se cura al subir de nivel
            nivelesGanados++;
        }
        return nivelesGanados;
    }

    /** Umbral de experiencia para pasar al siguiente nivel. */
    public int getExperienciaParaSubir() {
        return 50 * this.nivel;
    }

    // ---------- Sección 2: setters con lógica de control ----------

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

    /**
     * Rechaza saldos negativos sin alterar el atributo.
     *
     * @return true si el saldo fue aceptado, false si fue rechazado
     */
    public boolean setOro(double oro) {
        if (oro < 0.0) {
            System.out.println("Saldo insuficiente: operación rechazada.");
            return false;
        }
        this.oro = oro;
        return true;
    }

    /** protected según el Reto 2. Ignora valores menores a 1. */
    protected void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
        }
    }

    // ---------- Sección 2: getters públicos ----------

    public String getNombre()          { return nombre; }
    public int getPuntosVida()         { return puntosVida; }
    public int getVidaMaxima()         { return vidaMaxima; }
    public int getPuntosAtaque()       { return puntosAtaque; }
    public int getNivel()              { return nivel; }
    public double getOro()             { return oro; }
    public boolean isEstaVivo()        { return estaVivo; }
    public int getExperiencia()        { return experiencia; }
    public int getPociones()           { return pociones; }
    public boolean isUltimoGolpeCritico() { return ultimoGolpeCritico; }
}