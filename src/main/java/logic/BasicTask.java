package logic;

import entities.Task;

import java.time.LocalDate;

public class BasicTask implements Task, Cloneable{
    private int id;
    private String label;
    private int priority; //could get changed
    private LocalDate entryDate;
    private LocalDate completionDate;

    public BasicTask(int id, String label, int priority) {
        this.id = id;
        this.label = label;
        this.priority = priority;
        this.entryDate = LocalDate.now();
    }

    public boolean isCompleted() {
        return false;
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
    public int getPriority() {
        return this.priority;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public BasicTask clone(){
        return new BasicTask(this.id, this.label,this.priority);
    }
}
