package analizador;

import main.Arbol;
import main.AjedrezArbolVisualizer;
import modelo.Jugada;
import modelo.Partida;
import modelo.ResultadoAnalisis;
import modelo.Turno;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorGramatico {
    
    /**
     * Analiza una partida de ajedrez en formato texto
     * @param textoPartida Texto de la partida en notación SAN
     * @return Resultado del análisis
     */
    public ResultadoAnalisis analizar(String textoPartida) {
        try {
            List<String> movimientos = obtenerMovimientosDeTexto(textoPartida);

            if (movimientos.isEmpty()) {
                return new ResultadoAnalisis(false, "No hay movimientos válidos en el texto");
            }

            Partida partida = crearPartidaDesdeMovimientos(movimientos);
            
            // Crear y mostrar el árbol visual
            Arbol arbol = new Arbol();
            arbol.construirDesdePartida(partida);
            mostrarArbolVisual(arbol);

            return new ResultadoAnalisis(true, partida.contarTurnos());

        } catch (Exception error) {
            return new ResultadoAnalisis(false, "Error: " + error.getMessage());
        }
    }

    /**
     * Muestra el árbol visual de la partida
     * @param arbol Árbol a visualizar
     */
    private void mostrarArbolVisual(Arbol arbol) {
        SwingUtilities.invokeLater(() -> {
            AjedrezArbolVisualizer.mostrarArbolAjedrez(arbol);
        });
    }

    /**
     * Extrae los movimientos del texto de la partida
     * @param texto Texto de la partida
     * @return Lista de movimientos
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
     * @param movimientos Lista de movimientos
     * @return Partida creada
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
     * Analiza una partida y devuelve su árbol de derivación como texto
     * @param textoPartida Texto de la partida
     * @return Árbol de derivación en formato texto
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
     * Genera un árbol de derivación en formato texto
     * @param partida Partida a analizar
     * @return Árbol de derivación en formato texto
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

/**
 * Panel para visualizar el árbol de jugadas
 */
class PanelArbolJugadas extends JPanel {
    private List<String> jugadas;
    private int tamanoNodo = 30;
    private double escala = 1.0;

    public PanelArbolJugadas(List<String> jugadas) {
        this.jugadas = jugadas;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
    }

    /**
     * Calcula el tamaño preferido del panel
     */
    @Override
    public Dimension getPreferredSize() {
        // Calcular niveles del árbol
        int niveles = calcularNivelesArbol();

        // Calcular ancho y alto necesarios
        int ancho = (int) Math.pow(2, niveles) * tamanoNodo * 2;
        int alto = niveles * 100 + 50;

        return new Dimension(ancho, alto);
    }

    /**
     * Calcula cuántos niveles tendrá el árbol
     */
    private int calcularNivelesArbol() {
        return (int) Math.ceil(Math.log(jugadas.size() + 1) / Math.log(2));
    }

    /**
     * Dibuja el componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Mejorar la calidad del dibujo
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Si no hay jugadas, no dibujar nada
        if (jugadas.isEmpty()) return;

        // Calcular dimensiones del árbol
        int niveles = calcularNivelesArbol();
        int altoEstimado = niveles * 100;
        int anchoEstimado = (int) Math.pow(2, niveles - 1) * tamanoNodo * 3;

        // Calcular escala para ajustar el árbol a la ventana
        escala = Math.min(
                getWidth() / (double) anchoEstimado,
                getHeight() / (double) altoEstimado
        );

        // No permitir escala mayor a 1
        if (escala > 1.0) {
            escala = 1.0;
        }

        // Aplicar la escala al dibujo
        g2d.scale(escala, escala);

        // Dibujar el árbol comenzando por la raíz
        dibujarNodo(g2d, 0, getWidth() / (int)escala / 2, 50);
    }

    /**
     * Dibuja un nodo y sus hijos recursivamente
     */
    private void dibujarNodo(Graphics2D g2d, int indice, int x, int y) {
        // Si el índice está fuera de la lista, no dibujar
        if (indice >= jugadas.size()) return;

        // Elegir color según tipo de nodo
        if (indice == 0) {
            // Nodo raíz (azul)
            g2d.setColor(new Color(100, 180, 255));
        } else {
            // Resto de nodos (gris claro)
            g2d.setColor(new Color(220, 220, 220));
        }

        // Dibujar círculo del nodo
        g2d.fillOval(x - tamanoNodo/2, y - tamanoNodo/2, tamanoNodo, tamanoNodo);

        // Dibujar borde negro
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - tamanoNodo/2, y - tamanoNodo/2, tamanoNodo, tamanoNodo);

        // Dibujar texto de la jugada
        String textoJugada = jugadas.get(indice);
        FontMetrics medidas = g2d.getFontMetrics();
        int anchoTexto = medidas.stringWidth(textoJugada);
        g2d.drawString(textoJugada, x - anchoTexto/2, y + 5);

        // Calcular posición de los hijos
        int hijoIzquierdo = 2 * indice + 1;
        int hijoDerecho = 2 * indice + 2;

        // Calcular separación entre nodos
        int nivel = (int) (Math.log(indice + 1) / Math.log(2));
        int separacion = getWidth() / (int)escala / (int)Math.pow(2, nivel + 2);
        if (separacion < tamanoNodo * 2) {
            separacion = tamanoNodo * 2;
        }

        // Altura vertical entre niveles
        int alturaVertical = 100;

        // Dibujar hijo izquierdo si existe
        if (hijoIzquierdo < jugadas.size()) {
            // Calcular posición
            int hijoX = x - separacion;
            int hijoY = y + alturaVertical;

            // Dibujar línea hacia el hijo
            g2d.drawLine(x, y + tamanoNodo/2, hijoX, hijoY - tamanoNodo/2);

            // Dibujar nodo hijo recursivamente
            dibujarNodo(g2d, hijoIzquierdo, hijoX, hijoY);
        }

        // Dibujar hijo derecho si existe
        if (hijoDerecho < jugadas.size()) {
            // Calcular posición
            int hijoX = x + separacion;
            int hijoY = y + alturaVertical;

            // Dibujar línea hacia el hijo
            g2d.drawLine(x, y + tamanoNodo/2, hijoX, hijoY - tamanoNodo/2);

            // Dibujar nodo hijo recursivamente
            dibujarNodo(g2d, hijoDerecho, hijoX, hijoY);
        }
    }
}
