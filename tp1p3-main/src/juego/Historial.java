package juego;

import java.util.Stack;

// maneja la pila para poder deshacer jugadas. 
// sirve para que Juego no tenga que lidiar con el Stack directamente

public class Historial {
    private Stack<int[][]> estados;

    public Historial() {
        this.estados = new Stack<>();
    }

    public void guardarEstado(int[][] estado) {
        this.estados.push(estado);
    }

    public int[][] extraerEstadoAnterior() {
        if (puedeDeshacer()) {
            return this.estados.pop();
        }
        return null;
    }

    public boolean puedeDeshacer() {
        return !this.estados.isEmpty();
    }

    public void vaciar() {
        this.estados.clear();
    }

    public int getCantidadJugadas() {
        return this.estados.size();
    }
}