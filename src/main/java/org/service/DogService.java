package org.service;

import org.model.Dog;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DogService {
    @Transactional(readOnly = true)
    Dog findDogById(long id);

    void removeDog(Dog toRemove);

    Dog saveDog(Dog toSave);

    @Transactional(readOnly = true)
    List<Dog> findAllDogs();

    Dog updateDog(Long id, Dog dog);
}
