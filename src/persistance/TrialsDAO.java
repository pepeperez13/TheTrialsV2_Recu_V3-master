package persistance;

import business.trialsTypes.GenericTrial;

import java.util.LinkedList;

/**
 *  Interifice que contiene los metodos de lectura, escritura, etc. de un fichero de pruebas (CSV o JSON)
 */
public interface TrialsDAO {

    /**
     * Escribe una nueva prueba en un fichero
     * @param genericTrial prueba a escribir
     * @return indicia si se ha escrito correctamente (true) o no (false)
     */
    boolean create (GenericTrial genericTrial) ;

    /**
     * Lee todas las pruebas de un fichero y los guarda en una lista
     * @return lista de pruebas leídas
     */
    LinkedList<GenericTrial> readAll() ;

    /**
     * Obtiene una prueba concreta a traves de su posicion en el fichero
     * @param index posicion de la prueba a leer
     * @return Prueba leida
     */
    GenericTrial findByIndex(int index);

    /**
     * Elimina una prueba del fichero mediante su posicion
     * @param index posicion de la prueba a eliminar
     * @return indica si se ha eliminado correctamente (true) o no (false)
     */
    boolean delete(int index);

    /**
     * Actualiza la información de un objeto del fichero a traves de su posicion
     * @param index posicion del elemento a modificar
     * @param genericTrial nuevo elemento a escribir
     * @return indica si se ha eliminado correctamente (true) o no (false)
     */
    boolean changeLine (int index, GenericTrial genericTrial);
}
