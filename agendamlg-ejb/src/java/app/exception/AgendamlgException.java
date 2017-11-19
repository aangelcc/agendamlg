package app.exception;

public class AgendamlgException extends Exception {

    public AgendamlgException(String mensajeError) {
        super(mensajeError);
    }

    public AgendamlgException(String mensajeError, Throwable t) {
        super(mensajeError, t);
    }

    public AgendamlgException(Throwable t) {
        super(t.getMessage(), t);
    }
}
