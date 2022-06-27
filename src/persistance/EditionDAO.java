package persistance;

import business.Edition;

import java.util.LinkedList;

public interface EditionDAO {
    LinkedList<Edition> readAll();

    boolean create(Edition edition);

    boolean delete(int index);

    Edition findByIndex(int index);
}
