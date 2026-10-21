package co.edu.puj.rpg.arena;

/**
 * Contrato comportamental puro: cualquier entidad que participe en el
 * combate debe saber procesar el daño que recibe.
 */
public interface ICombatiente {

    /** Procesa la reducción de daño según la armadura o mitigación de la entidad. */
    void defender(int danoRecibido);
}
