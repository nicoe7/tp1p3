package juego;

import java.util.Scanner;

public class MainConsola {

    public static void main(String[] args) {
        Juego juego = new Juego(); 
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        System.out.println("=========================================");
        System.out.println("        ¡BIENVENIDO A THREES!            ");
        System.out.println("=========================================");

        // El loop sigue mientras el juego no termine y el usuario no ponga salir
        while (!juego.terminado() && !salir) {
            System.out.println("\n" + juego.toString());
            System.out.println("Puntos: " + juego.getPuntos() + " | Historial: " + juego.getCantidadJugadas() + " jugadas previas.");
            System.out.println("-----------------------------------------");
            System.out.println("Controles: [W] Arriba | [S] Abajo | [A] Izquierda | [D] Derecha");
            System.out.println("Extras   : [Z] Deshacer | [H] Sugerir Jugada | [Q] Salir");
            System.out.print("Ingresá tu acción: ");
            
            // Leemos la consola y lo pasamos a mayúsculas para no renegar
            String input = scanner.nextLine().toUpperCase();
            boolean seMovio = false;

            switch (input) {
                case "W":
                    seMovio = juego.moverArriba();
                    break;
                case "S":
                    seMovio = juego.moverAbajo();
                    break;
                case "A":
                    seMovio = juego.moverIzquierda();
                    break;
                case "D":
                    seMovio = juego.moverDerecha();
                    break;
                case "Z":
                    if (juego.deshacer()) {
                        System.out.println(">>> Volviste un paso atrás.");
                    } else {
                        System.out.println(">>> ¡No hay jugadas en el historial para deshacer!");
                    }
                    break;
                case "H":
                    Direccion sugerencia = juego.jugadaSugerida();
                    if (sugerencia != null) {
                        System.out.println(">>> Te sugiero mover hacia: " + sugerencia);
                    } else {
                        System.out.println(">>> Mmm... no veo ninguna jugada que te sume puntos ahora mismo.");
                    }
                    break;
                case "Q":
                    salir = true;
                    break;
                default:
                    System.out.println(">>> Tecla no válida. Usá W, A, S, D, Z, H o Q.");
                    break;
            }

            // Si apretó una tecla de movimiento pero el tablero no cambió
            if (input.matches("[WSAD]") && !seMovio) {
                System.out.println(">>> Movimiento bloqueado. No se puede desplazar hacia ahí.");
            }
        }

        // Chequeamos por qué salimos del loop
        if (juego.terminado()) {
            System.out.println("\n" + juego.toString());
            System.out.println("=========================================");
            System.out.println("              ¡FIN DEL JUEGO!            ");
            System.out.println("      Ya no hay más movimientos posibles ");
            System.out.println("      Puntaje final: " + juego.getPuntos());
            System.out.println("=========================================");
        } else {
            System.out.println("\nPartida cancelada. ¡Nos vemos!");
        }

        scanner.close();
    }
}