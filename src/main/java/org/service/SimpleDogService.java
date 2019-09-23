package org.service;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dao.DogDao;
import org.model.Dog;
import org.transactional.Transactional;

import java.util.List;

@Setter
@NoArgsConstructor
public class SimpleDogService implements DogService{

    private DogDao dogDao;

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
