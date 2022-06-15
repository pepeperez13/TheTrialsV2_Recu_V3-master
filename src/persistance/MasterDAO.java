package persistance;

import business.trialsTypes.MasterStudies;

import java.util.LinkedList;

public interface MasterDAO {
    boolean create(MasterStudies masterStudies);

    LinkedList<MasterStudies> readAll();

    MasterStudies findByIndex(int index);

    boolean delete(int index);

    boolean changeLine(int index, MasterStudies masterStudies);


}
