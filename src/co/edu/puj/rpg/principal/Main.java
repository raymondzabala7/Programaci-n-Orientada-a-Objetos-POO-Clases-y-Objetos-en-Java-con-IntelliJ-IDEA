package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

public class Main {

    private static final int CURACION_POCION = 50;
    private static final double COSTO_POCION = 25.0;
    private static final String[] NOMBRES_ENEMIGOS =
            {"Goblin", "Orco", "Esqueleto", "Troll", "Dragón"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        int numeroCombate = 0;
        boolean continuar;

        System.out.println("=== ARENA DE HÉROES ===");

        do {
            numeroCombate++;
            Enemigo enemigo = crearEnemigo(heroe.getNivel(), numeroCombate);
            combatir(sc, heroe, enemigo);

            if (!heroe.isEstaVivo()) {
                System.out.printf("%n%s ha caído en la arena. FIN DEL JUEGO.%n", heroe.getNombre());
                continuar = false;
            } else {
                continuar = preguntarOtroCombate(sc);
            }
        } while (continuar);

        System.out.println("¡Gracias por jugar!");
        sc.close();
    }

    // ---------------------------------------------------------------
    // Sección 3: combate con do-while + switch + validaciones if-else
    // ---------------------------------------------------------------
    private static void combatir(Scanner sc, Personaje heroe, Enemigo enemigo) {
        StringBuilder historial = new StringBuilder();
        historial.append(String.format("== Combate contra %s (nivel %d) ==%n",
                enemigo.getNombre(), enemigo.getNivel()));
        int ronda = 0;
        boolean huyo = false;

        System.out.printf("%n¡Aparece un %s nivel %d!%n", enemigo.getNombre(), enemigo.getNivel());

        do {
            ronda++;
            System.out.printf("%n--- Ronda %d ---%n", ronda);
            System.out.printf("%s: %d/%d PV | %s: %d/%d PV%n",
                    heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                    enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
            System.out.println("1. Atacar");
            System.out.println("2. Usar Poción");
            System.out.println("3. Ver Estado");
            System.out.println("4. Huir");
            System.out.println("5. Tienda");

            int opcion = leerOpcion(sc);
            boolean turnoConsumido = false;

            if (opcion < 1 || opcion > 5) {
                System.out.println("Opción inválida. Elige un número del 1 al 5.");
                historial.append(String.format("[Ronda %d] Opción inválida, turno sin efecto.%n", ronda));
            } else {
                switch (opcion) {
                    case 1: {
                        int dano = heroe.atacar(enemigo);
                        String tipo = heroe.isUltimoGolpeCritico() ? "¡GOLPE CRÍTICO!" : "golpe normal";
                        historial.append(String.format("[Ronda %d] %s ataca a %s: %d de daño (%s).%n",
                                ronda, heroe.getNombre(), enemigo.getNombre(), dano, tipo));
                        turnoConsumido = true;
                        break;
                    }
                    case 2: {
                        if (heroe.gastarPocion()) {
                            int antes = heroe.getPuntosVida();
                            heroe.usarPocion(CURACION_POCION);
                            int curado = heroe.getPuntosVida() - antes;
                            historial.append(String.format("[Ronda %d] %s usa una poción y recupera %d PV.%n",
                                    ronda, heroe.getNombre(), curado));
                            turnoConsumido = true;
                        } else {
                            System.out.println("No te quedan pociones. Visita la tienda.");
                            historial.append(String.format("[Ronda %d] Intento de usar poción sin inventario.%n", ronda));
                        }
                        break;
                    }
                    case 3:
                        mostrarEstado(heroe);
                        historial.append(String.format("[Ronda %d] Se consultó el estado.%n", ronda));
                        break;
                    case 4:
                        huyo = true;
                        historial.append(String.format("[Ronda %d] %s huye del combate.%n",
                                ronda, heroe.getNombre()));
                        break;
                    case 5:
                        abrirTienda(sc, heroe, historial, ronda);
                        break;
                    default:
                        break;
                }
            }

            // Turno del enemigo si el héroe actuó y el enemigo sigue en pie
            if (turnoConsumido && enemigo.isEstaVivo()) {
                int danoRecibido = enemigo.atacar(heroe);
                historial.append(String.format("[Ronda %d] %s contraataca: %d de daño a %s.%n",
                        ronda, enemigo.getNombre(), danoRecibido, heroe.getNombre()));
                if (!heroe.isEstaVivo()) {
                    historial.append(String.format("[Ronda %d] %s ha sido derrotado.%n",
                            ronda, heroe.getNombre()));
                }
            }
            if (!enemigo.isEstaVivo()) {
                historial.append(String.format("[Ronda %d] %s ha sido derrotado.%n",
                        ronda, enemigo.getNombre()));
            }

            // Sección 3.2: historial consolidado al final de cada ronda
            System.out.println("\n>>> HISTORIAL DE BATALLA <<<");
            System.out.print(historial);

            if (huyo) {
                break; // Huir rompe el bucle
            }
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo());

        // Recompensas por victoria (Reto 2)
        if (!enemigo.isEstaVivo() && heroe.isEstaVivo()) {
            int xp = enemigo.getRecompensaXp();
            double botin = enemigo.getRecompensaOro();
            heroe.setOro(heroe.getOro() + botin);
            int niveles = heroe.ganarExperiencia(xp);
            System.out.printf("%n¡Victoria! Ganas %d XP y %.2f de oro.%n", xp, botin);
            if (niveles > 0) {
                System.out.printf("¡SUBES DE NIVEL! Ahora eres nivel %d (vida máx. %d, ataque %d).%n",
                        heroe.getNivel(), heroe.getVidaMaxima(), heroe.getPuntosAtaque());
            }
        }
    }

    // ---------------------------------------------------------------
    // Reto 1: tienda
    // ---------------------------------------------------------------
    private static void abrirTienda(Scanner sc, Personaje heroe, StringBuilder historial, int ronda) {
        System.out.println("\n=== TIENDA ===");
        System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
        System.out.printf("1. Poción Grande de Salud (+%d PV) - %.2f de oro%n", CURACION_POCION, COSTO_POCION);
        System.out.println("2. Salir");

        int opcion = leerOpcion(sc);

        if (opcion == 1) {
            double nuevoSaldo = heroe.getOro() - COSTO_POCION;
            // El mutador valida: si el saldo sería negativo, rechaza y devuelve false
            if (heroe.setOro(nuevoSaldo)) {
                heroe.agregarPocion();
                System.out.printf("Compraste una Poción Grande. Saldo: %.2f | Pociones: %d%n",
                        heroe.getOro(), heroe.getPociones());
                historial.append(String.format("[Ronda %d] Compra: Poción Grande de Salud (-%.2f oro).%n",
                        ronda, COSTO_POCION));
            } else {
                System.out.println("Compra rechazada: no se entregó la poción.");
                historial.append(String.format("[Ronda %d] Compra rechazada por saldo insuficiente.%n", ronda));
            }
        } else if (opcion == 2) {
            System.out.println("Sales de la tienda.");
        } else {
            System.out.println("Opción inválida en la tienda.");
        }
    }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------
    private static void mostrarEstado(Personaje heroe) {
        System.out.println("\n------ ESTADO ------");
        System.out.printf("Nombre : %s%n", heroe.getNombre());
        System.out.printf("Nivel  : %d (XP %d/%d)%n",
                heroe.getNivel(), heroe.getExperiencia(), heroe.getExperienciaParaSubir());
        System.out.printf("Vida   : %d/%d%n", heroe.getPuntosVida(), heroe.getVidaMaxima());
        System.out.printf("Ataque : %d%n", heroe.getPuntosAtaque());
        System.out.printf("Oro    : %.2f%n", heroe.getOro());
        System.out.printf("Pociones: %d%n", heroe.getPociones());
        System.out.printf("Vivo   : %s%n", heroe.isEstaVivo() ? "Sí" : "No");
    }

    private static Enemigo crearEnemigo(int nivelHeroe, int numeroCombate) {
        String nombre = NOMBRES_ENEMIGOS[(numeroCombate - 1) % NOMBRES_ENEMIGOS.length];
        int nivel = nivelHeroe;
        int vida = 40 + 15 * nivel;
        int ataque = 6 + 2 * nivel;
        return new Enemigo(nombre, nivel, vida, ataque);
    }

    /** Lee un entero; devuelve -1 si la entrada no es numérica (sin romper el programa). */
    private static int leerOpcion(Scanner sc) {
        System.out.print("Elige una opción: ");
        if (sc.hasNextInt()) {
            int valor = sc.nextInt();
            sc.nextLine();
            return valor;
        }
        sc.nextLine();
        return -1;
    }

    private static boolean preguntarOtroCombate(Scanner sc) {
        while (true) {
            System.out.print("\n¿Otro combate? (1 = Sí, 2 = No): ");
            int r = -1;
            if (sc.hasNextInt()) {
                r = sc.nextInt();
            }
            sc.nextLine();
            if (r == 1) {
                return true;
            } else if (r == 2) {
                return false;
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }
}

// =====================================================================
// SECCIÓN 5: DEPURACIÓN DE ENCAPSULAMIENTO (casos de estudio)
// Para probar un caso: selecciónalo y pulsa Ctrl + / para descomentarlo,
// lee el mensaje de IntelliJ y vuelve a comentarlo.
// =====================================================================

// ---------------------------------------------------------------------
// CASO A: Acceso directo no autorizado (ERROR DE COMPILADOR)
// Error esperado: 'puntosVida' has private access in
//                 'co.edu.puj.rpg.modelo.Personaje'
//
// public static void casoA() {
//     Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
//     heroe.puntosVida = 200;
//     System.out.println("Vida: " + heroe.puntosVida);
// }
//
// CAUSA RAÍZ: puntosVida es private y Main está en otro paquete.
// SOLUCIÓN: usar heroe.setPuntosVida(200) y heroe.getPuntosVida().
// ---------------------------------------------------------------------

// ---------------------------------------------------------------------
// CASO B: Bypassing de validaciones mediante constructor defectuoso
// (va en la clase Personaje, no en Main; compila pero deja un estado
// incoherente)
//
// public Personaje(int puntosVida) {
//     this.puntosVida = -50;   // salta el setter y su validación
//     this.estaVivo = true;    // vida negativa pero figura como vivo
// }
//
// CAUSA RAÍZ: el constructor asigna directo al atributo y evita las
// reglas de negocio.
// SOLUCIÓN: llamar a setPuntosVida(valor) dentro del constructor, que
// fuerza puntosVida = 0 y estaVivo = false para valores <= 0.
// ---------------------------------------------------------------------

// ---------------------------------------------------------------------
// CASO C: Fuga de estado y asignación fuera de límites
//
// public static void ejecutarAccion(Personaje heroe) {
//     heroe.setPuntosVida(999999); // Excede vidaMaxima
//     heroe.setPuntosVida(-100);   // Vida negativa sin actualizar estaVivo
// }
//
// Con el setter validado de Personaje, la vida queda limitada a
// vidaMaxima en el primer caso, y en el segundo queda en 0 con
// estaVivo = false. Para ver el resultado, llama a ejecutarAccion(heroe)
// desde main() e imprime heroe.getPuntosVida() y heroe.isEstaVivo().
// ---------------------------------------------------------------------

// ---------------------------------------------------------------------
// 6.2 (punto 3): Pruebas manuales con valores límite (-10, 0, 100000)
// Para ejecutarlas: descomenta el método y llama a probarLimites()
// desde main(). Vuelve a comentarlo antes de entregar.
//
// public static void probarLimites() {
//     int[] limites = {-10, 0, 100000};
//
//     System.out.println("-- setPuntosVida --");
//     for (int v : limites) {
//         Personaje p = new Personaje("Test", 100, 15, 50.0);
//         p.setPuntosVida(v);
//         System.out.printf("setPuntosVida(%d) -> vida=%d, vivo=%b%n",
//                 v, p.getPuntosVida(), p.isEstaVivo());
//     }
//
//     System.out.println("-- setOro --");
//     for (int v : limites) {
//         Personaje p = new Personaje("Test", 100, 15, 50.0);
//         boolean ok = p.setOro(v);
//         System.out.printf("setOro(%d) -> aceptado=%b, oro=%.2f%n",
//                 v, ok, p.getOro());
//     }
//
//     // setNivel() es protected: desde este paquete no se puede llamar
//     // directamente, así que se prueba por la vía pública.
//     System.out.println("-- nivel (vía ganarExperiencia) --");
//     for (int v : limites) {
//         Personaje p = new Personaje("Test", 100, 15, 50.0);
//         int ganados = p.ganarExperiencia(v);
//         System.out.printf("ganarExperiencia(%d) -> niveles=%d, nivel=%d%n",
//                 v, ganados, p.getNivel());
//     }
// }
// ---------------------------------------------------------------------