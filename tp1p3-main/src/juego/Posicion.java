package juego;

// guarda las coordenadas. 
//se hace para no depender de arrays int[] al pasar posiciones y subir la cohesión.

public class Posicion {
    private int fila;
    private int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}