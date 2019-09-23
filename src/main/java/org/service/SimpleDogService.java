package org.service;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dao.DogDao;
import org.model.Dog;

import java.util.List;

@Setter
@NoArgsConstructor
public class SimpleDogService implements DogService{

    private DogDao dogDao;

    @Override
    public Dog findDogById(long id) {
        return dogDao.findDogById(id);
    }

    @Override
    public void removeDog(Dog toRemove) {
        dogDao.removeDog(toRemove);
    }

    @Override
    public Dog saveDog(Dog toSave) {
        return dogDao.saveDog(toSave);
    }

    @Override
    public List<Dog> findAllDogs() {
        return dogDao.findAllDogs();
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        return dogDao.updateDog(id, dog);
    }
}
