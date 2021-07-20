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
    private static final String DATABASE_NAME = "tasks_data";
    private static final String TABLE_NAME = "tasks";
    private static final String DB_USERNAME = "DBM"; //TODO: Manage account
    private static final String DB_PASSWORD = "Tn65z6&dDObh@YJRRt39OwhV";//TODO: Manage account
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + "?allowPublicKeyRetrieval=true&useSSL=false";

    private Connection connection;
    private List<Task> tasks;

    public TaskDAO() {
        tasks = new ArrayList<>();
    }

    @Override
    public List<Task> getTasks() {
        if (tasks.isEmpty()) {
            try {
                this.startConnection();

                String strSelect = "SELECT * FROM " + TABLE_NAME;
                Statement statement = connection.createStatement();
                System.out.println("The SQL statement is: " + strSelect); // Echo For debugging

                ResultSet resultSet = statement.executeQuery(strSelect);
                int rowCount = 0;
                while (resultSet.next()) {
                    try {
                        int id = resultSet.getInt("id");
                        String description = resultSet.getString("description");
                        Priority priority =  Priority.valueOf(resultSet.getString("priority"));
                        LocalDateTime entryDate = resultSet.getTimestamp("entry_date").toLocalDateTime();
                        Timestamp completionTimestamp = resultSet.getTimestamp("completion_date");
                        LocalDateTime completionDate = completionTimestamp != null ? completionTimestamp.toLocalDateTime() : null;
                        tasks.add(new BasicTask(id, description, priority, entryDate, completionDate));
                    } catch (Exception e) {
                        e.printStackTrace(); //TODO: manage
                    }
                    ++rowCount;
                }
                System.out.println("Total number of records = " + rowCount + "\n");
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();//TODO:deal with it
            }
            finally {
                this.closeConnection();
            }
        }
        return tasks;
    }

    @Override
    public void addTask(Task task) {
        try {
            this.startConnection();

            String strInsert = "INSERT INTO " + DATABASE_NAME + "." + TABLE_NAME + " (id, description , priority , entry_date , completion_date)" +
                    " values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(strInsert);

            preparedStatement.setInt(1, task.getId());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getPriority().toString());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(task.getEntry()));
            preparedStatement.setTimestamp(5, task.getCompletion() != null ? Timestamp.valueOf(task.getCompletion()) : null);
            System.out.println("The SQL statement is: " + strInsert); // Echo For debugging
            ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null; //TODO: Check what to do with this result set
            System.out.println("Tasks Inserted" + "\n"); // Echo For debugging
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();//TODO:Manage exception
        }finally {
            this.closeConnection();
        }
        refreshTaskList();
    }
    @Override
    public void editTask(int id, Task task) {
        try {
            this.startConnection();

            String strUpdate = "UPDATE " + DATABASE_NAME + "." + TABLE_NAME + " set description = ?, priority = ?, entry_date = ?, completion_date = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strUpdate);

            preparedStatement.setString(1, task.getDescription());
            preparedStatement.setString(2, task.getPriority().toString());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getEntry()));
            preparedStatement.setTimestamp(4, task.getCompletion() != null ? Timestamp.valueOf(task.getCompletion()) : null);
            preparedStatement.setInt(5, id);

            System.out.println("The SQL statement is: " + strUpdate); // Echo For debugging
            ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null; //TODO: Check what to do with this result set
            System.out.println("Tasks updated" + "\n"); // Echo For debugging
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();//TODO:Manage exception
        }
        finally {
            this.closeConnection();
        }
        refreshTaskList();
    }
    @Override
    public void removeTask(int id, Task task) {
        try {
            this.startConnection();

            String strDelete = "DELETE FROM " + DATABASE_NAME + "." + TABLE_NAME + " where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strDelete);

            preparedStatement.setInt(1, id);

            System.out.println("The SQL statement is: " + strDelete); // Echo For debugging
            ResultSet set = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null; //TODO: Check what to do with this result set
            System.out.println("Tasks updated" + "\n"); // Echo For debugging
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();//TODO:Manage exception
        }
        finally {
            this.closeConnection();
        }
        refreshTaskList();
    }

    private void startConnection(){
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            //connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException sqlException) {
           sqlException.printStackTrace();//TODO:Manage exception
        }
        finally {
            System.out.print("Connected to database"); //TODO: Do this with a Logger
        }
    }

    private void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqlException) { sqlException.printStackTrace();//TODO:Manage exception
        }
    }

    private void refreshTaskList(){
        this.tasks.clear();
    }
}
