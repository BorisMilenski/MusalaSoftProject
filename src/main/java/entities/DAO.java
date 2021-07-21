package entities;

import java.util.List;

public interface DAO { //TODO: Refactor after merge, DAO is an independent pattern
    List<Task> getTasks();
    void addTask(Task task);
    void editTask(int id, Task task);
    void removeTask(int id, Task task);
}
