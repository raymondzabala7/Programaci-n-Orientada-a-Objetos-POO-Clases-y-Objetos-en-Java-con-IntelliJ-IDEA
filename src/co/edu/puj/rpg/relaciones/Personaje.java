package co.edu.puj.rpg.relaciones;

/**
 * Clase central del dominio:
 *  - Asociación unidireccional  Personaje -> Arma       (0..1)
 *  - Asociación bidireccional   Personaje <-> Gremio    (0..* a 1)
 *  - Composición                Personaje *-- CorazonRPG (1..1)
 */
public class Personaje {

    private String nombre;
    private int nivel;
    private Arma armaEquipada;          // Asociación unidireccional (0..1)
    private Gremio gremio;              // Asociación bidireccional (0..1)
    private final CorazonRPG corazon;   // Composición (1..1): final y sin setter

    public Personaje(String nombre, int nivel) {
        this.nombre = nombre;
        this.nivel = Math.max(1, nivel);
        this.armaEquipada = null;       // Inicialmente desarmado
        this.gremio = null;             // Inicialmente sin gremio
        // Composición: el contenedor crea su componente
        this.corazon = new CorazonRPG(100 + this.nivel * 10, 50 + this.nivel * 5);
    }

    // ---------------- Asociación unidireccional: Arma ----------------

    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.println("No se puede equipar un arma nula.");
            return;
        }
        this.armaEquipada = nuevaArma;
        System.out.printf("%s equipó %s.%n", nombre, nuevaArma);
    }

    public void desequiparArma() {
        if (this.armaEquipada != null) {
            System.out.printf("%s guardó %s y queda desarmado.%n", nombre, armaEquipada.getNombre());
            this.armaEquipada = null;
        } else {
            System.out.printf("%s no tiene arma para retirar.%n", nombre);
        }
    }

    public int calcularAtaqueTotal() {
        int ataqueBase = this.nivel * 5;
        if (this.armaEquipada != null) {      // evita NullPointerException
            ataqueBase += this.armaEquipada.getDanioBase();
        }
        return ataqueBase;
    }

    // ---------------- Asociación bidireccional: Gremio ----------------

    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == this.gremio) {
            return;
        }
        if (this.gremio != null) {
            salirDeGremio();                   // desvincular del gremio previo
        }
        this.gremio = nuevoGremio;
        nuevoGremio.agregarMiembro(this);      // sincronización cruzada
        System.out.printf("%s se unió al gremio %s.%n", nombre, nuevoGremio.getNombre());
    }

    public void salirDeGremio() {
        if (this.gremio == null) {
            return;
        }
        Gremio anterior = this.gremio;
        this.gremio = null;                    // primero se limpia, así no hay recursión
        anterior.removerMiembro(this);
        System.out.printf("%s salió del gremio %s.%n", nombre, anterior.getNombre());
    }

    // ---------------- Composición: acceso mediado ----------------
    // No hay getCorazon() ni setCorazon(): el exterior solo ve valores.

    public int getSalud() {
        return corazon.getSaludBase();
    }

    public int getMana() {
        return corazon.getManaBase();
    }

    public void recibirDano(int cantidad) {
        corazon.recibirDano(cantidad);
    }

    // ---------------- Getters ----------------

    public String getNombre()       { return nombre; }
    public int getNivel()           { return nivel; }
    public Arma getArmaEquipada()   { return armaEquipada; }
    public Gremio getGremio()       { return gremio; }

    /**
     * Reporte con StringBuilder. Del gremio solo imprime el NOMBRE: llamar a
     * gremio.toString() cerraría el ciclo Personaje -> Gremio -> Personaje
     * y lanzaría StackOverflowError.
     */
    @Override
    public String toString() {
        String sep = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append("Personaje: ").append(nombre).append(sep);
        sb.append("  Nivel        : ").append(nivel).append(sep);
        sb.append("  Arma         : ")
          .append(armaEquipada != null ? armaEquipada.toString() : "Desarmado").append(sep);
        sb.append("  Gremio       : ")
          .append(gremio != null ? gremio.getNombre() : "Sin gremio").append(sep);
        sb.append("  Corazón      : ").append(corazon).append(sep);
        sb.append("  Ataque total : ").append(calcularAtaqueTotal());
        return sb.toString();
    }
}
