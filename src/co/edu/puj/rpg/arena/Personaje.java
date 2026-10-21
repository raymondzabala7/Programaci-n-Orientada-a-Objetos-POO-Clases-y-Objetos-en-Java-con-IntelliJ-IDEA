package co.edu.puj.rpg.arena;

/**
 * Clase base abstracta: concentra el estado común (atributos protected)
 * y la lógica compartida. Obliga a las subclases a definir su ataque especial.
 */
public abstract class Personaje implements ICombatiente {

    protected String nombre;
    protected int vida;
    protected int ataqueBase;
    protected int posicionX;   // coordenada horizontal (columna)
    protected int posicionY;   // coordenada vertical (fila)

    protected int vidaMaxima;  // tope para las curaciones

    public Personaje(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataqueBase = ataqueBase;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    /** Cada subclase define su propia mecánica de ataque único. */
    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    // ---------- ICombatiente ----------

    /** Implementación por defecto: sin mitigación. Las subclases pueden sobrescribirla. */
    @Override
    public void defender(int danoRecibido) {
        recibirDano(danoRecibido);
    }

    // ---------- Métodos concretos compartidos ----------

    public boolean estaVivo() {
        return vida > 0;
    }

    /** Descuenta salud sin dejar que baje de cero. */
    public void recibirDano(int cantidad) {
        if (cantidad < 0) {
            return;
        }
        vida = Math.max(0, vida - cantidad);
        System.out.printf("   -> %s recibe %d de daño (vida: %d/%d)%n",
                nombre, cantidad, vida, vidaMaxima);
        if (vida == 0) {
            System.out.printf("   *** %s ha caído ***%n", nombre);
        }
    }

    public void curar(int cantidad) {
        if (cantidad <= 0 || !estaVivo()) {
            return;
        }
        int antes = vida;
        vida = Math.min(vidaMaxima, vida + cantidad);
        System.out.printf("   -> %s recupera %d de vida (vida: %d/%d)%n",
                nombre, vida - antes, vida, vidaMaxima);
    }

    // ---------- Sobrecarga de métodos (polimorfismo estático) ----------

    public void atacar(Personaje objetivo) {
        atacar(objetivo, false);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        int dano = modoFuria ? ataqueBase * 2 : ataqueBase;
        System.out.printf("%s ataca a %s%s.%n", nombre, objetivo.getNombre(),
                modoFuria ? " en MODO FURIA" : "");
        objetivo.defender(dano);
    }

    /** Hook que se ejecuta al comenzar el turno (por defecto no hace nada). */
    public void iniciarTurno() {
    }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    /** Símbolo que representa al personaje en el tablero. */
    public char getSimbolo() {
        return '?';
    }

    /** Línea de estado; las subclases añaden sus recursos propios. */
    public String getResumen() {
        return String.format("%s [%c] vida %d/%d pos(%d,%d)",
                nombre, getSimbolo(), vida, vidaMaxima, posicionX, posicionY);
    }

    // ---------- Getters ----------

    public String getNombre()   { return nombre; }
    public int getVida()        { return vida; }
    public int getAtaqueBase()  { return ataqueBase; }
    public int getPosicionX()   { return posicionX; }
    public int getPosicionY()   { return posicionY; }
}
