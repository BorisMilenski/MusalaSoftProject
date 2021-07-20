package entities;

import java.time.LocalDate;

public interface Task{
    boolean isCompleted();
    int completionTimeFromSet();
    int completionTimeFromCustom(LocalDate date);
    int getId();
    int getPriority();
    String getLabel();
}
