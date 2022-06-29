package persistance;

import business.Edition;

import java.util.LinkedList;

/**
 * Interifice que contiene los metodos de lectura, escritura, etc. de un fichero de ediciones (CSV o JSON)
 */
public interface EditionDAO {

    /**
     * Lee todas las ediciones de un fichero y los guarda en una lista
     * @return lista de ediciones leídas
     */
    LinkedList<Edition> readAll();

    /**
     * Escribe una nueva edicion en un fichero
     * @param edition edicion a escribir
     * @return indicia si se ha escrito correctamente (true) o no (false)
     */
    boolean create(Edition edition);

    /**
     * Elimina una edicion del fichero mediante su posicion
     * @param index posicion de la edicion a eliminar
     * @return indica si se ha eliminado correctamente (true) o no (false)
     */
    boolean delete(int index);

    /**
     * Obtiene una edicion concreta a traves de su posicion en el fichero
     * @param index posicion de la edicion a leer
     * @return Edicion leida
     */
    Edition findByIndex(int index);
}
