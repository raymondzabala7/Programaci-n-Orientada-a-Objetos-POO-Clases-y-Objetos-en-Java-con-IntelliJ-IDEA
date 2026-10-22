package co.edu.puj.rpg.relaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class AplicacionArenaRPG {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        // Colecciones globales (multiplicidad 0..* -> List)
        List<Personaje> personajes = new ArrayList<>();
        List<Gremio> gremios = new ArrayList<>();
        List<Escuadron> escuadrones = new ArrayList<>();

        do {
            System.out.println("\n=== MENÚ ARENA DE HÉROES RPG ===");
            System.out.println("1. Crear Personaje");
            System.out.println("2. Crear Gremio");
            System.out.println("3. Reclutar Personaje a Escuadrón");
            System.out.println("4. Equipar Arma a Personaje");
            System.out.println("5. Mostrar Estado Detallado (Personaje y Corazón RPG)");
            System.out.println("6. Salir");
            opcion = leerEntero(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    crearPersonaje(scanner, personajes, gremios);
                    break;
                case 2:
                    crearGremio(scanner, personajes, gremios);
                    break;
                case 3:
                    reclutarEnEscuadron(scanner, personajes, escuadrones);
                    break;
                case 4:
                    gestionarArma(scanner, personajes);
                    break;
                case 5:
                    mostrarEstado(scanner, personajes, gremios, escuadrones);
                    break;
                case 6:
                    System.out.println("Cerrando la Arena de Héroes...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    // =====================================================================
    // Casos del menú
    // =====================================================================

    private static void crearPersonaje(Scanner sc, List<Personaje> personajes, List<Gremio> gremios) {
        String nombre = leerTexto(sc, "Nombre del personaje: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        int nivel = leerEntero(sc, "Nivel inicial (mínimo 1): ");
        if (nivel < 1) {
            System.out.println("Nivel inválido, se usará 1.");
            nivel = 1;
        }
        Personaje p = new Personaje(nombre, nivel);
        personajes.add(p);
        System.out.printf("Personaje '%s' creado (nivel %d).%n", p.getNombre(), p.getNivel());

        if (!gremios.isEmpty()) {
            Gremio g = elegir(sc, gremios, "¿Unir a un gremio?", Gremio::getNombre);
            if (g != null) {
                p.unirseAGremio(g);   // sincronización bidireccional
            }
        }
    }

    private static void crearGremio(Scanner sc, List<Personaje> personajes, List<Gremio> gremios) {
        String nombre = leerTexto(sc, "Nombre del gremio: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        Gremio g = new Gremio(nombre);
        gremios.add(g);
        System.out.printf("Gremio '%s' creado.%n", g.getNombre());

        if (!personajes.isEmpty()) {
            Personaje p = elegir(sc, personajes, "¿Añadir un personaje al gremio?", Personaje::getNombre);
            if (p != null) {
                p.unirseAGremio(g);
            }
        }
    }

    private static void reclutarEnEscuadron(Scanner sc, List<Personaje> personajes, List<Escuadron> escuadrones) {
        Personaje p = elegir(sc, personajes, "Personaje a reclutar", Personaje::getNombre);
        if (p == null) {
            return;
        }
        Escuadron escuadron = null;
        if (!escuadrones.isEmpty()) {
            escuadron = elegir(sc, escuadrones, "Escuadrón destino (0 para crear uno nuevo)",
                    Escuadron::getNombre);
        }
        if (escuadron == null) {
            String nombre = leerTexto(sc, "Nombre del nuevo escuadrón: ");
            if (nombre.isEmpty()) {
                System.out.println("Operación cancelada.");
                return;
            }
            escuadron = new Escuadron(nombre);
            escuadrones.add(escuadron);
        }
        escuadron.reclutarPersonaje(p);   // agregación: p no se modifica
    }

    private static void gestionarArma(Scanner sc, List<Personaje> personajes) {
        Personaje p = elegir(sc, personajes, "Personaje", Personaje::getNombre);
        if (p == null) {
            return;
        }
        System.out.println("1. Equipar nueva arma");
        System.out.println("2. Retirar arma");
        int sub = leerEntero(sc, "Opción: ");
        if (sub == 1) {
            String nombreArma = leerTexto(sc, "Nombre del arma: ");
            int danio = leerEntero(sc, "Daño base: ");
            if (nombreArma.isEmpty() || danio < 0) {
                System.out.println("Datos del arma inválidos.");
                return;
            }
            p.equiparArma(new Arma(nombreArma, danio));   // asociación unidireccional
        } else if (sub == 2) {
            p.desequiparArma();
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void mostrarEstado(Scanner sc, List<Personaje> personajes,
                                      List<Gremio> gremios, List<Escuadron> escuadrones) {
        if (personajes.isEmpty()) {
            System.out.println("Aún no hay personajes creados.");
        } else {
            // Tabla resumen con printf
            System.out.println();
            System.out.printf("%-15s %5s %-22s %-14s %7s %6s %8s%n",
                    "PERSONAJE", "NIVEL", "ARMA", "GREMIO", "SALUD", "MANÁ", "ATAQUE");
            System.out.println("-".repeat(84));
            for (Personaje p : personajes) {
                String arma = p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "Desarmado";
                String gremio = p.getGremio() != null ? p.getGremio().getNombre() : "Sin gremio";
                System.out.printf("%-15s %5d %-22s %-14s %7d %6d %8d%n",
                        p.getNombre(), p.getNivel(), arma, gremio,
                        p.getSalud(), p.getMana(), p.calcularAtaqueTotal());
            }
        }

        System.out.println("\n--- Escuadrones ---");
        if (escuadrones.isEmpty()) {
            System.out.println("(ninguno)");
        }
        for (Escuadron e : escuadrones) {
            System.out.println(e);
        }

        System.out.println("\n--- Gremios ---");
        if (gremios.isEmpty()) {
            System.out.println("(ninguno)");
        }
        for (Gremio g : gremios) {
            System.out.println(g);
        }

        if (!personajes.isEmpty()) {
            System.out.println();
            Personaje detalle = elegir(sc, personajes, "Ver detalle completo de un personaje",
                    Personaje::getNombre);
            if (detalle != null) {
                System.out.println();
                System.out.println(detalle);   // toString con StringBuilder
            }
        }
    }

    // =====================================================================
    // Utilidades de entrada
    // =====================================================================

    /** Muestra una lista numerada y devuelve el elemento elegido (null si cancela o es inválido). */
    private static <T> T elegir(Scanner sc, List<T> lista, String titulo, Function<T, String> nombre) {
        if (lista.isEmpty()) {
            System.out.println("No hay elementos disponibles.");
            return null;
        }
        System.out.println(titulo + " (0 para cancelar):");
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, nombre.apply(lista.get(i)));
        }
        int idx = leerEntero(sc, "Selecciona: ");
        if (idx == 0) {
            return null;
        }
        if (idx < 1 || idx > lista.size()) {   // evita IndexOutOfBoundsException
            System.out.println("Selección inválida.");
            return null;
        }
        return lista.get(idx - 1);
    }

    /** Lee un entero; devuelve -1 si la entrada no es numérica (no rompe el programa). */
    private static int leerEntero(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        if (sc.hasNextInt()) {
            int valor = sc.nextInt();
            sc.nextLine();
            return valor;
        }
        sc.nextLine();
        return -1;
    }

    private static String leerTexto(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    // =====================================================================
    // SECCIÓN 5: DEPURACIÓN EN INTELLIJ (casos de estudio)
    // Para probar un caso: descoméntalo (Ctrl + /), ejecútalo en modo Debug y
    // vuelve a comentarlo.
    // =====================================================================

    // ---------------------------------------------------------------------
    // 5.1 StackOverflowError por toString() circular
    //
    // En Personaje:
    //   @Override public String toString() {
    //       return "Personaje " + nombre + " | " + gremio;        // llama a Gremio.toString()
    //   }
    // En Gremio:
    //   @Override public String toString() {
    //       return "Gremio " + nombre + " | " + miembros;         // llama a Personaje.toString()
    //   }
    //
    // CADENA: Personaje.toString() -> Gremio.toString() -> Personaje.toString() -> ...
    //
    // DIAGNÓSTICO: breakpoint en la primera línea de Personaje.toString(), Debug
    // (Shift + F9) y revisar Frames/Call Stack: se repiten ambos métodos.
    // SOLUCIÓN (ya aplicada en este proyecto): Gremio.toString() imprime solo
    // p.getNombre() y Personaje.toString() solo gremio.getNombre().
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // 5.2 NullPointerException
    //
    // Personaje p = new Personaje("Test", 1);
    // int d = p.getArmaEquipada().getDanioBase();   // armaEquipada es null
    //
    // DIAGNÓSTICO: en la ventana Variables, p.armaEquipada muestra null.
    // SOLUCIÓN: if (p.getArmaEquipada() != null) { ... } como en
    // calcularAtaqueTotal(). Tampoco hay NPE con el CorazonRPG porque se crea
    // dentro del constructor de Personaje.
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // 5.2 Desincronización bidireccional
    //
    // // En un unirseAGremio() defectuoso se hace solo:
    // this.gremio = nuevoGremio;      // y se olvida nuevoGremio.agregarMiembro(this)
    //
    // DIAGNÓSTICO: Watch (Alt + F8) con personaje.getGremio() y
    // gremio.getMiembros(): el gremio del personaje existe, pero la lista del
    // gremio no lo contiene.
    // SOLUCIÓN: actualizar ambos extremos, como hacen unirseAGremio() y
    // agregarMiembro() (la validación contains() evita la recursión infinita).
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // 5.2 ConcurrentModificationException
    //
    // for (Personaje p : escuadron.getMiembros()) {
    //     if (p.getSalud() == 0) escuadron.expulsarPersonaje(p);
    // }
    // // Con una lista interna directa (sin copia) esto lanzaría la excepción.
    //
    // SOLUCIONES: iterar sobre una copia (getMiembros() ya devuelve una) o usar
    // Iterator.remove() / removeIf().
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // GUÍA RÁPIDA DE DEBUG
    // 1. Breakpoint: clic en el margen junto al número de línea.
    // 2. Debug: Shift + F9 (o el ícono del insecto).
    // 3. F8 = Step Over, F7 = Step Into.
    // 4. Pestaña Variables: despliega personajes, gremios y escuadrones.
    // 5. Alt + F8 (Evaluate Expression): prueba expresiones sin reiniciar.
    // ---------------------------------------------------------------------
}
