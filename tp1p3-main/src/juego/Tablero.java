package juego;

import java.util.ArrayList;
import java.util.List;

// es el modelo de la grilla. sabe cómo mover y fusionar las fichas 
// pero no tiene idea de los puntos, turnos o el historial

public class Tablero {
    private int tamanio;
    private int[][] matriz;
    private boolean[][] matrizCambios;

    public Tablero(int tamanio) {
        this.tamanio = tamanio;
        this.matriz = new int[tamanio][tamanio];
        this.matrizCambios = new boolean[tamanio][tamanio];
    }

    public int getSize() {
        return this.tamanio;
    }

    public int getValor(int fila, int columna) {
        validarCoordenadas(fila, columna);
        return this.matriz[fila][columna];
    }

    public boolean isCambio(int fila, int columna) {
        validarCoordenadas(fila, columna);
        return this.matrizCambios[fila][columna];
    }

    public boolean isVacio(int fila, int columna) {
        validarCoordenadas(fila, columna);
        return (getValor(fila, columna) == 0);
    }

    public void colocarValor(int fila, int columna, int valor) {
        this.matriz[fila][columna] = valor;
        marcarCambio(fila, columna);
    }

    // vacía los valores reales de la matriz
    public void vaciarTablero() {
        for (int fila = 0; fila < this.getSize(); fila++) {
            for (int colu = 0; colu < this.getSize(); colu++) {
                this.matriz[fila][colu] = 0;
                desmarcarCambio(fila, colu);
            }
        }
    }

    public void limpiarCambios() {
        for (int fila = 0; fila < this.getSize(); fila++) {
            for (int colu = 0; colu < this.getSize(); colu++) {
                desmarcarCambio(fila, colu);
            }
        }
    }

    public int[][] copiarEstado() {
        int[][] copia = new int[tamanio][tamanio];
        for (int i = 0; i < tamanio; i++) {
            System.arraycopy(matriz[i], 0, copia[i], 0, tamanio);
        }
        return copia;
    }

    public void restaurarEstado(int[][] estadoAnterior) {
        for (int i = 0; i < tamanio; i++) {
            System.arraycopy(estadoAnterior[i], 0, this.matriz[i], 0, tamanio);
        }
    }

