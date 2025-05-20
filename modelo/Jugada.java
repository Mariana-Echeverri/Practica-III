package modelo;

public class Jugada {
    private String notacion;

    public Jugada(String notacion) {
        this.notacion = notacion;
    }

    public String getNotacion() {
        return notacion;
    }

    @Override
    public String toString() {
        return notacion;
    }
}
