package org.service;

import lombok.RequiredArgsConstructor;
import org.Transactional;
import org.dao.DogDao;
import org.model.Dog;

import java.util.List;

@RequiredArgsConstructor
public class SimpleDogService implements DogService {

    private final DogDao dogDao;

    @Override
    public Dog findDogById(long id) {
        return dogDao.findDogById(id);
    }

    @Override @Transactional
    public void removeDog(Dog toRemove) {
        dogDao.removeDog(toRemove);
    }

    @Override @Transactional
    public Dog saveDog(Dog toSave) {
        return dogDao.saveDog(toSave);
    }

    @Override
    public List<Dog> findAllDogs() {
        return dogDao.findAllDogs();
    }

    @Override @Transactional
    public Dog updateDog(Long id, Dog dog) {
        return dogDao.updateDog(id, dog);
    }
}
