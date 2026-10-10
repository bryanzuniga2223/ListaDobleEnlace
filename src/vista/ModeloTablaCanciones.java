package vista;

import estructura.ListaBidireccional;
import javax.swing.table.AbstractTableModel;
import modelo.Cancion;

/**
 * Adaptador entre la lista y el JTable. No copia los datos a ninguna
 * colección: cada celda se lee directamente de la lista enlazada.
 * Puede mostrar la lista hacia adelante (primero → ultimo) o hacia atrás.
 */
public class ModeloTablaCanciones extends AbstractTableModel {

    private final ListaBidireccional lista;
    private boolean haciaAtras = false;

    public ModeloTablaCanciones(ListaBidireccional lista) {
        this.lista = lista;
    }

    public void setHaciaAtras(boolean haciaAtras) {
        this.haciaAtras = haciaAtras;
        fireTableDataChanged();
    }

    public boolean isHaciaAtras() {
        return haciaAtras;
    }

    /** Convierte una fila de la tabla en la posición real dentro de la lista (y viceversa). */
    public int convertir(int filaOPosicion) {
        return haciaAtras ? lista.tamanio() - 1 - filaOPosicion : filaOPosicion;
    }

    @Override
    public int getRowCount() {
        return lista.tamanio();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columna) {
        return switch (columna) {
            case 0 -> "Posición";
            case 1 -> "Título";
            case 2 -> "Artista";
            default -> "Duración";
        };
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        // shortcut: recorre la lista por cada celda (O(n) por celda); suficiente para decenas de canciones.
        int posicion = convertir(fila);
        Cancion c = lista.mostrarEnPosicion(posicion);
        return switch (columna) {
            case 0 -> posicion;
            case 1 -> c.titulo();
            case 2 -> c.artista();
            default -> c.duracionTexto();
        };
    }

    public void refrescar() {
        fireTableDataChanged();
    }
}