package co.edu.puj.rpg.integracion.engine;

import co.edu.puj.rpg.integracion.exception.SinManaException;
import co.edu.puj.rpg.integracion.model.Personaje;
import co.edu.puj.rpg.integracion.model.enums.EstadoPersonaje;

/**
 * Capa de dominio del combate. Evalúa reglas, deja propagar las excepciones
 * de dominio hacia Main y gestiona la fase de actualización de estados.
 */
public final class CombateEngine {

    private static final int DANO_CONTRAATAQUE = 20;
    private static final int MANA_POR_TURNO = 8;

    private CombateEngine() {
    }

    /**
     * Ataque especial por maná.
     *
     * @return daño causado (0 si el personaje no pudo actuar)
     * @throws SinManaException se propaga desde Personaje; el turno no se consume
     */
    public static int ejecutarAtaqueEspecial(Personaje atacante, int costoMana) throws SinManaException {
        if (!atacante.getEstado().puedeActuar()) {
            perderTurno(atacante);
            return 0;
        }
        int dano = atacante.lanzarHechizo(costoMana);   // puede lanzar SinManaException
        System.out.printf("%s lanza un hechizo y causa %d de daño.%n", atacante.getNombre(), dano);
        contraataqueEnemigo(atacante);
        faseActualizacion(atacante);
        return dano;
    }

    /**
     * Técnica de sacrificio: paga vida para causar el doble de daño.
     *
     * @throws co.edu.puj.rpg.integracion.exception.VidaInsuficienteException (no verificada)
     */
    public static int ejecutarTecnicaSacrificio(Personaje atacante, int costoVida) {
        if (!atacante.getEstado().puedeActuar()) {
            perderTurno(atacante);
            return 0;
        }
        atacante.consumirVida(costoVida);               // puede lanzar VidaInsuficienteException
        int dano = costoVida * 2;
        System.out.printf("%s sacrifica %d de vida y causa %d de daño.%n",
                atacante.getNombre(), costoVida, dano);
        contraataqueEnemigo(atacante);
        faseActualizacion(atacante);
        return dano;
    }

    private static void perderTurno(Personaje p) {
        if (p.getEstado() == EstadoPersonaje.DERROTADO) {
            System.out.printf("%s está derrotado y no puede actuar.%n", p.getNombre());
        } else {
            System.out.printf("%s está %s y pierde su turno.%n", p.getNombre(), p.getEstado());
            faseActualizacion(p);
        }
    }

    private static void contraataqueEnemigo(Personaje p) {
        int real = p.recibirDano(DANO_CONTRAATAQUE);
        System.out.printf("El enemigo contraataca: %d de daño (vida %d/%d).%n",
                real, p.getVida(), p.getVidaMaxima());
        if (p.getEstado() == EstadoPersonaje.DERROTADO) {
            System.out.printf("*** %s ha sido derrotado ***%n", p.getNombre());
            return;
        }
        if (p.getEstado() == EstadoPersonaje.VIVO) {
            double azar = Math.random();
            if (azar < 0.25) {
                p.aplicarEstado(EstadoPersonaje.ENVENENADO, 3);
                System.out.printf("%s ha sido ENVENENADO por 3 turnos.%n", p.getNombre());
            } else if (azar < 0.40) {
                p.aplicarEstado(EstadoPersonaje.ATURDIDO, 1);
                System.out.printf("%s ha sido ATURDIDO y perderá su próximo turno.%n", p.getNombre());
            }
        }
    }

    /** Fase de actualización: veneno, duración de estados y regeneración de maná. */
    public static void faseActualizacion(Personaje p) {
        if (p.getEstado() == EstadoPersonaje.DERROTADO) {
            return;
        }
        if (p.getEstado() == EstadoPersonaje.ENVENENADO) {
            int veneno = p.getEstado().getDanoPorTurno();
            p.recibirDanoDirecto(veneno);
            System.out.printf("El veneno causa %d de daño a %s (vida %d/%d).%n",
                    veneno, p.getNombre(), p.getVida(), p.getVidaMaxima());
            if (p.getEstado() == EstadoPersonaje.DERROTADO) {
                System.out.printf("*** %s ha sucumbido al veneno ***%n", p.getNombre());
                return;
            }
        }
        p.avanzarEstado();
        p.regenerarMana(MANA_POR_TURNO);
    }
}
