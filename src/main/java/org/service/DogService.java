package org.service;

import org.aspect.ExecutionLogged;
import org.model.Dog;
import org.transactional.Transactional;

import java.util.List;

public interface DogService {
    @ExecutionLogged
    Dog findDogById(long id);

    @Transactional
    void removeDog(Dog toRemove);

    @Transactional
    Dog saveDog(Dog toSave);

    List<Dog> findAllDogs();

    @Transactional
    Dog updateDog(Long id, Dog dog);
}
