package estructura;

/** Se lanza cuando se intenta remover, mostrar o buscar con primero y ultimo en null. */
public class ListaVaciaException extends RuntimeException {

    public ListaVaciaException(String mensaje) {
        super(mensaje);
    }
}