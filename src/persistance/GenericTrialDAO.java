package persistance;

import business.trialsTypes.GenericTrial;

import java.util.LinkedList;

public interface GenericTrialDAO {

    boolean create(GenericTrial name) ;

    LinkedList<GenericTrial> readAll() ;

    GenericTrial findByIndex(int index);

    boolean delete(int index);

    //boolean changeLine (int index, GenericTrial genericTrial);

}
