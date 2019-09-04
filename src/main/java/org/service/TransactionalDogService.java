package org.service;

import lombok.RequiredArgsConstructor;
import org.JdbcConnectionHolder;
import org.model.Dog;

import java.util.List;

@RequiredArgsConstructor
public class TransactionalDogService implements DogService {

    private final DogService dogService;
    private final JdbcConnectionHolder connectionHolder;

    @Override
    public Dog findDogById(long id) {
        return dogService.findDogById(id);
    }

    @Override
    public void removeDog(Dog toRemove) {
        try {
            connectionHolder.startTransaction();
            dogService.removeDog(toRemove);
            connectionHolder.commitTransaction();
        } catch (Exception e) {
            connectionHolder.rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            connectionHolder.closeConnection();
        }
    }

    @Override
    public Dog saveDog(Dog toSave) {
        Dog saved;
        try {
            connectionHolder.startTransaction();
            saved = dogService.saveDog(toSave);
            connectionHolder.commitTransaction();
        } catch (Exception e) {
            connectionHolder.rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            connectionHolder.closeConnection();
        }
        return saved;
    }

    @Override
    public List<Dog> findAllDogs() {
        return dogService.findAllDogs();
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        Dog updated;
        try {
            connectionHolder.startTransaction();
            updated = dogService.updateDog(id, dog);
            connectionHolder.commitTransaction();
        } catch (Exception e) {
            connectionHolder.rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            connectionHolder.closeConnection();
        }
        return updated;
    }
}
