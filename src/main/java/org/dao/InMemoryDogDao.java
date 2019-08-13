package org.dao;

import org.model.Dog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDogDao implements DogDao {

    private static List<Dog> dogs = new CopyOnWriteArrayList<Dog>() {{
        this.add(new Dog().setId(0L).setName("ABBA").setDateOfBirth(new Date()).setHeight(1).setWeight(1));
    }};
    private static AtomicLong index = new AtomicLong(1);

    @Override
    public Dog findDogById(long id) {
        for (Dog dog : dogs) {
            if (dog.getId().equals(id)) {
                return dog;
            }
        }
        return null;
    }

    @Override
    public void removeDog(Dog toRemove) {
        dogs.remove(toRemove);
    }

    @Override
    public Dog saveDog(Dog toSave) {
        dogs.add(toSave.setId(index.incrementAndGet()));
        return toSave;
    }

    @Override
    public List<Dog> findAllDogs() {
        return new ArrayList<>(dogs);
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        Dog dogById = findDogById(id);
        if (dogById != null) {
            removeDog(dogById);
            saveDog(dog);
        }
        return dog.setId(id);
    }
}
