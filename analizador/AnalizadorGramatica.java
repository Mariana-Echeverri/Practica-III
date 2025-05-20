package analizador;

import main.Arbol;
import modelo.Jugada;
import modelo.Partida;
import modelo.Turno;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para analizar la gramática de una partida de ajedrez y generar un árbol de derivación
 */
public class AnalizadorGramatica {

    /**
     * Analiza una partida y devuelve su árbol de derivación como texto
     */
    public String analizarYObtenerArbol(String textoPartida) {
        try {
            // Obtener los movimientos del texto
            List<String> movimientos = obtenerMovimientosDeTexto(textoPartida);
            
            if (movimientos.isEmpty()) {
                return "No se encontraron movimientos válidos en el texto.";
            }
            
            // Crear la partida a partir de los movimientos
            Partida partida = crearPartidaDesdeMovimientos(movimientos);
            
            // Generar el árbol de derivación como texto
            return generarArbolDerivacionTexto(partida);
            
        } catch (Exception e) {
            return "Error al analizar la partida: " + e.getMessage();
        }
    }
    
    /**
     * Analiza una partida y genera un árbol visual
     */
    public void analizarYObtenerArbol(Partida partida) {
        // Crear el árbol
        Arbol arbol = new Arbol();
        arbol.construirDesdePartida(partida);
        
        // Mostrar el árbol visual (implementación pendiente)
    }
    
    /**
     * Extrae los movimientos del texto de la partida
     */
    private List<String> obtenerMovimientosDeTexto(String texto) {
        List<String> movimientos = new ArrayList<>();

        // Definir patrón para buscar movimientos en el texto
        String patron = "(\\d+)\\.\\s*([A-Za-z0-9\\-O\\+#x=]+)(?:\\s+([A-Za-z0-9\\-O\\+#x=]+))?";
        Pattern buscador = Pattern.compile(patron);
        Matcher resultado = buscador.matcher(texto);

        // Buscar todos los movimientos en el texto
        while (resultado.find()) {
            // Añadir movimiento blanco
            movimientos.add(resultado.group(2).trim());

            // Añadir movimiento negro si existe
            String movimientoNegro = resultado.group(3);
            if (movimientoNegro != null && !movimientoNegro.isEmpty()) {
                movimientos.add(movimientoNegro.trim());
            }
        }

        return movimientos;
    }
    
    /**
     * Crea una partida a partir de una lista de movimientos
     */
    private Partida crearPartidaDesdeMovimientos(List<String> movimientos) {
        Partida partida = new Partida();
        int numeroTurno = 1;

        for (int i = 0; i < movimientos.size(); i += 2) {
            Turno turno = new Turno(numeroTurno);
            
            // Jugada blanca
            turno.setJugadaBlanca(new Jugada(movimientos.get(i)));
            
            // Jugada negra (si existe)
            if (i + 1 < movimientos.size()) {
                turno.setJugadaNegra(new Jugada(movimientos.get(i + 1)));
            }
            
            partida.agregarTurno(turno);
            numeroTurno++;
        }
        
        return partida;
    }
    
    /**
     * Genera un árbol de derivación en formato texto
     */
    private String generarArbolDerivacionTexto(Partida partida) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<partida>\n");
        
        for (Turno turno : partida.getTurnos()) {
            sb.append("  <turno>\n");
            sb.append("    <numero_turno>").append(turno.obtenerNumero()).append("</numero_turno>\n");
            
            // Jugada blanca
            sb.append("    <jugada_blanca>\n");
            sb.append("      <jugada>").append(turno.getJugadaBlanca().getNotacion()).append("</jugada>\n");
            sb.append("    </jugada_blanca>\n");
            
            // Jugada negra (si existe)
            if (turno.getJugadaNegra() != null) {
                sb.append("    <jugada_negra>\n");
                sb.append("      <jugada>").append(turno.getJugadaNegra().getNotacion()).append("</jugada>\n");
                sb.append("    </jugada_negra>\n");
            }
            
            sb.append("  </turno>\n");
        }
        
        sb.append("</partida>");
        
        return sb.toString();
    }
}
