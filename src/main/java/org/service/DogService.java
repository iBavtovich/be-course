package org.service;

import org.model.Dog;
import org.transactional.Transactional;

import java.util.List;

public interface DogService {
    Dog findDogById(long id);

    void removeDog(Dog toRemove);

    Dog saveDog(Dog toSave);

    List<Dog> findAllDogs();

    Dog updateDog(Long id, Dog dog);
}
