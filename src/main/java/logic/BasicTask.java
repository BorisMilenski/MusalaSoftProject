package logic;

import entities.Priority;
import entities.Task;

import java.time.LocalDate;

public class BasicTask implements Task, Cloneable{
    private int id;
    private String description;
    private Priority priority; //could get changed
    private LocalDate entryDate;
    private LocalDate completionDate;

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

    public BasicTask clone(){
        return new BasicTask(this.id, this.description,this.priority);
    }
}
