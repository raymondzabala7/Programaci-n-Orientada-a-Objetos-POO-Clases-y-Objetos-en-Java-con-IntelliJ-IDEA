package co.edu.puj.rpg.arena;

import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor genérico: un solo tipo de inventario sirve para pociones,
 * equipamiento, etc. El compilador garantiza que solo entren objetos de tipo T
 * y elimina la necesidad de casteos al recuperar elementos.
 */
public class Inventario<T> {

    private List<T> items = new ArrayList<>();

    public void agregar(T item) {
        items.add(item);
    }

    public T obtener(int index) {
        return items.get(index);
    }

    public List<T> getItems() {
        return items;
    }

    // Métodos de apoyo para el juego
    public T remover(int index) {
        return items.remove(index);
    }

    public int tamano() {
        return items.size();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}
