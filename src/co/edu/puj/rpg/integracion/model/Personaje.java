package co.edu.puj.rpg.integracion.model;

import co.edu.puj.rpg.integracion.exception.CapacidadExcedidaException;
import co.edu.puj.rpg.integracion.exception.SinManaException;
import co.edu.puj.rpg.integracion.exception.VidaInsuficienteException;
import co.edu.puj.rpg.integracion.model.enums.EstadoPersonaje;
import co.edu.puj.rpg.integracion.model.enums.TipoPersonaje;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Héroe de la arena, refactorizado con enums, Set y excepciones de dominio.
 */
public class Personaje {

    public static final int CAPACIDAD_HABILIDADES = 5;

    private static final int VIDA_BASE = 100;
    private static final int MANA_BASE = 60;
    private static final int ATAQUE_BASE = 10;
    private static final int DEFENSA_BASE = 10;

    private final String nombre;
    private final TipoPersonaje tipoPersonaje;   // sustituye al String tipo
    private EstadoPersonaje estado;              // sustituye a banderas booleanas
    private int turnosEstadoRestantes;

    private int vida;
    private final int vidaMaxima = VIDA_BASE;
    private int mana;
    private final int manaMaximo = MANA_BASE;

    // Libro de habilidades: sin duplicados, el orden no importa -> HashSet
    private final Set<String> habilidades = new HashSet<>();

    public Personaje(String nombre, TipoPersonaje tipoPersonaje) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.tipoPersonaje = Objects.requireNonNull(tipoPersonaje, "El tipo no puede ser nulo");
        this.estado = EstadoPersonaje.VIVO;
        this.turnosEstadoRestantes = 0;
        this.vida = vidaMaxima;
        this.mana = manaMaximo;
    }

    // ------------------------------------------------------------------
    // Estadísticas: consumen los bonificadores encapsulados en el enum
    // ------------------------------------------------------------------

    public double calcularAtaque() {
        return ATAQUE_BASE * (1.0 + tipoPersonaje.getBonificadorAtaqueBase());
    }

    public double calcularDefensa() {
        return DEFENSA_BASE * (1.0 + tipoPersonaje.getBonificadorDefensaBase());
    }

    // ------------------------------------------------------------------
    // Sección 2: Set de habilidades
    // ------------------------------------------------------------------

    /**
     * @return true si se agregó; false si la habilidad ya existía (duplicado rechazado).
     * @throws CapacidadExcedidaException si el libro ya alcanzó su límite máximo.
     */
    public boolean aprenderHabilidad(String habilidad) throws CapacidadExcedidaException {
        if (habilidad == null || habilidad.isBlank()) {
            throw new IllegalArgumentException("La habilidad no puede estar vacía");
        }
        if (habilidades.contains(habilidad)) {
            return false;                          // no altera la colección
        }
        if (habilidades.size() >= CAPACIDAD_HABILIDADES) {
            throw new CapacidadExcedidaException(String.format(
                    "El libro de %s está lleno (máximo %d habilidades).", nombre, CAPACIDAD_HABILIDADES));
        }
        return habilidades.add(habilidad);
    }

    /** Vista no modificable del conjunto de habilidades. */
    public Set<String> obtenerHabilidades() {
        return Collections.unmodifiableSet(habilidades);
    }

    /** Alias usado en la guía de depuración (Evaluate Expression). */
    public Set<String> getHabilidades() {
        return obtenerHabilidades();
    }

    // ------------------------------------------------------------------
    // Sección 4: reglas de negocio que lanzan excepciones
    // ------------------------------------------------------------------

    /**
     * Lanza un hechizo mágico.
     *
     * @return daño causado
     * @throws SinManaException si costoMana > manaActual (excepción verificada)
     */
    public int lanzarHechizo(int costoMana) throws SinManaException {
        if (costoMana <= 0) {
            throw new IllegalArgumentException("El costo de maná debe ser positivo");
        }
        if (costoMana > mana) {
            throw new SinManaException(String.format(
                    "%s no tiene maná suficiente: requiere %d y dispone de %d.", nombre, costoMana, mana));
        }
        mana -= costoMana;
        return (int) Math.round(calcularAtaque() + costoMana * 2.0);
    }

    /**
     * Consume puntos de vida como costo de una acción.
     *
     * @throws VidaInsuficienteException si costoVida >= vidaActual (no verificada)
     */
    public void consumirVida(int costoVida) {
        if (costoVida <= 0) {
            throw new IllegalArgumentException("El costo de vida debe ser positivo");
        }
        if (costoVida >= vida) {
            throw new VidaInsuficienteException(String.format(
                    "%s no puede pagar %d de vida: solo tiene %d.", nombre, costoVida, vida));
        }
        vida -= costoVida;
    }

    // ------------------------------------------------------------------
    // Daño y estados
    // ------------------------------------------------------------------

    /** Aplica daño mitigado por la defensa. @return daño real recibido. */
    public int recibirDano(int dano) {
        if (dano <= 0 || estado == EstadoPersonaje.DERROTADO) {
            return 0;
        }
        int real = Math.max(1, dano - (int) Math.round(calcularDefensa()));
        reducirVida(real);
        return real;
    }

    /** Daño que ignora la defensa (por ejemplo, veneno). */
    public void recibirDanoDirecto(int dano) {
        if (dano > 0 && estado != EstadoPersonaje.DERROTADO) {
            reducirVida(dano);
        }
    }

    private void reducirVida(int cantidad) {
        vida = Math.max(0, vida - cantidad);
        if (vida == 0) {
            estado = EstadoPersonaje.DERROTADO;
            turnosEstadoRestantes = 0;
        }
    }

    public void aplicarEstado(EstadoPersonaje nuevoEstado, int turnos) {
        if (estado == EstadoPersonaje.DERROTADO) {
            return;
        }
        this.estado = nuevoEstado;
        this.turnosEstadoRestantes = turnos;
    }

    /** Avanza un turno de duración; al terminar, ENVENENADO/ATURDIDO vuelven a VIVO. */
    public void avanzarEstado() {
        if (turnosEstadoRestantes > 0) {
            turnosEstadoRestantes--;
            if (turnosEstadoRestantes == 0
                    && (estado == EstadoPersonaje.ENVENENADO || estado == EstadoPersonaje.ATURDIDO)) {
                estado = EstadoPersonaje.VIVO;
            }
        }
    }

    public void regenerarMana(int cantidad) {
        if (cantidad > 0 && estado != EstadoPersonaje.DERROTADO) {
            mana = Math.min(manaMaximo, mana + cantidad);
        }
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------

    public String getNombre()               { return nombre; }
    public TipoPersonaje getTipoPersonaje() { return tipoPersonaje; }
    public EstadoPersonaje getEstado()      { return estado; }
    public int getVida()                    { return vida; }
    public int getVidaMaxima()              { return vidaMaxima; }
    public int getMana()                    { return mana; }
    public int getManaMaximo()              { return manaMaximo; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre).append(" [").append(tipoPersonaje).append("] ")
          .append(estado).append(" | vida ").append(vida).append('/').append(vidaMaxima)
          .append(" | maná ").append(mana).append('/').append(manaMaximo)
          .append(String.format(" | ataque %.1f | defensa %.1f", calcularAtaque(), calcularDefensa()))
          .append(" | habilidades ").append(habilidades);
        return sb.toString();
    }
}
