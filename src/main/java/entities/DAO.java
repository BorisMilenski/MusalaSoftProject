package entities;

import java.sql.SQLException;
import java.util.List;

public interface DAO { //TODO: Refactor after merge, DAO is an independent pattern
    List<Task> getTasks() throws SQLException;
    void addTask(Task task) throws SQLException;
    void editTask(int id, Task task) throws SQLException, IllegalArgumentException;
    void removeTask(int id) throws SQLException, IllegalArgumentException;
}
