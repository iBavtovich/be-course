package org.dao;

import lombok.RequiredArgsConstructor;
import org.h2.jdbcx.JdbcDataSource;
import org.model.Dog;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class H2DogDao implements DogDao {

    private final DataSource dataSource;

    public H2DogDao() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE DOG (\n" +
                    "    ID number(22) auto_increment primary key,\n" +
                    "    NAME varchar(100) not null,\n" +
                    "    BIRTH_DATE timestamp,\n" +
                    "    HEIGHT number(3),\n" +
                    "    WEIGHT number(3)\n" +
                    ")");
            statement.execute("INSERT INTO DOG(NAME, BIRTH_DATE, HEIGHT, WEIGHT) values ('BBA', NOW(), 1, 1)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.dataSource = dataSource;
    }

    @Override
    public Dog findDogById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM DOG WHERE ID = " + id);
            resultSet.next();

            return Dog.fromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeDog(Dog toRemove) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM DOG WHERE ID = " + toRemove.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dog saveDog(Dog toSave) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(new StringBuilder().append("INSERT INTO DOG(NAME, BIRTH_DATE, HEIGHT, WEIGHT) values (")
                                                 .append("'" + toSave.getName() + "'").append(", ")
                                                 .append(toSave.getDateOfBirth() != null ? "'" + new Timestamp(toSave.getDateOfBirth().getTime()) + "'" : null).append(", ")
                                                 .append(toSave.getHeight()).append(", ")
                                                 .append(toSave.getWeight()).append(")").toString(),
                    Statement.RETURN_GENERATED_KEYS);
            statement.getGeneratedKeys().next();
            return toSave.setId(statement.getGeneratedKeys().getLong("ID"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Dog> findAllDogs() {
        List<Dog> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM DOG");

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
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(new StringBuilder().append("UPDATE DOG SET NAME = ")
                                                                         .append("'" + forUpdate.getName() + "'").append(", ")
                                                                         .append(forUpdate.getDateOfBirth() != null ?
                                                                                 "BIRTH_DATE = " + "'" + new Timestamp(forUpdate.getDateOfBirth().getTime()) + "'" : null).append(", ")
                                                                         .append("HEIGHT = " + forUpdate.getHeight()).append(", ")
                                                                         .append("WEIGHT = " + forUpdate.getWeight()).append(" WHERE ID = " + id).toString());
            if (updatedRows != 1) {
                throw new NoSuchElementException();
            }
            return forUpdate.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
