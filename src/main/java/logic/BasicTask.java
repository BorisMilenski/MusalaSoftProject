package logic;

import entities.Priority;
import entities.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BasicTask implements Task{
    private int id;
    private String description;
    private Priority priority;
    private LocalDateTime entryDate;
    private LocalDateTime completionDate;

    public BasicTask(int id, String description, Priority priority, LocalDateTime entryDate, LocalDateTime completionDate) {
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
        this.entryDate = LocalDateTime.now();
    }

    public BasicTask(String label, Priority priority) {
        this.description = label;
        this.priority = priority;
        this.entryDate = LocalDateTime.now();
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
    public LocalDateTime getEntry() {
        return this.entryDate;
    }

    @Override
    public LocalDateTime getCompletion() {
        return this.completionDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public void setEntry(LocalDateTime entry) {
        this.entryDate = entry;
    }

    @Override
    public void setCompletion(LocalDateTime completion) {
        this.completionDate = completion;
    }

    @Override
    public String toString() {
        return this.getId() + ". " + this.getDescription() + " (" + this.getPriority() + ")";
    }
}
