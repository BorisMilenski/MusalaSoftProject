package logic;

import entities.Priority;
import entities.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    public String completionTimeFromSet() {
        return timeDiffToString(compareLocalDateTime(entryDate, completionDate));
    }

    public String completionTimeFromCustom(LocalDateTime date) {
        return timeDiffToString(compareLocalDateTime(date, completionDate));
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
        return this.getDescription() + " (" + this.getPriority() + ")";
    }

    private long[] compareLocalDateTime(LocalDateTime fromDateTime, LocalDateTime toDateTime){
        long[] diff = new long[6];
        LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );

        diff[0] = tempDateTime.until( toDateTime, ChronoUnit.YEARS );
        tempDateTime = tempDateTime.plusYears( diff[0] );

        diff[1] = tempDateTime.until( toDateTime, ChronoUnit.MONTHS );
        tempDateTime = tempDateTime.plusMonths( diff[1] );

        diff[2] = tempDateTime.until( toDateTime, ChronoUnit.DAYS );
        tempDateTime = tempDateTime.plusDays(diff[2]);

        diff[3] = tempDateTime.until( toDateTime, ChronoUnit.HOURS );
        tempDateTime = tempDateTime.plusHours(diff[3]);

        diff[4] = tempDateTime.until( toDateTime, ChronoUnit.MINUTES );
        tempDateTime = tempDateTime.plusMinutes( diff[4] );

        diff[5] = tempDateTime.until( toDateTime, ChronoUnit.SECONDS );

        return diff;
    }

    private String timeDiffToString(long[] diff){
        String result = "";
        String[] labels = new String[]{"year", "month", "day", "hour", "minute", "second"};
        for (int i = 0; i < diff.length; i++){
            if (diff[i] != 0){
                if (!result.equals("")){
                    result.concat(", ") ;
                }
                result += labels[i];
                if (diff[i] > 1){
                    result.concat("s");
                }
                result.concat(" " + diff[i]);
            }
        }
        if (result.equals("")){
            result = "The dates are the same";
        }
        return result;
    }
}
