package logic;

import entities.Priority;
import entities.Task;

import java.time.LocalDate;

public class BasicTask implements Task{
    private int id;
    private String description;
    private Priority priority;
    private LocalDate entryDate;
    private LocalDate completionDate;

    public BasicTask(int id, String description, Priority priority, LocalDate entryDate, LocalDate completionDate) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.entryDate = entryDate;
        this.completionDate = completionDate;
    }

    public BasicTask(int id, String label, Priority priority) {
        this.id = id;
        this.description = label;
        this.priority = priority;
        this.entryDate = LocalDate.now();
    }

    public boolean isCompleted() {
        return completionDate != null;
    }

    public int completionTimeFromSet() {
        return 0;
    }

    public int completionTimeFromCustom(LocalDate date) {
        return 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public LocalDate getEntryLocalDate() {
        return this.entryDate;
    }

    @Override
    public LocalDate getCompletionLocalDate() {
        return this.completionDate;
    }


}
