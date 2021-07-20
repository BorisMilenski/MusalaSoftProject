package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Task{
    boolean isCompleted();
    int completionTimeFromSet();
    int completionTimeFromCustom(LocalDate date);
    int getId();
    Priority getPriority();
    String getDescription();
    LocalDateTime getEntry();
    LocalDateTime getCompletion();
    void setId(int id);
    void setDescription(String description);
    void setPriority(Priority priority);
    void setEntry(LocalDateTime entry);
    void setCompletion(LocalDateTime completion);

}
