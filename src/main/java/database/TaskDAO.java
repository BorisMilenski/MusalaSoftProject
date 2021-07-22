package database;

import entities.DAO;
import entities.Priority;
import entities.Task;
import entities.User;
import entities.BasicTask;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO extends DatabaseAccess implements DAO<Task> {
    private static final String TABLE_NAME = "tasks";

    private final User user;
    private final List<Task> tasks;

    public TaskDAO(User user) {
        this.user = user;
        tasks = new ArrayList<>();
    }

    @Override
    synchronized public List<Task> get() throws SQLException {
        if (tasks.isEmpty()) {
            this.startConnection();

            String strSelect = "SELECT * FROM " + TABLE_NAME + " where user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strSelect);

            preparedStatement.setInt(1, user.getId());

            ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            int rowCount = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String description = resultSet.getString("description");
                    Priority priority = Priority.valueOf(resultSet.getString("priority"));
                    LocalDateTime entryDate = resultSet.getTimestamp("entry_date").toLocalDateTime();
                    Timestamp completionTimestamp = resultSet.getTimestamp("completion_date");
                    LocalDateTime completionDate = completionTimestamp != null ? completionTimestamp.toLocalDateTime() : null;
                    tasks.add(new BasicTask(id, description, priority, entryDate, completionDate));
                    ++rowCount;
                }
                System.out.println("[+] Total number of records = " + rowCount + "\n");
            }
            this.closeConnection();
        }
//        if (tasks.isEmpty()){
//            throw new SQLDataException("[-] User Id not found or no Task data Found");
//        }
        return tasks;
    }

    @Override
    synchronized public void add(Task task) throws SQLException {
        this.startConnection();

        String strInsert = "INSERT INTO " + DATABASE_NAME + "." + TABLE_NAME + " (description , priority , entry_date , completion_date, user_id)" +
                " values (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(strInsert);

        preparedStatement.setString(1, task.getDescription());
        preparedStatement.setString(2, task.getPriority().toString());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getEntry()));
        preparedStatement.setTimestamp(4, task.getCompletion() != null ? Timestamp.valueOf(task.getCompletion()) : null);
        preparedStatement.setInt(5, user.getId());

        System.out.println("[+] The SQL statement is: " + strInsert); // Echo For debugging
        ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
        System.out.println("[+] Tasks Inserted" + "\n"); // Echo For debugging

        this.closeConnection();
        refreshTaskList();
    }
    @Override
    synchronized public void edit(int id, Task t) throws SQLException, IllegalArgumentException {
        if (doesIdExistInList(id)) {
            this.startConnection();

            String strUpdate = "UPDATE " + DATABASE_NAME + "." + TABLE_NAME + " set description = ?, priority = ?, entry_date = ?, completion_date = ?, user_id = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strUpdate);

            preparedStatement.setString(1, t.getDescription());
            preparedStatement.setString(2, t.getPriority().toString());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(t.getEntry()));
            preparedStatement.setTimestamp(4, t.getCompletion() != null ? Timestamp.valueOf(t.getCompletion()) : null);
            preparedStatement.setInt(5, user.getId());
            preparedStatement.setInt(6, id);

            System.out.println("[+] The SQL statement is: " + strUpdate); // Echo For debugging
            ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

            this.closeConnection();
            refreshTaskList();
        }else {
            throw new IllegalArgumentException("[-] Task Id not found");
        }
    }
    @Override
    synchronized public void remove(int id) throws SQLException, IllegalArgumentException {
        if (doesIdExistInList(id)) {
            this.startConnection();

            String strDelete = "DELETE FROM " + DATABASE_NAME + "." + TABLE_NAME + " where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strDelete);

            preparedStatement.setInt(1, id);

            System.out.println("[+] The SQL statement is: " + strDelete); // Echo For debugging
            ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

            this.closeConnection();
            refreshTaskList();
        }else{
            throw new IllegalArgumentException("[-] Task Id not found");
        }
    }

    synchronized public void markTaskAsCompleted(int id, LocalDateTime completion_date) throws SQLException, IllegalArgumentException {
        if (completion_date != null) {
            if (doesIdExistInList(id)) {
                this.startConnection();

                String strUpdate = "UPDATE " + DATABASE_NAME + "." + TABLE_NAME + " set completion_date = ? where id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(strUpdate);

                preparedStatement.setTimestamp(1, Timestamp.valueOf(completion_date));
                preparedStatement.setInt(2, id);

                System.out.println("[+] The SQL statement is: " + strUpdate); // Echo For debugging
                ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
                System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

                this.closeConnection();
                refreshTaskList();
            }
            else{
                throw new IllegalArgumentException("[-] Task Id not found");
            }
        }else{
            throw new IllegalArgumentException("[-] completion_date cannot be null");
        }
    }

    private void refreshTaskList(){
        this.tasks.clear();
    }

    private boolean doesIdExistInList(int id) throws SQLException {
        this.get();
        return tasks.stream().anyMatch((task) -> task.getId() == id);
    }
}
