package org.controller;

import org.model.Dog;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

public class DogControllerTest {

    private DogController dogController = new DogController();

    @Test
    public void whenAddDog_itIsSaved() {
        Dog dog = new Dog();
        int beforeSave = dogController.getAllDogs().size();
        Dog returned = dogController.addDog(dog);

        List<Dog> fromDb = dogController.getAllDogs();
        assertNotNull(returned.getId());
        assertEquals(fromDb.size(), beforeSave + 1);
        assertTrue(fromDb.contains(returned));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrown_whenTryToAddDofWithId() {
        dogController.addDog(new Dog().setId(1L));
    }

    @Test
    public void getDog_returnedDogFromDb() {
        Dog dog = new Dog();
        Dog returned = dogController.addDog(dog);

        Dog fromDb = (Dog) dogController.getDogById(returned.getId()).getBody();
        assertEquals(fromDb, returned);
    }

    @Test
    public void wheDogUpdated_returnUpdatedAndSaveInDb() {
        Dog dog = new Dog();
        Dog returned = dogController.addDog(dog);
        returned.setWeight(10).setHeight(20).setName("Luck").setDateOfBirth(new Date(0));

        Dog updated = dogController.updateDog(returned, returned.getId());
        assertEquals(updated.getHeight(), 20);
        assertEquals(updated.getWeight(), 10);
        assertEquals(updated.getName(), "Luck");
        assertEquals(updated.getDateOfBirth(), new Date(0));

        Dog fromDb = (Dog) dogController.getDogById(returned.getId()).getBody();
        assertEquals(fromDb, updated);
    }

    @Test
    public void whenRemove_itIsRemoved() {
        Dog dog = new Dog();
        Dog returned = dogController.addDog(dog);

        dogController.deleteDog(returned.getId());

        Dog fromDb = (Dog) dogController.getDogById(returned.getId()).getBody();
        assertNull(fromDb);
    }
}