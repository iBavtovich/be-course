package org.dao;

import org.model.Dog;

import java.util.List;

public interface DogDao {

    Dog findDogById(long id);

    void removeDog(Dog toRemove);

    Dog saveDog(Dog toSave);

    List<Dog> findAllDogs();

    Dog updateDog(Long id, Dog dog);
}
