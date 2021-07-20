package entities;

import java.time.LocalDate;

public interface Task{
    boolean isCompleted();
    int completionTimeFromSet();
    int completionTimeFromCustom(LocalDate date);
    int getId();
    Priority getPriority();
    String getDescription();
    LocalDate getEntryLocalDate();
    LocalDate getCompletionLocalDate();
}
