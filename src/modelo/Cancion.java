package modelo;

/**
 * Entidad de dominio inmutable. El record genera el constructor,
 * los métodos de lectura (titulo(), artista(), ...), equals(),
 * hashCode() y toString(). Sus campos son final.
 */
public record Cancion(String titulo, String artista, int duracionSegundos) {

    // Constructor compacto: segunda capa de validación (la GUI valida primero).
    public Cancion {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (artista == null || artista.isBlank()) {
            throw new IllegalArgumentException("El artista no puede estar vacío.");
        }
        if (duracionSegundos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0:00.");
        }
        titulo = titulo.trim();
        artista = artista.trim();
    }

    /** Duración en formato minutos:segundos, por ejemplo 3:45. */
    public String duracionTexto() {
        return String.format("%d:%02d", duracionSegundos / 60, duracionSegundos % 60);
    }

    public String describir() {
        return String.format("Título: %s%nArtista: %s%nDuración: %s", titulo, artista, duracionTexto());
    }
}