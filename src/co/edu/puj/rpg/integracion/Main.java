package co.edu.puj.rpg.integracion;

import co.edu.puj.rpg.integracion.engine.Bestiario;
import co.edu.puj.rpg.integracion.engine.CatalogoTienda;
import co.edu.puj.rpg.integracion.engine.CombateEngine;
import co.edu.puj.rpg.integracion.exception.CapacidadExcedidaException;
import co.edu.puj.rpg.integracion.exception.SinManaException;
import co.edu.puj.rpg.integracion.exception.VidaInsuficienteException;
import co.edu.puj.rpg.integracion.model.Item;
import co.edu.puj.rpg.integracion.model.Personaje;
import co.edu.puj.rpg.integracion.model.PerfilJugador;
import co.edu.puj.rpg.integracion.model.enums.RaridadItem;
import co.edu.puj.rpg.integracion.model.enums.TipoPersonaje;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Capa de interfaz: captura las excepciones de dominio con catch específicos,
 * muestra e.getMessage() y nunca interrumpe el bucle principal.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Personaje> personajes = new ArrayList<>();
    private static final Bestiario bestiario = new Bestiario();
    private static final PerfilJugador perfil = new PerfilJugador("Jugador 1");
    private static final CatalogoTienda tienda = crearTienda();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n=== ARENA DE HÉROES RPG ===");
            System.out.println("1. Crear Personaje");
            System.out.println("2. Asignar Habilidad Única");
            System.out.println("3. Registrar Victoria en Bestiario");
            System.out.println("4. Ejecutar Ataque Especial");
            System.out.println("5. Consultar Bestiario y Logros");
            System.out.println("6. Salir");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    crearPersonaje();
                    break;
                case 2:
                    asignarHabilidad();
                    break;
                case 3:
                    registrarVictoria();
                    break;
                case 4:
                    ejecutarAtaqueEspecial();
                    break;
                case 5:
                    consultar();
                    break;
                case 6:
                    System.out.println("Cerrando la Arena de Héroes...");
                    break;
                default:
                    System.err.println("Opción inválida. Elige un número del 1 al 6.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    // =====================================================================
    // Opciones del menú
    // =====================================================================

    private static void crearPersonaje() {
        String nombre = leerTexto("Nombre del personaje: ");
        if (nombre.isEmpty()) {
            System.err.println("El nombre no puede estar vacío.");
            return;
        }
        TipoPersonaje[] tipos = TipoPersonaje.values();
        System.out.println("Tipos disponibles:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("  %d. %-8s (ataque +%.0f%%, defensa +%.0f%%)%n", i + 1, tipos[i],
                    tipos[i].getBonificadorAtaqueBase() * 100,
                    tipos[i].getBonificadorDefensaBase() * 100);
        }
        int idx = leerEntero("Elige el tipo: ");
        if (idx < 1 || idx > tipos.length) {
            System.err.println("Tipo inválido.");
            return;
        }
        Personaje p = new Personaje(nombre, tipos[idx - 1]);   // estado inicial: VIVO
        personajes.add(p);
        System.out.println("Personaje creado: " + p);
    }

    private static void asignarHabilidad() {
        Personaje p = elegirPersonaje();
        if (p == null) {
            return;
        }
        String habilidad = leerTexto("Nombre de la habilidad: ");
        if (habilidad.isEmpty()) {
            System.err.println("La habilidad no puede estar vacía.");
            return;
        }
        try {
            boolean agregada = p.aprenderHabilidad(habilidad);
            if (agregada) {
                System.out.printf("%s aprendió '%s' (%d/%d).%n", p.getNombre(), habilidad,
                        p.obtenerHabilidades().size(), Personaje.CAPACIDAD_HABILIDADES);
                if (p.obtenerHabilidades().size() == Personaje.CAPACIDAD_HABILIDADES
                        && perfil.registrarLogro("Libro de hechizos completo")) {
                    System.out.println("¡Logro desbloqueado: Libro de hechizos completo!");
                }
            } else {
                System.out.printf("%s ya conoce la habilidad '%s'. Duplicado rechazado.%n",
                        p.getNombre(), habilidad);
            }
        } catch (CapacidadExcedidaException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    private static void registrarVictoria() {
        String enemigo = leerTexto("Nombre del enemigo derrotado: ");
        if (enemigo.isEmpty()) {
            System.err.println("El nombre del enemigo no puede estar vacío.");
            return;
        }
        boolean primera = bestiario.registrarVictoria(enemigo);
        System.out.printf("Victoria registrada contra '%s'. Total: %d.%n",
                enemigo, bestiario.getVictorias(enemigo));
        if (primera) {
            String logro = "Primera victoria contra " + enemigo;
            if (perfil.registrarLogro(logro)) {
                System.out.println("¡Logro desbloqueado: " + logro + "!");
            }
        }
    }

    private static void ejecutarAtaqueEspecial() {
        Personaje p = elegirPersonaje();
        if (p == null) {
            return;
        }
        System.out.println("1. Hechizo (cuesta maná)");
        System.out.println("2. Técnica de sacrificio (cuesta vida)");
        int tipo = leerEntero("Tipo de ataque: ");

        if (tipo == 1) {
            int costo = leerEntero("Costo de maná del ataque: ");
            if (costo <= 0) {
                System.err.println("El costo debe ser mayor que 0.");
                return;
            }
            try {
                int dano = CombateEngine.ejecutarAtaqueEspecial(p, costo);
                if (dano > 0 && costo >= 40 && perfil.registrarLogro("Maestro del Mana")) {
                    System.out.println("¡Logro desbloqueado: Maestro del Mana!");
                }
            } catch (SinManaException e) {
                System.out.println("[!] " + e.getMessage());
            }
        } else if (tipo == 2) {
            int costo = leerEntero("Puntos de vida a sacrificar: ");
            if (costo <= 0) {
                System.err.println("El costo debe ser mayor que 0.");
                return;
            }
            try {
                CombateEngine.ejecutarTecnicaSacrificio(p, costo);
            } catch (VidaInsuficienteException e) {
                System.out.println("[!] " + e.getMessage());
            }
        } else {
            System.err.println("Tipo de ataque inválido.");
        }
    }

    private static void consultar() {
        System.out.println("\n--- BESTIARIO ---");
        if (bestiario.getDerrotados().isEmpty()) {
            System.out.println("(sin victorias registradas)");
        }
        for (Map.Entry<String, Integer> entry : bestiario.getDerrotados().entrySet()) {
            System.out.printf("  %-25s %3d victorias%n", entry.getKey(), entry.getValue());
        }
        System.out.printf("  Total de victorias: %d%n", bestiario.getTotalVictorias());

        System.out.println("\n--- LOGROS (orden alfabético) ---");
        if (perfil.listarLogrosOrdenados().isEmpty()) {
            System.out.println("(ninguno todavía)");
        }
        for (String logro : perfil.listarLogrosOrdenados()) {
            System.out.println("  * " + logro);
        }

        System.out.println("\n--- PERSONAJES ---");
        if (personajes.isEmpty()) {
            System.out.println("(ninguno todavía)");
        }
        for (Personaje p : personajes) {
            System.out.println("  " + p);
        }

        System.out.println("\n--- CATÁLOGO DE LA TIENDA (orden alfabético) ---");
        for (Map.Entry<String, Double> entry : tienda.getPrecios().entrySet()) {
            System.out.printf("  %-20s %8.2f oro%n", entry.getKey(), entry.getValue());
        }
    }

    // =====================================================================
    // Utilidades
    // =====================================================================

    private static CatalogoTienda crearTienda() {
        CatalogoTienda catalogo = new CatalogoTienda();
        catalogo.agregarItem(new Item("Espada de hierro", 12, 40.0, RaridadItem.COMUN));
        catalogo.agregarItem(new Item("Arco élfico", 15, 60.0, RaridadItem.RARO));
        catalogo.agregarItem(new Item("Báculo arcano", 20, 90.0, RaridadItem.LEGENDARIO));
        catalogo.agregarItem(new Item("Daga oxidada", 6, 15.0, RaridadItem.COMUN));
        return catalogo;
    }

    private static Personaje elegirPersonaje() {
        if (personajes.isEmpty()) {
            System.out.println("Primero crea un personaje (opción 1).");
            return null;
        }
        System.out.println("Personajes (0 para cancelar):");
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.printf("  %d. %s [%s, %s]%n", i + 1, p.getNombre(), p.getTipoPersonaje(), p.getEstado());
        }
        int idx = leerEntero("Selecciona: ");
        if (idx == 0) {
            return null;
        }
        if (idx < 1 || idx > personajes.size()) {
            System.err.println("Selección inválida.");
            return null;
        }
        return personajes.get(idx - 1);
    }

    /**
     * Lee un entero blindado contra entradas inválidas: captura
     * InputMismatchException, limpia el búfer, avisa por System.err y reintenta.
     */
    private static int leerEntero(String mensaje) {
        while (true) {
            boolean lecturaExitosa = false;
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                lecturaExitosa = true;
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();   // descarta la entrada inválida
                System.err.println("Entrada inválida: escribe un número entero.");
            } finally {
                // Se ejecuta en cada intento: tras una lectura correcta consume
                // el salto de línea pendiente para no contaminar la siguiente lectura.
                if (lecturaExitosa) {
                    scanner.nextLine();
                }
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    // =====================================================================
    // SECCIÓN 6: DEPURACIÓN EN INTELLIJ
    // =====================================================================

    // ---------------------------------------------------------------------
    // 7.1 ANTIPATRONES REVISADOS EN ESTE PROYECTO
    //
    // 1. Exception swallowing: ningún catch está vacío; todos muestran
    //    e.getMessage() al usuario y el bucle del menú continúa.
    //        MAL : catch (SinManaException e) { }
    //        MAL : catch (SinManaException e) { e.printStackTrace(); }
    //
    // 2. Pérdida de la causa raíz: JuegoException y sus hijas tienen un
    //    constructor (mensaje, causa) que llama a super(mensaje, causa).
    //        throw new SinManaException("No se pudo lanzar", causaOriginal);
    //
    // 3. Claves nulas o inexistentes en un Map: el Bestiario usa containsKey y
    //    getOrDefault en lugar de asumir que get(clave) nunca devuelve null.
    //        MAL : int n = mapa.get("Orco") + 1;       // NullPointerException
    //        BIEN: int n = mapa.getOrDefault("Orco", 0) + 1;
    //
    // 4. equals() y hashCode(): si el Set/Map pasara de String a una clase propia
    //    (por ejemplo Set<Habilidad>) habría que sobrescribir ambos métodos, como
    //    hace Item. Sin ellos, HashSet insertaría objetos conceptualmente iguales.
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // 7.2 LISTA DE COMPROBACIÓN DE DEPURACIÓN
    //
    // 1. Breakpoints
    //    - En la línea de Main.asignarHabilidad() donde se llama a p.aprenderHabilidad(...).
    //    - Condicional en la línea "throw new SinManaException(...)" de
    //      Personaje.lanzarHechizo(), con la condición:  costoMana > mana
    //
    // 2. Inspección de colecciones (Shift + F9 para iniciar en Debug)
    //    - Pestaña Variables: despliega 'p' y examina 'habilidades' (HashSet):
    //      su tabla de buckets y el campo size.
    //    - Despliega 'bestiario.derrotados' (HashMap) y revisa size y los nodos.
    //
    // 3. Evaluate Expression (Alt + F8) con la ejecución pausada:
    //        bestiario.contiene("Orcos")
    //        p.getHabilidades().contains("Bola de Fuego")
    //        perfil.listarLogrosOrdenados()
    //    Comprueba que el resultado coincide con lo esperado y reanuda con F9.
    // ---------------------------------------------------------------------
}
