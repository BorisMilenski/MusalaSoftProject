package logic;

import entities.Task;

public class RepeatingTask extends BasicTask{
    private int parentId;
    public RepeatingTask(Task task) {
        super(task.getId(), task.getLabel(), task.getPriority());
    }
    
}
