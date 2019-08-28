package org.dao;

import lombok.RequiredArgsConstructor;
import org.model.Dog;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class DBDogDao implements DogDao {

    private final DataSource dataSource;

    @Override
    public Dog findDogById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM DOG WHERE ID = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return Dog.fromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeDog(Dog toRemove) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("DELETE FROM DOG WHERE ID = ?");
            statement.setLong(1, toRemove.getId());
            statement.execute();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            rollbackTransactions(connection);
        } finally {
            handleException(connection, statement);
        }
    }

    @Override
    public Dog saveDog(Dog toSave) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement("INSERT INTO DOG(NAME, BIRTH_DATE, HEIGHT, WEIGHT) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, toSave.getName());
            statement.setTimestamp(2, toSave.getDateOfBirth() != null ? new Timestamp(toSave.getDateOfBirth().getTime()) : null);
            statement.setInt(3, toSave.getHeight());
            statement.setInt(4, toSave.getWeight());
            statement.execute();
            statement.getGeneratedKeys().next();
            connection.commit();
            return toSave.setId(statement.getGeneratedKeys().getLong(1));
        } catch (Exception e) {
            e.printStackTrace();
            rollbackTransactions(connection);
        } finally {
            handleException(connection, statement);
        }
        return null;
    }

    @Override
    public List<Dog> findAllDogs() {
        List<Dog> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DOG");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(Dog.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Dog updateDog(Long id, Dog forUpdate) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("UPDATE DOG SET NAME = ?, BIRTH_DATE = ?, HEIGHT = ?, WEIGHT = ? WHERE ID = ?");

            statement.setString(1, forUpdate.getName());
            statement.setTimestamp(2, forUpdate.getDateOfBirth() != null ? new Timestamp(forUpdate.getDateOfBirth().getTime()) : null);
            statement.setInt(3, forUpdate.getHeight());
            statement.setInt(4, forUpdate.getWeight());
            statement.setLong(5, forUpdate.getId());
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 1) {
                throw new NoSuchElementException();
            }
            connection.commit();
            return forUpdate.setId(id);
        }catch (Exception e) {
            e.printStackTrace();
            rollbackTransactions(connection);
        } finally {
            handleException(connection, statement);
        }
        return null;
    }

    private void rollbackTransactions(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleException(Connection connection, PreparedStatement statement) {
        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