    // chequea tanto celdas vacías como adyacencias combinables
    public boolean hayMovimientosPosibles() {
        for (int f = 0; f < tamanio; f++) {
            for (int c = 0; c < tamanio; c++) {
                if (isVacio(f, c)) return true;
                
                if (c < tamanio - 1 && sonCombinables(getValor(f, c), getValor(f, c + 1))) {
                    return true;
                }
                if (f < tamanio - 1 && sonCombinables(getValor(f, c), getValor(f + 1, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Posicion> getCeldasLibres() {
        List<Posicion> libres = new ArrayList<>();
        for (int f = 0; f < tamanio; f++) {
            for (int c = 0; c < tamanio; c++) {
                if (isVacio(f, c)) libres.add(new Posicion(f, c));
            }
        }
        return libres;
    }

    // Retorna celdas libres solo en el borde donde debe ingresar la nueva ficha
    public List<Posicion> getCeldasLibresEnBorde(Direccion direccionMovimiento) {
        List<Posicion> libres = new ArrayList<>();
        int limite = tamanio - 1;
        
        for (int i = 0; i < tamanio; i++) {
            // si muevo a la derecha, la ficha entra por la izquierda (columna 0)
            if (direccionMovimiento == Direccion.DERECHA && isVacio(i, 0)) {
                libres.add(new Posicion(i, 0));
            }
            // si muevo a la izquierda, la ficha entra por la derecha (columna limite)
            if (direccionMovimiento == Direccion.IZQUIERDA && isVacio(i, limite)) {
                libres.add(new Posicion(i, limite));
            }
            // si muevo abajo, la ficha entra por arriba (fila 0)
            if (direccionMovimiento == Direccion.ABAJO && isVacio(0, i)) {
                libres.add(new Posicion(0, i));
            }
            // si muevo arriba, la ficha entra por abajo (fila limite)
            if (direccionMovimiento == Direccion.ARRIBA && isVacio(limite, i)) {
                libres.add(new Posicion(limite, i));
            }
        }
        return libres;
    }

    public boolean desplazarDerecha() {
        boolean cambio = false;
        for (int colu = this.getSize() - 2; colu >= 0; colu--) {
            for (int fila = 0; fila < this.getSize(); fila++) {
                if (!isVacio(fila, colu)) {
                    if (isVacio(fila, colu + 1)) {
                        moverValor(fila, colu, fila, colu + 1);
                        matriz[fila][colu] = 0;
                        marcarCambio(fila, colu, fila, colu + 1);
                        cambio = true;
                    } else {
                        int valorOrigen = getValor(fila, colu);
                        int valorDestino = getValor(fila, colu + 1);
                        if (sonCombinables(valorOrigen, valorDestino)) {
                            matriz[fila][colu + 1] = valorOrigen + valorDestino;
                            matriz[fila][colu] = 0;
                            marcarCambio(fila, colu, fila, colu + 1);
                            cambio = true;
                        }
                    }
                }
            }
        }
        return cambio;
    }

    public boolean desplazarIzquierda() {
        boolean cambio = false;
        for (int colu = 1; colu < this.getSize(); colu++) {
            for (int fila = 0; fila < this.getSize(); fila++) {
                if (!isVacio(fila, colu)) {
                    if (isVacio(fila, colu - 1)) {
                        moverValor(fila, colu, fila, colu - 1);
                        matriz[fila][colu] = 0;
                        marcarCambio(fila, colu, fila, colu - 1);
                        cambio = true;
                    } else {
                        int valorOrigen = getValor(fila, colu);
                        int valorDestino = getValor(fila, colu - 1);
                        if (sonCombinables(valorOrigen, valorDestino)) {
                            matriz[fila][colu - 1] = valorOrigen + valorDestino;
                            matriz[fila][colu] = 0;
                            marcarCambio(fila, colu, fila, colu - 1);
                            cambio = true;
                        }
                    }
                }
            }
        }
        return cambio;
    }

    public boolean desplazarArriba() {
        boolean cambio = false;
        for (int colu = 0; colu < this.getSize(); colu++) {
            for (int fila = 1; fila < this.getSize(); fila++) {
                if (!isVacio(fila, colu)) {
                    if (isVacio(fila - 1, colu)) {
                        moverValor(fila, colu, fila - 1, colu);
                        matriz[fila][colu] = 0;
                        marcarCambio(fila, colu, fila - 1, colu);
                        cambio = true;
                    } else {
                        int valorOrigen = getValor(fila, colu);
                        int valorDestino = getValor(fila - 1, colu);
                        if (sonCombinables(valorOrigen, valorDestino)) {
                            matriz[fila - 1][colu] = valorOrigen + valorDestino;
                            matriz[fila][colu] = 0;
                            marcarCambio(fila, colu, fila - 1, colu);
                            cambio = true;
                        }
                    }
                }
            }
        }
        return cambio;
    }

    public boolean desplazarAbajo() {
        boolean cambio = false;
        for (int colu = 0; colu < this.getSize(); colu++) {
            for (int fila = this.getSize() - 2; fila >= 0; fila--) {
                if (!isVacio(fila, colu)) {
                    if (isVacio(fila + 1, colu)) {
                        moverValor(fila, colu, fila + 1, colu);
                        matriz[fila][colu] = 0;
                        marcarCambio(fila, colu, fila + 1, colu);
                        cambio = true;
                    } else {
                        int valorOrigen = getValor(fila, colu);
                        int valorDestino = getValor(fila + 1, colu);
                        if (sonCombinables(valorOrigen, valorDestino)) {
                            matriz[fila + 1][colu] = valorOrigen + valorDestino;
                            matriz[fila][colu] = 0;
                            marcarCambio(fila, colu, fila + 1, colu);
                            cambio = true;
                        }
                    }
                }
            }
        }
        return cambio;
    }

    private boolean sonCombinables(int valor1, int valor2) {
        if (valor1 == 1 && valor2 == 2) return true;
        if (valor1 == 2 && valor2 == 1) return true;
        if (valor1 == valor2 && valor1 % 3 == 0) return true;
        return false;
    }

    private void moverValor(int filaOrigen, int coluOrigen, int filaDest, int coluDest) {
        int valor = getValor(filaOrigen, coluOrigen);
        matriz[filaDest][coluDest] = valor;
    }

    private void marcarCambio(int fila, int columna) {
        this.matrizCambios[fila][columna] = true;
    }

    private void desmarcarCambio(int fila, int columna) {
        this.matrizCambios[fila][columna] = false;
    }

    private void marcarCambio(int filaOrigen, int coluOrigen, int filaDest, int coluDest) {
        marcarCambio(filaOrigen, coluOrigen);
        marcarCambio(filaDest, coluDest);
    }

    private void validarCoordenadas(int fila, int columna) {
        if (fila < 0 || fila >= this.getSize()) {
            throw new IllegalArgumentException("Fila fuera de rango.");
        }
        if (columna < 0 || columna >= this.getSize()) {
            throw new IllegalArgumentException("Columna fuera de rango.");
        }
    }
}