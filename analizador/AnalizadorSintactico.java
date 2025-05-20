package analizador;

import modelo.Jugada;
import modelo.Partida;
import modelo.ResultadoAnalisis;
import modelo.Turno;
import util.ValidadorSAN;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase analiza texto de partidas de ajedrez en formato SAN
 */
public class AnalizadorSintactico {

    // Expresión que busca movimientos en formato: número. jugadaBlanca jugadaNegra
    private String expresionRegular = "(\\d+)\\.\\s*([A-Za-z0-9\\-O\\+#x=]+)(?:\\s+([A-Za-z0-9\\-O\\+#x=]+))?";
    private Pattern buscadorDeTurnos;

    /**
     * Constructor que prepara el analizador
     */
    public AnalizadorSintactico() {
        this.buscadorDeTurnos = Pattern.compile(expresionRegular);
    }

    /**
     * Procesa el texto de una partida de ajedrez
     */
    public ResultadoAnalisis analizar(String textoAjedrez) {
        // Crear una partida nueva
        Partida nuevaPartida = new Partida();

        try {
            // Buscar todos los turnos en el texto
            List<Turno> turnosEncontrados = buscarTurnos(textoAjedrez);

            // Verificar que haya al menos un turno
            if (turnosEncontrados.size() == 0) {
                return new ResultadoAnalisis("No se encontró ningún turno en el texto");
            }

            // Revisar cada turno y agregarlo a la partida
            for (Turno t : turnosEncontrados) {
                comprobarTurno(t);
                nuevaPartida.agregarTurno(t);
            }

            // Devolver resultado exitoso con la cantidad de turnos
            return new ResultadoAnalisis(nuevaPartida.contarTurnos());

        } catch (Exception error) {
            // Si hay algún error, devolver un resultado fallido
            return new ResultadoAnalisis(error.getMessage());
        }
    }

    /**
     * Encuentra todos los turnos en el texto de la partida
     */
    private List<Turno> buscarTurnos(String texto) {
        // Lista para guardar los turnos encontrados
        List<Turno> listaTurnos = new ArrayList<>();

        // Buscar en el texto usando la expresión regular
        Matcher buscador = buscadorDeTurnos.matcher(texto);

        // Por cada coincidencia encontrada
        while (buscador.find()) {
            // Extraer información del turno
            String numeroComoTexto = buscador.group(1);
            String movimientoBlancas = buscador.group(2).trim();
            String movimientoNegras = buscador.group(3);

            // Convertir el número de texto a entero
            int numero = Integer.parseInt(numeroComoTexto);

            // Crear un nuevo turno
            Turno nuevoTurno = new Turno(numero);

            // Agregar el movimiento de las blancas
            nuevoTurno.setJugadaBlanca(new Jugada(movimientoBlancas));

            // Agregar el movimiento de las negras si existe
            if (movimientoNegras != null && !movimientoNegras.isEmpty()) {
                nuevoTurno.setJugadaNegra(new Jugada(movimientoNegras.trim()));
            }

            // Agregar el turno a la lista
            listaTurnos.add(nuevoTurno);
        }

        return listaTurnos;
    }

    /**
     * Verifica que un turno sea válido según las reglas
     */
    private void comprobarTurno(Turno turno) throws Exception {
        // El número del turno debe ser positivo
        if (turno.obtenerNumero() <= 0) {
            throw new Exception("El turno tiene un número inválido: " + turno.obtenerNumero());
        }

        // Debe tener movimiento de blancas
        Jugada movBlancas = turno.getJugadaBlanca();
        if (movBlancas == null || movBlancas.getNotacion().isEmpty()) {
            throw new Exception("El turno " + turno.obtenerNumero() + " no tiene movimiento de blancas");
        }

        // El movimiento de blancas debe ser válido
        String notacionBlancas = movBlancas.getNotacion();
        if (!ValidadorSAN.esJugadaValida(notacionBlancas)) {
            throw new Exception("En el turno " + turno.obtenerNumero() + ", el movimiento de blancas '" +
                    notacionBlancas + "' no es válido");
        }

        // Si hay movimiento de negras, debe ser válido
        Jugada movNegras = turno.getJugadaNegra();
        if (movNegras != null) {
            String notacionNegras = movNegras.getNotacion();
            if (!ValidadorSAN.esJugadaValida(notacionNegras)) {
                throw new Exception("En el turno " + turno.obtenerNumero() + ", el movimiento de negras '" +
                        notacionNegras + "' no es válido");
            }
        }
    }
}
