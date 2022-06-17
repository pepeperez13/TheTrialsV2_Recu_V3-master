package persistance;

import business.trialsTypes.GenericTrial;

import java.util.LinkedList;

public interface TrialsDAO {

    boolean create (GenericTrial genericTrial) ;

    LinkedList<GenericTrial> readAll() ;

    GenericTrial findByIndex(int index);

    boolean delete(int index);

    boolean changeLine (int index, GenericTrial genericTrial);
}
