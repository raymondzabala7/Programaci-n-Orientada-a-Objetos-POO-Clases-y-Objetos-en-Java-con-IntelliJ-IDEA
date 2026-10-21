package co.edu.puj.rpg.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArenaMain {

    private static final int TAM = 5;
    private static final int COFRE_X = 2;   // columna del cofre
    private static final int COFRE_Y = 2;   // fila del cofre
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // ---------- 4.1 Paso 1: Inicialización ----------
        char[][] tablero = new char[TAM][TAM];
        List<Personaje> heroes = new ArrayList<>();
        List<Personaje> enemigos = new ArrayList<>();
        Inventario<Pocion> inventario = new Inventario<>();

        heroes.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        heroes.add(new Mago("Gandalf", 80, 25, 1, 1, 50));

        enemigos.add(new Enemigo("Orco", 60, 12, 4, 0));
        enemigos.add(new Enemigo("Goblin", 40, 8, 0, 4));
        enemigos.add(new Enemigo("Troll", 90, 14, 4, 4));

        inventario.agregar(new Pocion("Poción menor", 30));
        inventario.agregar(new Pocion("Poción mayor", 60));
        inventario.agregar(new Pocion("Elixir", 45));

        Personaje heroePrincipal = heroes.get(0);
        int turno = 0;

        System.out.println("=== ARENA RPG ===");
        System.out.println("Objetivo: derrota a todos los enemigos o lleva a " + heroePrincipal.getNombre()
                + " hasta el cofre [C].");

        // ---------- 4.1 Paso 2: Ciclo principal (do-while) ----------
        do {
            turno++;
            System.out.printf("%n==================== TURNO %d ====================%n", turno);

            // Paso 3: Renderizado y estado
            actualizarTablero(tablero, heroes, enemigos);
            mostrarTablero(tablero);
            mostrarEstadisticas(heroes, enemigos);

            // Paso 4: Turno del jugador (for-each sobre héroes vivos)
            for (Personaje heroe : heroes) {
                if (!heroe.estaVivo()) {
                    continue;
                }
                if (enemigos.isEmpty() || estaEnCofre(heroePrincipal)) {
                    break;
                }
                heroe.iniciarTurno();
                turnoHeroe(heroe, heroes, enemigos, inventario);
                enemigos.removeIf(e -> !e.estaVivo()); // retira bajas enseguida
            }

            // Paso 5: Turno enemigo (IA)
            if (!enemigos.isEmpty() && !estaEnCofre(heroePrincipal)) {
                turnoEnemigos(heroes, enemigos);
            }

            // Paso 6: Limpieza de bajas
            heroes.removeIf(h -> !h.estaVivo());
            enemigos.removeIf(e -> !e.estaVivo());

        } while (!heroes.isEmpty() && sumaVida(heroes) > 0
                && !enemigos.isEmpty() && !estaEnCofre(heroePrincipal));

        // ---------- 4.2 Victoria / Derrota ----------
        actualizarTablero(tablero, heroes, enemigos);
        mostrarTablero(tablero);
        System.out.println();
        if (heroes.isEmpty() || sumaVida(heroes) <= 0) {
            System.out.println("*** DERROTA: tu escuadrón ha sido aniquilado. ***");
        } else if (estaEnCofre(heroePrincipal)) {
            System.out.println("*** VICTORIA: " + heroePrincipal.getNombre() + " alcanzó el cofre [C]. ***");
        } else {
            System.out.println("*** VICTORIA: todos los enemigos fueron eliminados. ***");
        }
        System.out.println("Turnos jugados: " + turno);
        sc.close();
    }

    // =====================================================================
    // Tablero 2D
    // =====================================================================

    public static void mostrarTablero(char[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println();
        }
    }

    /** Reconstruye la matriz desde las posiciones reales: refleja el estado en tiempo real. */
    private static void actualizarTablero(char[][] tablero, List<Personaje> heroes, List<Personaje> enemigos) {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = '.';          // nunca queda una celda sin inicializar
            }
        }
        tablero[COFRE_Y][COFRE_X] = 'C';
        colocar(tablero, enemigos);
        colocar(tablero, heroes);
    }

    private static void colocar(char[][] tablero, List<Personaje> lista) {
        for (Personaje p : lista) {
            if (p.estaVivo() && dentroDelTablero(p.getPosicionX(), p.getPosicionY())) {
                tablero[p.getPosicionY()][p.getPosicionX()] = p.getSimbolo();
            }
        }
    }

    private static boolean dentroDelTablero(int x, int y) {
        return x >= 0 && x < TAM && y >= 0 && y < TAM;
    }

    private static void mostrarEstadisticas(List<Personaje> heroes, List<Personaje> enemigos) {
        System.out.println("\n-- Héroes --");
        for (Personaje h : heroes) {
            System.out.println("  " + h.getResumen());
        }
        System.out.println("-- Enemigos --");
        for (Personaje e : enemigos) {
            System.out.println("  " + e.getResumen());
        }
    }

    // =====================================================================
    // Turno del jugador (menú con switch)
    // =====================================================================

    private static void turnoHeroe(Personaje heroe, List<Personaje> heroes,
                                   List<Personaje> enemigos, Inventario<Pocion> inventario) {
        boolean accionRealizada = false;
        do {
            System.out.printf("%n--- Turno de %s ---%n", heroe.getNombre());
            System.out.println("1. Atacar");
            System.out.printf("2. Usar Inventario (%d pociones)%n", inventario.tamano());
            System.out.println("3. Mover en Tablero");
            int opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    accionRealizada = accionAtacar(heroe, enemigos);
                    break;
                case 2:
                    accionRealizada = accionInventario(heroe, inventario);
                    break;
                case 3:
                    accionRealizada = accionMover(heroe, heroes, enemigos);
                    break;
                default:
                    System.out.println("Opción inválida. Elige 1, 2 o 3.");
                    break;
            }
        } while (!accionRealizada);
    }

    private static boolean accionAtacar(Personaje heroe, List<Personaje> enemigos) {
        System.out.println("Elige un enemigo (0 para cancelar):");
        for (int i = 0; i < enemigos.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, enemigos.get(i).getResumen());
        }
        int idx = leerEntero("Objetivo: ");
        if (idx == 0) {
            return false;
        }
        if (idx < 1 || idx > enemigos.size()) {   // evita IndexOutOfBoundsException
            System.out.println("Objetivo inválido.");
            return false;
        }
        heroe.realizarAtaqueEspecial(enemigos.get(idx - 1)); // polimorfismo
        return true;
    }

    private static boolean accionInventario(Personaje heroe, Inventario<Pocion> inventario) {
        if (inventario.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return false;
        }
        System.out.println("Elige una poción (0 para cancelar):");
        List<Pocion> pociones = inventario.getItems();
        for (int i = 0; i < pociones.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, pociones.get(i));
        }
        int idx = leerEntero("Poción: ");
        if (idx == 0) {
            return false;
        }
        if (idx < 1 || idx > inventario.tamano()) {
            System.out.println("Poción inválida.");
            return false;
        }
        Pocion pocion = inventario.obtener(idx - 1);   // sin casteo gracias a los genéricos
        System.out.printf("%s usa %s.%n", heroe.getNombre(), pocion.getNombre());
        heroe.curar(pocion.getCuracion());
        inventario.remover(idx - 1);
        return true;
    }

    private static boolean accionMover(Personaje heroe, List<Personaje> heroes, List<Personaje> enemigos) {
        System.out.println("1. Arriba  2. Abajo  3. Izquierda  4. Derecha  0. Cancelar");
        int dir = leerEntero("Dirección: ");
        int dx = 0;
        int dy = 0;
        switch (dir) {
            case 0:
                return false;
            case 1: dy = -1; break;
            case 2: dy = 1; break;
            case 3: dx = -1; break;
            case 4: dx = 1; break;
            default:
                System.out.println("Dirección inválida.");
                return false;
        }
        int nx = heroe.getPosicionX() + dx;
        int ny = heroe.getPosicionY() + dy;

        if (!dentroDelTablero(nx, ny)) {
            System.out.println("Movimiento fuera del tablero.");
            return false;
        }
        if (ocupada(nx, ny, heroes, enemigos)) {
            System.out.println("Esa celda está ocupada.");
            return false;
        }
        heroe.setPosicion(nx, ny);
        System.out.printf("%s se mueve a (%d,%d).%n", heroe.getNombre(), nx, ny);
        return true;
    }

    // =====================================================================
    // Turno enemigo (IA)
    // =====================================================================

    private static void turnoEnemigos(List<Personaje> heroes, List<Personaje> enemigos) {
        System.out.println("\n--- Turno enemigo ---");
        for (Personaje enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                continue;
            }
            Personaje objetivo = heroeMasCercano(enemigo, heroes);
            if (objetivo == null) {
                break;
            }
            if (distancia(enemigo, objetivo) <= 1) {
                if (Math.random() < 0.30) {
                    enemigo.realizarAtaqueEspecial(objetivo);
                } else {
                    enemigo.atacar(objetivo, enemigo.getVida() < 20); // sobrecarga: furia si está débil
                }
            } else {
                moverHacia(enemigo, objetivo, heroes, enemigos);
            }
        }
    }

    private static Personaje heroeMasCercano(Personaje enemigo, List<Personaje> heroes) {
        Personaje mejor = null;
        for (Personaje h : heroes) {
            if (h.estaVivo() && (mejor == null || distancia(enemigo, h) < distancia(enemigo, mejor))) {
                mejor = h;
            }
        }
        return mejor;
    }

    private static int distancia(Personaje a, Personaje b) {
        return Math.max(Math.abs(a.getPosicionX() - b.getPosicionX()),
                        Math.abs(a.getPosicionY() - b.getPosicionY()));
    }

    private static void moverHacia(Personaje enemigo, Personaje objetivo,
                                   List<Personaje> heroes, List<Personaje> enemigos) {
        int difX = objetivo.getPosicionX() - enemigo.getPosicionX();
        int difY = objetivo.getPosicionY() - enemigo.getPosicionY();
        int dx = Integer.signum(difX);
        int dy = Integer.signum(difY);

        boolean movido;
        if (Math.abs(difX) >= Math.abs(difY)) {
            movido = intentarMover(enemigo, dx, 0, heroes, enemigos) || intentarMover(enemigo, 0, dy, heroes, enemigos);
        } else {
            movido = intentarMover(enemigo, 0, dy, heroes, enemigos) || intentarMover(enemigo, dx, 0, heroes, enemigos);
        }
        if (!movido) {
            System.out.printf("%s no logra avanzar.%n", enemigo.getNombre());
        }
    }

    private static boolean intentarMover(Personaje p, int dx, int dy,
                                         List<Personaje> heroes, List<Personaje> enemigos) {
        if (dx == 0 && dy == 0) {
            return false;
        }
        int nx = p.getPosicionX() + dx;
        int ny = p.getPosicionY() + dy;
        boolean esCofre = (nx == COFRE_X && ny == COFRE_Y);
        if (!dentroDelTablero(nx, ny) || esCofre || ocupada(nx, ny, heroes, enemigos)) {
            return false;
        }
        p.setPosicion(nx, ny);
        System.out.printf("%s avanza a (%d,%d).%n", p.getNombre(), nx, ny);
        return true;
    }

    // =====================================================================
    // Utilidades
    // =====================================================================

    private static boolean ocupada(int x, int y, List<Personaje> heroes, List<Personaje> enemigos) {
        for (Personaje p : heroes) {
            if (p.estaVivo() && p.getPosicionX() == x && p.getPosicionY() == y) {
                return true;
            }
        }
        for (Personaje p : enemigos) {
            if (p.estaVivo() && p.getPosicionX() == x && p.getPosicionY() == y) {
                return true;
            }
        }
        return false;
    }

    private static boolean estaEnCofre(Personaje p) {
        return p.estaVivo() && p.getPosicionX() == COFRE_X && p.getPosicionY() == COFRE_Y;
    }

    private static int sumaVida(List<Personaje> lista) {
        int total = 0;
        for (Personaje p : lista) {
            total += p.getVida();
        }
        return total;
    }

    /** Lee un entero; devuelve -1 si la entrada no es numérica (sin romper el programa). */
    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        if (sc.hasNextInt()) {
            int valor = sc.nextInt();
            sc.nextLine();
            return valor;
        }
        sc.nextLine();
        return -1;
    }

    // =====================================================================
    // SECCIÓN 5: DEPURACIÓN EN INTELLIJ (errores comunes)
    // Para probar un caso: descoméntalo (Ctrl + /), lee el error y vuelve a comentarlo.
    // =====================================================================

    // ---------------------------------------------------------------------
    // ERROR 1: IndexOutOfBoundsException
    //
    // public static void error1() {
    //     char[][] tablero = new char[5][5];
    //     tablero[5][5] = 'G';                 // índice 5 fuera de rango (válidos: 0-4)
    //     List<Personaje> lista = new ArrayList<>();
    //     lista.get(0);                        // lista vacía
    // }
    //
    // CAUSA: acceder a un índice inexistente en la matriz o el ArrayList.
    // CORRECCIÓN: breakpoint condicional antes del acceso; revisar en Variables el
    // índice y lista.size() / matriz.length. En este proyecto se evita con
    // dentroDelTablero(x, y) y validando idx < 1 || idx > lista.size().
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // ERROR 2: ClassCastException / tipos crudos
    //
    // public static void error2() {
    //     Personaje p = new Guerrero("Thorin", 120, 15, 0, 0, 10);
    //     Mago m = (Mago) p;                   // la instancia real es Guerrero
    // }
    //
    // CAUSA: casteo explícito a una subclase que no corresponde a la instancia real.
    // CORRECCIÓN: inspeccionar el objeto en el panel de depuración, usar genéricos
    // explícitos (Inventario<Pocion>) y polimorfismo en lugar de castear.
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // ERROR 3: Error de compilación (instanciación directa de clase abstracta)
    //
    // public static void error3() {
    //     Personaje p = new Personaje("X", 10, 1, 0, 0);
    //     // IntelliJ: 'Personaje' is abstract; cannot be instantiated
    // }
    //
    // CORRECCIÓN: instanciar una subclase concreta: new Guerrero(...) o new Mago(...).
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // 5.2 GUÍA DE DEBUGGING
    // 1. Breakpoints: clic en el margen junto a la línea de realizarAtaqueEspecial()
    //    y en el inicio del do-while de ArenaMain.main.
    // 2. Debug: Shift + F9 (o el ícono del escarabajo verde).
    // 3. F8 = Step Over (siguiente línea); F7 = Step Into (entrar al método, p. ej. defender()).
    // 4. Pestaña Variables: despliega 'heroes' / 'enemigos' para ver nombre, vida y posiciones.
    // 5. Alt + F8 (Evaluate Expression): prueba heroes.get(0).estaVivo() o tablero[2][2].
    // ---------------------------------------------------------------------
}
