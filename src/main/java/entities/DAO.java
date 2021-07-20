package entities;

import java.util.List;

public interface DAO {
    List<Task> getTasks();
    void storeTasks(List<Task> tasks);
}
