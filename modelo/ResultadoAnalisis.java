package modelo;

public class ResultadoAnalisis {
    private boolean esValido;
    private String mensajeError;
    private int numeroTurnos;

    /**
     * Constructor para resultado exitoso
     */
    public ResultadoAnalisis(int numeroTurnos) {
        this.esValido = true;
        this.numeroTurnos = numeroTurnos;
        this.mensajeError = null;
    }

    /**
     * Constructor para resultado con error
     */
    public ResultadoAnalisis(String mensajeError) {
        this.esValido = false;
        this.mensajeError = mensajeError;
        this.numeroTurnos = 0;
    }

    /**
     * Constructor general
     */
    public ResultadoAnalisis(boolean esValido, Object resultado) {
        this.esValido = esValido;
        if (esValido) {
            if (resultado instanceof Integer) {
                this.numeroTurnos = (Integer) resultado;
            } else {
                this.numeroTurnos = 0;
            }
            this.mensajeError = null;
        } else {
            this.mensajeError = resultado.toString();
            this.numeroTurnos = 0;
        }
    }

    /**
     * Verifica si el análisis fue exitoso
     */
    public boolean esValido() {
        return esValido;
    }

    /**
     * Obtiene el mensaje de error (si hay)
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * Obtiene el número de turnos analizados
     */
    public int getNumeroTurnos() {
        return numeroTurnos;
    }

    /**
     * Compatibilidad con versiones anteriores
     */
    public boolean isExito() {
        return esValido;
    }

    /**
     * Compatibilidad con versiones anteriores
     */
    public Object getResultado() {
        return esValido ? numeroTurnos : mensajeError;
    }
}
