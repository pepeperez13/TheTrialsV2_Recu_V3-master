package persistance;

import business.trialsTypes.Budget;
import java.util.LinkedList;

public interface BudgetDAO {
    boolean create(Budget budget);

    LinkedList<Budget> readAll();

    Budget findByIndex(int index);

    boolean delete(int index);

    boolean changeLine(int index, Budget budget);
}
