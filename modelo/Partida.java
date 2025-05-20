package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Almacena una partida completa de ajedrez
 * Esta clase organiza todos los movimientos por turnos
 */
public class Partida {
    //-------------------------
    // CONSTANTES
    //-------------------------

    // Número inicial de turnos reservados en memoria
    private static final int CAPACIDAD_INICIAL = 40;

    //-------------------------
    // PROPIEDADES
    //-------------------------

    // Contador de turnos almacenados
    private int numeroTurnos;

    // Arreglo para almacenar los turnos
    private Turno[] secuenciaTurnos;

    //-------------------------
    // CONSTRUCTOR
    //-------------------------

    /**
     * Crea una nueva partida vacía
     */
    public Partida() {
        // Inicializar contador a cero
        this.numeroTurnos = 0;

        // Crear el arreglo con capacidad para varios turnos
        this.secuenciaTurnos = new Turno[CAPACIDAD_INICIAL];
    }

    //-------------------------
    // MÉTODOS PÚBLICOS
    //-------------------------

    /**
     * Añade un nuevo turno a la partida
     */
    public void agregarTurno(Turno nuevoTurno) {
        // Comprobar si necesitamos más espacio
        if (numeroTurnos >= secuenciaTurnos.length) {
            ampliarCapacidad();
        }

        // Guardar el turno en la siguiente posición
        secuenciaTurnos[numeroTurnos] = nuevoTurno;

        // Incrementar el contador
        numeroTurnos++;
    }

    /**
     * Devuelve todos los turnos de la partida
     */
    public Turno[] obtenerTurnos() {
        // Crear un nuevo arreglo del tamaño exacto
        Turno[] resultado = new Turno[numeroTurnos];

        // Copiar los turnos al nuevo arreglo
        for (int i = 0; i < numeroTurnos; i++) {
            resultado[i] = secuenciaTurnos[i];
        }

        return resultado;
    }

    /**
     * Obtiene la cantidad de turnos en la partida
     */
    public int contarTurnos() {
        return numeroTurnos;
    }

    /**
     * Obtiene un turno específico por su índice
     */
    public Turno obtenerTurno(int indice) {
        // Verificar que el índice sea válido
        if (indice < 0 || indice >= numeroTurnos) {
            throw new IllegalArgumentException("Índice de turno fuera de rango: " + indice);
        }

        return secuenciaTurnos[indice];
    }

    /**
     * Método compatible con el original para obtener turnos como lista
     */
    public List<Turno> getTurnos() {
        List<Turno> lista = new ArrayList<>();

        // Convertir array a lista
        for (int i = 0; i < numeroTurnos; i++) {
            lista.add(secuenciaTurnos[i]);
        }

        return lista;
    }

    /**
     * Convierte la partida a texto
     */
    @Override
    public String toString() {
        // Si no hay movimientos
        if (numeroTurnos == 0) {
            return "Partida sin movimientos";
        }

        // Crear un constructor de texto
        StringBuilder texto = new StringBuilder();
        texto.append("Partida con ").append(numeroTurnos).append(" turnos: ");

        // Añadir cada turno
        for (int i = 0; i < numeroTurnos; i++) {
            if (i > 0) {
                texto.append(" | ");
            }
            texto.append(secuenciaTurnos[i]);
        }

        return texto.toString();
    }

    //-------------------------
    // MÉTODOS PRIVADOS
    //-------------------------

    /**
     * Amplía la capacidad del arreglo cuando se llena
     */
    private void ampliarCapacidad() {
        // Crear un nuevo arreglo más grande
        Turno[] nuevoArreglo = new Turno[secuenciaTurnos.length * 2];

        // Copiar todos los elementos al nuevo arreglo
        for (int i = 0; i < secuenciaTurnos.length; i++) {
            nuevoArreglo[i] = secuenciaTurnos[i];
        }

        // Reemplazar el arreglo antiguo con el nuevo
        secuenciaTurnos = nuevoArreglo;
    }
}
