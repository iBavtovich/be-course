package org.service;

import lombok.RequiredArgsConstructor;
import org.JdbcConnectionHolder;
import org.dao.DogDao;
import org.model.Dog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class SimpleDogService implements DogService {

    private final DogDao dogDao;
    private final JdbcConnectionHolder connectionHolder;

    @Override
    public Dog findDogById(long id) {
        return dogDao.findDogById(id);
    }

    @Override
    public void removeDog(Dog toRemove) {
        Connection connection = null;
        try {
            connection = connectionHolder.acquireConnection();
            connection.setAutoCommit(false);
            dogDao.removeDog(toRemove);
            connection.commit();
        } catch (Exception e) {
            rollbackTransactions(connection);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Dog saveDog(Dog toSave) {
        Dog dog;
        Connection connection = null;
        try {
            connection = connectionHolder.acquireConnection();
            connection.setAutoCommit(false);
            dog = dogDao.saveDog(toSave);
            connection.commit();
        } catch (Exception e) {
            rollbackTransactions(connection);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return dog;
    }

    @Override
    public List<Dog> findAllDogs() {
        return dogDao.findAllDogs();
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        Dog updatedDog;
        Connection connection = null;
        try {
            connection = connectionHolder.acquireConnection();
            connection.setAutoCommit(false);
            updatedDog = dogDao.updateDog(id, dog);
            connection.commit();
        } catch (Exception e) {
            rollbackTransactions(connection);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return updatedDog;
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void rollbackTransactions(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
