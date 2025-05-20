package util;

import java.util.regex.Pattern;

/**
 * Clase utilitaria para validar notación algebraica estándar (SAN) de ajedrez
 */
public class ValidadorSAN {

    // Patrones para validar diferentes tipos de movimientos
    private static final Pattern PATRON_ENROQUE = Pattern.compile("^O-O(-O)?$");
    private static final Pattern PATRON_MOVIMIENTO_PIEZA = Pattern.compile("^[KQRBN][a-h1-8]?[a-h1-8]?x?[a-h][1-8](=[QRBN])?[+#]?$");
    private static final Pattern PATRON_MOVIMIENTO_PEON = Pattern.compile("^([a-h]x)?[a-h][1-8](=[QRBN])?[+#]?$");

    /**
     * Verifica si una jugada en notación SAN es válida
     * @param notacion La notación a validar
     * @return true si la notación es válida, false en caso contrario
     */
    public static boolean esJugadaValida(String notacion) {
        // Verificar que la notación no sea nula o vacía
        if (notacion == null || notacion.isEmpty()) {
            return false;
        }

        // Verificar si es un enroque
        if (PATRON_ENROQUE.matcher(notacion).matches()) {
            return true;
        }

        // Verificar si es un movimiento de pieza
        if (PATRON_MOVIMIENTO_PIEZA.matcher(notacion).matches()) {
            return true;
        }

        // Verificar si es un movimiento de peón
        if (PATRON_MOVIMIENTO_PEON.matcher(notacion).matches()) {
            return true;
        }

        // Si no coincide con ningún patrón, no es válido
        return false;
    }
}
