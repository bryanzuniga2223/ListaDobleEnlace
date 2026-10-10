package estructura;

import modelo.Cancion;

/**
 * TDA Lista Bidireccional (doblemente enlazada).
 * No usa colecciones de Java ni arreglos: solo nodos enlazados con
 * siguiente y anterior, y las referencias extremas primero y ultimo.
 * Las posiciones empiezan en 0 (0 = primero, tamanio - 1 = ultimo).
 */
public class ListaBidireccional {

    private Nodo primero;
    private Nodo ultimo;
    private int tamanio;

    public ListaBidireccional() {
        primero = null;
        ultimo = null;
        tamanio = 0;
    }

    // ===================== ESTADO =====================

    public boolean estaVacia() {
        return primero == null;
    }

    public int tamanio() {
        return tamanio;
    }

    public void vaciar() {
        primero = null; // sin referencias hacia los nodos, el recolector de basura de la JVM los libera
        ultimo = null;
        tamanio = 0;
    }

    // ===================== INSERTAR =====================

    public void insertarAlPrincipio(Cancion cancion) {
        Nodo nuevo = new Nodo(cancion);
        if (estaVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.setSiguiente(primero);
            primero.setAnterior(nuevo);
            primero = nuevo;
        }
        tamanio++;
    }

    public void insertarAlFinal(Cancion cancion) {
        Nodo nuevo = new Nodo(cancion);
        if (estaVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.setAnterior(ultimo);
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        tamanio++;
    }

    /** Posiciones válidas para insertar: 0 a tamanio (tamanio = insertar al final). */
    public void insertarEnPosicion(Cancion cancion, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " inválida para insertar. Usa un valor de 0 a " + tamanio + ".");
        }
        if (posicion == 0) {
            insertarAlPrincipio(cancion);
            return;
        }
        if (posicion == tamanio) {
            insertarAlFinal(cancion);
            return;
        }
        Nodo despues = nodoEn(posicion);       // el nodo que hoy ocupa esa posición
        Nodo antes = despues.getAnterior();
        Nodo nuevo = new Nodo(cancion);
        nuevo.setAnterior(antes);              // 1. el nuevo apunta hacia atrás
        nuevo.setSiguiente(despues);           // 2. el nuevo apunta hacia adelante
        antes.setSiguiente(nuevo);             // 3. el vecino anterior apunta al nuevo
        despues.setAnterior(nuevo);            // 4. el vecino posterior apunta al nuevo
        tamanio++;
    }

    // ===================== REMOVER =====================

    public Cancion removerAlPrincipio() {
        verificarNoVacia("remover");
        Nodo removido = primero;
        if (primero == ultimo) {               // era el único nodo
            primero = null;
            ultimo = null;
        } else {
            primero = removido.getSiguiente();
            primero.setAnterior(null);
            removido.setSiguiente(null);
        }
        tamanio--;
        return removido.getDato();
    }

    public Cancion removerAlFinal() {
        verificarNoVacia("remover");
        Nodo removido = ultimo;
        if (primero == ultimo) {               // era el único nodo
            primero = null;
            ultimo = null;
        } else {
            ultimo = removido.getAnterior();
            ultimo.setSiguiente(null);
            removido.setAnterior(null);
        }
        tamanio--;
        return removido.getDato();
    }

    public Cancion removerEnPosicion(int posicion) {
        verificarNoVacia("remover");
        verificarIndice(posicion);
        if (posicion == 0) {
            return removerAlPrincipio();
        }
        if (posicion == tamanio - 1) {
            return removerAlFinal();
        }
        Nodo removido = nodoEn(posicion);
        Nodo antes = removido.getAnterior();
        Nodo despues = removido.getSiguiente();
        antes.setSiguiente(despues);           // 1. el anterior se salta al removido
        despues.setAnterior(antes);            // 2. el siguiente se salta al removido
        removido.setAnterior(null);            // 3. el removido queda aislado
        removido.setSiguiente(null);           // 4.
        tamanio--;
        return removido.getDato();
    }

    // ===================== MOSTRAR =====================

    public Cancion mostrarPrimero() {
        verificarNoVacia("mostrar");
        return primero.getDato();
    }

    public Cancion mostrarUltimo() {
        verificarNoVacia("mostrar");
        return ultimo.getDato();
    }

    public Cancion mostrarEnPosicion(int posicion) {
        verificarNoVacia("mostrar");
        verificarIndice(posicion);
        return nodoEn(posicion).getDato();
    }

    // ===================== BUSCAR =====================

    /** Busca por valor: recorre desde primero comparando el título. Devuelve null si no existe. */
    public ResultadoBusqueda buscarPorTitulo(String titulo) {
        verificarNoVacia("buscar");
        String clave = titulo.trim();
        Nodo actual = primero;
        int posicion = 0;
        while (actual != null) {
            if (actual.getDato().titulo().equalsIgnoreCase(clave)) {
                return new ResultadoBusqueda(actual.getDato(), posicion, "primero", posicion);
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return null;
    }

    /** Busca por posición: cuenta saltos desde el extremo más cercano (primero o ultimo). */
    public ResultadoBusqueda buscarPorPosicion(int posicion) {
        verificarNoVacia("buscar");
        verificarIndice(posicion);
        boolean desdePrimero = masCercaDePrimero(posicion);
        int saltos = desdePrimero ? posicion : tamanio - 1 - posicion;
        return new ResultadoBusqueda(nodoEn(posicion).getDato(), posicion,
                desdePrimero ? "primero" : "ultimo", saltos);
    }

    // ===================== RECORRER =====================

    /** Recorre de primero a ultimo usando solo los enlaces siguiente. */
    public String recorrerAdelante() {
        StringBuilder sb = new StringBuilder("primero → ");
        Nodo actual = primero;
        while (actual != null) {
            sb.append('[').append(actual.getDato().titulo()).append("] → ");
            actual = actual.getSiguiente();
        }
        return sb.append("null").toString();
    }

    /** Recorre de ultimo a primero usando solo los enlaces anterior. */
    public String recorrerAtras() {
        StringBuilder sb = new StringBuilder("ultimo → ");
        Nodo actual = ultimo;
        while (actual != null) {
            sb.append('[').append(actual.getDato().titulo()).append("] → ");
            actual = actual.getAnterior();
        }
        return sb.append("null").toString();
    }

    // ===================== AUXILIARES PRIVADOS =====================

    private boolean masCercaDePrimero(int posicion) {
        return posicion <= tamanio - 1 - posicion;
    }

    /** Llega al nodo desde el extremo más cercano. Supone que la posición ya fue validada. */
    private Nodo nodoEn(int posicion) {
        if (masCercaDePrimero(posicion)) {
            Nodo actual = primero;
            for (int i = 0; i < posicion; i++) {
                actual = actual.getSiguiente();
            }
            return actual;
        }
        Nodo actual = ultimo;
        for (int i = tamanio - 1; i > posicion; i--) {
            actual = actual.getAnterior();
        }
        return actual;
    }

    private void verificarNoVacia(String operacion) {
        if (estaVacia()) {
            throw new ListaVaciaException("No se puede " + operacion + ": la lista está vacía.");
        }
    }

    private void verificarIndice(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " fuera de rango. Usa un valor de 0 a " + (tamanio - 1) + ".");
        }
    }
}