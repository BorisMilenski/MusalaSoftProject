package entities;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    List<T> get() throws SQLException;
    void add(T t) throws SQLException;
    void edit(int id, T t) throws SQLException, IllegalArgumentException;
    void remove(int id) throws SQLException, IllegalArgumentException;
}
