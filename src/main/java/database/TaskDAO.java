package database;

import entities.DAO;
import entities.Priority;
import entities.Task;
import logic.BasicTask;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements DAO {
    private static TaskDAO instance = new TaskDAO();

    private static final String DATABASE_NAME = "tasks_data";
    private static final String TABLE_NAME = "tasks";
    private static final String DB_USERNAME = "DBM";
    private static final String DB_PASSWORD = "Tn65z6&dDObh@YJRRt39OwhV";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + "?allowPublicKeyRetrieval=true&useSSL=false";

    private Connection connection;
    private List<Task> tasks;

    public static TaskDAO getInstance(){
        return instance;
    }

    @Override
    public List<Task> getTasks() throws SQLException {
        if (tasks.isEmpty()) {
            this.startConnection();

            String strSelect = "SELECT * FROM " + TABLE_NAME;
            Statement statement = connection.createStatement();
            System.out.println("[+] The SQL statement is: " + strSelect); // Echo For debugging

            ResultSet resultSet = statement.executeQuery(strSelect);
            int rowCount = 0;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                Priority priority =  Priority.valueOf(resultSet.getString("priority"));
                LocalDateTime entryDate = resultSet.getTimestamp("entry_date").toLocalDateTime();
                Timestamp completionTimestamp = resultSet.getTimestamp("completion_date");
                LocalDateTime completionDate = completionTimestamp != null ? completionTimestamp.toLocalDateTime() : null;
                tasks.add(new BasicTask(id, description, priority, entryDate, completionDate));
                ++rowCount;
            }
            System.out.println("[+] Total number of records = " + rowCount + "\n");
            this.closeConnection();
        }
        return tasks;
    }

    @Override
    public void addTask(Task task) throws SQLException {
        this.startConnection();

        String strInsert = "INSERT INTO " + DATABASE_NAME + "." + TABLE_NAME + " (description , priority , entry_date , completion_date)" +
                " values (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(strInsert);

        preparedStatement.setString(1, task.getDescription());
        preparedStatement.setString(2, task.getPriority().toString());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getEntry()));
        preparedStatement.setTimestamp(4, task.getCompletion() != null ? Timestamp.valueOf(task.getCompletion()) : null);
        System.out.println("[+] The SQL statement is: " + strInsert); // Echo For debugging
        ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
        System.out.println("[+] Tasks Inserted" + "\n"); // Echo For debugging

        this.closeConnection();
        refreshTaskList();
    }
    @Override
    public void editTask(int id, Task task) throws SQLException, IllegalArgumentException {
        if (doesIdExistInList(id)) {
            this.startConnection();

            String strUpdate = "UPDATE " + DATABASE_NAME + "." + TABLE_NAME + " set description = ?, priority = ?, entry_date = ?, completion_date = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strUpdate);

            preparedStatement.setString(1, task.getDescription());
            preparedStatement.setString(2, task.getPriority().toString());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getEntry()));
            preparedStatement.setTimestamp(4, task.getCompletion() != null ? Timestamp.valueOf(task.getCompletion()) : null);
            preparedStatement.setInt(5, id);

            System.out.println("[+] The SQL statement is: " + strUpdate); // Echo For debugging
            ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

            this.closeConnection();
            refreshTaskList();
        }else {
            throw new IllegalArgumentException("Task Id not found");
        }
    }
    @Override
    public void removeTask(int id) throws SQLException, IllegalArgumentException {
        if (doesIdExistInList(id)) {
            this.startConnection();

            String strDelete = "DELETE FROM " + DATABASE_NAME + "." + TABLE_NAME + " where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strDelete);

            preparedStatement.setInt(1, id);

            System.out.println("[+] The SQL statement is: " + strDelete); // Echo For debugging
            ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

            this.closeConnection();
            refreshTaskList();
        }else{
            throw new IllegalArgumentException("Task Id not found");
        }
    }

    public void markTaskasCompleted(int id, LocalDateTime completion_date) throws SQLException, IllegalArgumentException {
        if (completion_date != null) {

            this.startConnection();

            String strUpdate = "UPDATE " + DATABASE_NAME + "." + TABLE_NAME + " set completion_date = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strUpdate);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(completion_date));
            preparedStatement.setInt(2, id);

            System.out.println("[+] The SQL statement is: " + strUpdate); // Echo For debugging
            ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

            this.closeConnection();
            refreshTaskList();
        }else{
            throw new IllegalArgumentException("completion_date cannot be null");
        }
    }

    private TaskDAO() {
        tasks = new ArrayList<>();
    }

    private void startConnection() throws SQLException {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("[+] Connected to database");
    }

    private void closeConnection() throws SQLException {
        if(connection != null) {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("[+] Disconnected from database");
            }
        }
    }

    private void refreshTaskList(){
        this.tasks.clear();
    }

    private boolean doesIdExistInList(int id) throws SQLException {
        this.getTasks();
        return tasks.stream().anyMatch((task) -> task.getId() == id);
    }
}
