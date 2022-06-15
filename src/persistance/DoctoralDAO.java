package persistance;

import business.trialsTypes.DoctoralThesis;

import java.util.LinkedList;

public interface DoctoralDAO {
    boolean create(DoctoralThesis doctoralThesis);

    LinkedList<DoctoralThesis> readAll();

    DoctoralThesis findByIndex(int index);

    boolean delete(int index);

    boolean changeLine (int index, DoctoralThesis doctoralThesis);
}
