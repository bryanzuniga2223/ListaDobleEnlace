package estructura;

import modelo.Cancion;

/**
 * Resultado de una búsqueda: la canción, su posición, desde qué extremo
 * se recorrió la lista ("primero" o "ultimo") y cuántos saltos se dieron.
 */
public record ResultadoBusqueda(Cancion cancion, int posicion, String recorridoDesde, int saltos) {
}