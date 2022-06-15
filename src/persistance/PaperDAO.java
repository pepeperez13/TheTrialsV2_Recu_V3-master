package persistance;

import business.trialsTypes.PaperPublication;

import java.util.LinkedList;

public interface PaperDAO {

    boolean create(PaperPublication article);

    LinkedList<PaperPublication> readAll();

    boolean delete(int index);

    boolean changeLine(int index, PaperPublication paperPublication);
}
