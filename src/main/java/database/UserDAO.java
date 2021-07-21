package database;

import entities.DAO;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DatabaseAccess implements DAO<User> {

    private static final String TABLE_NAME = "users";
    private User user;
    private String username;
    private String password;

    public UserDAO(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        this.user = this.get().get(0);
    }

    public UserDAO() {
    }

    public User getUser() {
        return user;
    }

    @Override
    synchronized public List<User> get() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        if (user == null) {
            this.startConnection();

            String strSelect = "SELECT * FROM " + TABLE_NAME + " where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(strSelect);
            System.out.println("[+] The SQL statement is: " + strSelect); // Echo For debugging

            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.password);

            ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    users.add(new User(id, username, password, email));
                }
            }
            this.closeConnection();
        }
        if (users.isEmpty()){
            throw new SQLDataException("No user found");
        }
        if (users.size() > 1){
            throw new SQLDataException("Multiple users found");
        }
        return users;
    }

    @Override
    synchronized public void add(User t) throws SQLException {
        this.startConnection();

        String strInsert = "INSERT INTO " + DATABASE_NAME + "." + TABLE_NAME + " (username, password, email)" +
                " values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(strInsert);

        preparedStatement.setString(1, t.getUsername());
        preparedStatement.setString(2, t.getPassword());
        preparedStatement.setString(2, t.getEmail());
        System.out.println("[+] The SQL statement is: " + strInsert); // Echo For debugging
        ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
        System.out.println("[+] User Inserted" + "\n"); // Echo For debugging

        this.closeConnection();
    }
    //TODO: Remove placeholders
    @Override
    synchronized public void edit(int id, User user) throws SQLException, IllegalArgumentException {

    }

    @Override
    synchronized public void remove(int id) throws SQLException, IllegalArgumentException {

    }
    /*@Override TODO: Check if we need these ones and add extra checks (does user exist)
    synchronized public void edit(int id, User t) throws SQLException, IllegalArgumentException {
        this.startConnection();

        String strUpdate = "UPDATE " + DATABASE_NAME + "." + TABLE_NAME + " set username = ?, password = ?, email = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(strUpdate);

        preparedStatement.setString(1, t.getUsername());
        preparedStatement.setString(2, t.getPassword());
        preparedStatement.setString(2, t.getEmail());
        preparedStatement.setInt(4, id);

        System.out.println("[+] The SQL statement is: " + strUpdate); // Echo For debugging
        ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
        System.out.println("[+] Tasks updated" + "\n"); // Echo For debugging

        this.closeConnection();

    }
    @Override
    synchronized public void remove(int id) throws SQLException, IllegalArgumentException {
        this.startConnection();

        String strDelete = "DELETE FROM " + DATABASE_NAME + "." + TABLE_NAME + " where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(strDelete);

        preparedStatement.setInt(1, id);

        System.out.println("[+] The SQL statement is: " + strDelete); // Echo For debugging
        ResultSet resultSet = (preparedStatement.execute()) ? preparedStatement.getResultSet() : null;
        System.out.println("[+] User deleted" + "\n"); // Echo For debugging

        this.closeConnection();
    }*/
}
