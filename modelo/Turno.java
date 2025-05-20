package modelo;

public class Turno {
    private int numeroTurno;
    private Jugada jugadaBlanca;
    private Jugada jugadaNegra;

    public Turno(int numeroTurno) {
        this.numeroTurno = numeroTurno;
        this.jugadaBlanca = null;
        this.jugadaNegra = null;
    }

    public void setJugadaBlanca(Jugada jugada) {
        this.jugadaBlanca = jugada;
    }

    public void setJugadaNegra(Jugada jugada) {
        this.jugadaNegra = jugada;
    }

    public Jugada getJugadaBlanca() {
        return this.jugadaBlanca;
    }

    public Jugada getJugadaNegra() {
        return this.jugadaNegra;
    }
    
    /**
     * Método para compatibilidad con código existente
     */
    public void agregarMovimientoBlanco(Jugada jugada) {
        setJugadaBlanca(jugada);
    }
    
    /**
     * Método para compatibilidad con código existente
     */
    public void agregarMovimientoNegro(Jugada jugada) {
        setJugadaNegra(jugada);
    }
    
    /**
     * Método para compatibilidad con código existente
     */
    public int obtenerNumero() {
        return numeroTurno;
    }
    
    /**
     * Método para compatibilidad con código existente
     */
    public Jugada obtenerMovimientoBlanco() {
        return getJugadaBlanca();
    }
    
    /**
     * Método para compatibilidad con código existente
     */
    public Jugada obtenerMovimientoNegro() {
        return getJugadaNegra();
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder(numeroTurno + ". ");
        
        if (jugadaBlanca != null) {
            resultado.append(jugadaBlanca.getNotacion());
        }
        
        if (jugadaNegra != null) {
            resultado.append(" ").append(jugadaNegra.getNotacion());
        }
        
        return resultado.toString();
    }
}
