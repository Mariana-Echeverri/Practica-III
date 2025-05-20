package main;

import modelo.Jugada;
import modelo.Partida;
import modelo.Turno;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class Arbol {

    public Nodo partida;
    
    public Arbol() {
        // Inicializar con la raíz "Partida"
        partida = new Nodo(1);
        partida.jugada = "Partida";
    }

    /**
     * Construye un árbol a partir de una partida de ajedrez
     * @param partida La partida de ajedrez a representar
     */
    public void construirDesdePartida(Partida partida) {
        if (partida == null) return;
        
        // Reiniciar el árbol
        this.partida = new Nodo(1);
        this.partida.jugada = "Partida";
        
        List<Jugada> jugadas = new ArrayList<>();
        
        // Extraer todas las jugadas de la partida
        for (Turno turno : partida.getTurnos()) {
            if (turno.getJugadaBlanca() != null) {
                jugadas.add(turno.getJugadaBlanca());
            }
            if (turno.getJugadaNegra() != null) {
                jugadas.add(turno.getJugadaNegra());
            }
        }
        
        // Construir el árbol binario a partir de las jugadas
        construirArbolBinario(jugadas);
    }
    
    /**
     * Construye un árbol binario a partir de una lista de jugadas
     * @param jugadas Lista de jugadas
     */
    private void construirArbolBinario(List<Jugada> jugadas) {
        if (jugadas.isEmpty()) return;
        
        // Insertar cada jugada en el árbol
        for (int i = 0; i < jugadas.size(); i++) {
            insertarEnArbolBinario(i + 2, jugadas.get(i).getNotacion());
        }
    }
    
    /**
     * Inserta un nodo en el árbol binario en la posición correcta
     * @param indice Índice del nodo
     * @param contenido Contenido del nodo
     */
    private void insertarEnArbolBinario(int indice, Object contenido) {
        Nodo nuevoNodo = new Nodo(indice);
        nuevoNodo.jugada = contenido;
        
        if (partida == null) {
            partida = nuevoNodo;
            return;
        }
        
        // Encontrar la posición correcta usando el algoritmo de árbol binario completo
        insertarNodoEnPosicion(partida, nuevoNodo);
    }
    
    /**
     * Inserta un nodo en la posición correcta del árbol binario
     * @param actual Nodo actual
     * @param nuevo Nodo a insertar
     */
    private void insertarNodoEnPosicion(Nodo actual, Nodo nuevo) {
        // Usamos una cola para hacer un recorrido por niveles
        Queue<Nodo> cola = new LinkedList<>();
        cola.add(actual);
        
        while (!cola.isEmpty()) {
            Nodo nodo = cola.poll();
            
            // Si no tiene hijo izquierdo, insertamos ahí
            if (nodo.izquierda == null) {
                nodo.izquierda = nuevo;
                nuevo.parent = nodo;
                return;
            } 
            // Si no tiene hijo derecho, insertamos ahí
            else if (nodo.derecha == null) {
                nodo.derecha = nuevo;
                nuevo.parent = nodo;
                return;
            }
            // Si tiene ambos hijos, seguimos buscando
            else {
                cola.add(nodo.izquierda);
                cola.add(nodo.derecha);
            }
        }
    }

    /**
     * Recorre el árbol en orden
     * @param n Nodo inicial
     */
    public void recorrer(Nodo n) {
        if (n != null) {
            recorrer(n.izquierda);
            System.out.println("Indice " + n.llave + " jugada " + n.jugada);
            recorrer(n.derecha);
        }
    }

    /**
     * Obtiene una lista de todas las jugadas en el árbol
     * @return Lista de jugadas
     */
    public List<String> obtenerJugadas() {
        List<String> jugadas = new ArrayList<>();
        recolectarJugadas(partida, jugadas);
        return jugadas;
    }

    private void recolectarJugadas(Nodo nodo, List<String> jugadas) {
        if (nodo != null) {
            jugadas.add(nodo.jugada.toString());
            recolectarJugadas(nodo.izquierda, jugadas);
            recolectarJugadas(nodo.derecha, jugadas);
        }
    }

    public class Nodo {
        public Nodo parent;
        public Nodo derecha;
        public Nodo izquierda;
        public int llave;
        public Object jugada;

        public Nodo(int indice) {
            llave = indice;
            derecha = null;
            izquierda = null;
            parent = null;
            jugada = null;
        }
    }
}
