package juego;

import java.util.List;
import java.util.Random;

// maneja la lógica principal de la partida. 
// le delega el manejo de la matriz a Tablero y el deshacer a Historial
public class Juego {    
    private static final int TAMANIO_POR_DEFECTO = 4;
    private Tablero tablero;
    private Historial historial;
    private Random random;
    private int proximaFicha;
    
    public Juego() {
        this(TAMANIO_POR_DEFECTO);
    }

    public Juego(int tamanio) {
        if (tamanio < TAMANIO_POR_DEFECTO || tamanio > 10) {
            throw new IllegalArgumentException(
                    "El tamaño del tablero debe estar entre " 
                    + TAMANIO_POR_DEFECTO + " y 10. Valor enviado: " + tamanio);
        }

        this.tablero = new Tablero(tamanio);
        this.historial = new Historial();
        this.random = new Random();
        
        reiniciar();
    }
    
    public boolean terminado() {
        return !tablero.hayMovimientosPosibles();
    }
    
    public void reiniciar() {
        tablero.vaciarTablero();
        historial.vaciar();
        
        List<Posicion> libres = tablero.getCeldasLibres();
        if (libres.size() >= 2) {
            Posicion pos1 = libres.remove(random.nextInt(libres.size()));
            tablero.colocarValor(pos1.getFila(), pos1.getColumna(), 1);
            
            Posicion pos2 = libres.get(random.nextInt(libres.size()));
            tablero.colocarValor(pos2.getFila(), pos2.getColumna(), 2);
        }
        proximaFicha = getValorRandom();
    }
    
    public int getProximaFicha(){
    	return proximaFicha;
    }

    public int getSize() {
        return tablero.getSize();
    }

    public int getValor(int fila, int columna) {
        return tablero.getValor(fila, columna);
    }   

    public boolean isCambio(int fila, int columna) {
        return tablero.isCambio(fila, columna);
    }

    public Integer getPuntos() {
        Integer suma = 0;
        for (int fila = 0; fila < tablero.getSize(); fila++) {
            for (int colu = 0; colu < tablero.getSize(); colu++) {
                int valor = tablero.getValor(fila, colu);
                if (valor > 2) {
                    suma += valor;
                }
            }
        }
        return suma;
    }
    
    public boolean moverDerecha() {
        return procesarMovimiento(Direccion.DERECHA);
    }

    public boolean moverIzquierda() {
        return procesarMovimiento(Direccion.IZQUIERDA);
    }

    public boolean moverArriba() {
        return procesarMovimiento(Direccion.ARRIBA);
    }
    
    public boolean moverAbajo() {
        return procesarMovimiento(Direccion.ABAJO);
    }

    private boolean procesarMovimiento(Direccion direccion) {
        tablero.limpiarCambios();
        historial.guardarEstado(tablero.copiarEstado());

        boolean huboDesplazamiento = false;
        
        if (direccion == Direccion.DERECHA) huboDesplazamiento = tablero.desplazarDerecha();
        else if (direccion == Direccion.IZQUIERDA) huboDesplazamiento = tablero.desplazarIzquierda();
        else if (direccion == Direccion.ARRIBA) huboDesplazamiento = tablero.desplazarArriba();
        else if (direccion == Direccion.ABAJO) huboDesplazamiento = tablero.desplazarAbajo();

        if (huboDesplazamiento) {
            colocarFichaNueva(direccion);
            return true;
        }
        
        deshacer(); 
        return false;
    }

    private void colocarFichaNueva(Direccion direccionMovimiento) {
        List<Posicion> libres = tablero.getCeldasLibresEnBorde(direccionMovimiento);
        if (!libres.isEmpty()) {
            Posicion elegida = libres.get(random.nextInt(libres.size()));
            tablero.colocarValor(elegida.getFila(), elegida.getColumna(), proximaFicha);
            proximaFicha = getValorRandom();
        }
    }
    
    public Direccion jugadaSugerida() {
        Direccion mejorJugada = null;
        int maxPuntos = -1;
        
        int[][] estadoActual = tablero.copiarEstado(); 
        int fichaActual = proximaFicha;
        
        for (Direccion dir : Direccion.values()) {
            boolean movio = false;
            
            if (dir == Direccion.DERECHA) movio = moverDerecha();
            else if (dir == Direccion.IZQUIERDA) movio = moverIzquierda();
            else if (dir == Direccion.ARRIBA) movio = moverArriba();
            else if (dir == Direccion.ABAJO) movio = moverAbajo();
            
            if (movio) {
                if (getPuntos() > maxPuntos) {
                    maxPuntos = getPuntos();
                    mejorJugada = dir;
                }               
                deshacer(); 
            }
            
            // vuelve al tablero como estaba
            tablero.restaurarEstado(estadoActual);
            proximaFicha = fichaActual;
        }
        
        return mejorJugada; // devuelve null si no hay jugadas posibles
    }

    public boolean deshacer() {
        if (puedeDeshacer()) {
            int[][] estadoAnterior = historial.extraerEstadoAnterior();
            tablero.restaurarEstado(estadoAnterior);
            return true;
        }
        return false;
    }
    
    public boolean puedeDeshacer() {
        return historial.puedeDeshacer();
    }
    
    public int getCantidadJugadas() {
        return historial.getCantidadJugadas();
    }

    private int getValorRandom() {
        return random.nextInt(3) + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────┬─────┬─────┬─────┐\n");
        for (int fila = 0; fila < tablero.getSize(); fila++) {
            sb.append("│");
            for (int colu = 0; colu < tablero.getSize(); colu++) {
                int valor = getValor(fila, colu);
                if (valor == 0) {
                    sb.append("     ");
                } else {
                    String strNum = String.format("%3d", valor);                	
                    sb.append(String.format(" " + strNum + " "));
                }
                sb.append("│");
            }
            sb.append("\n");
            if (fila < tablero.getSize() - 1) {
                sb.append("├─────┼─────┼─────┼─────┤\n");
            }
        }
        sb.append("└─────┴─────┴─────┴─────┘");
        return sb.toString();
    }

    public String toStringCambios() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌─────┬─────┬─────┬─────┐\n");
        for (int fila = 0; fila < tablero.getSize(); fila++) {
            sb.append("│");
            for (int colu = 0; colu < tablero.getSize(); colu++) {
                if (isCambio(fila, colu)) {
                    sb.append(String.format("  X  "));
                } else {
                    sb.append("     ");
                }
                sb.append("│");
            }
            sb.append("\n");
            if (fila < tablero.getSize() - 1) {
                sb.append("├─────┼─────┼─────┼─────┤\n");
            }
        }
        sb.append("└─────┴─────┴─────┴─────┘");
        return sb.toString();
    }
}