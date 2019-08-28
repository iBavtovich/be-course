package org.controller;

import org.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

@ContextConfiguration(value = {"classpath:spring-dispatcher-servlet.xml", "classpath:spring-test-config.xml"})
public class DogControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DogController dogController;

    @Test
    public void whenAddDog_itIsSaved() {
        Dog dog = Dog.random();
        int beforeSave = dogController.getAllDogs().size();
        Dog returned = dogController.addDog(dog);

        List<Dog> fromDb = dogController.getAllDogs();
        assertNotNull(returned.getId());
        assertEquals(fromDb.size(), beforeSave + 1);
        assertTrue(fromDb.contains(returned));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void exceptionIsThrown_whenTryToAddDofWithId() {
        dogController.addDog(Dog.random().setId(1L));
    }

    @Test
    public void getDog_returnedDogFromDb() {
        Dog dog = Dog.random();
        Dog returned = dogController.addDog(dog);

        Dog fromDb = (Dog) dogController.getDogById(returned.getId()).getBody();
        assertThat(fromDb).isEqualToComparingFieldByField(returned);
    }

    @Test
    public void wheDogUpdated_returnUpdatedAndSaveInDb() {
        Dog dog = Dog.random();
        Dog returned = dogController.addDog(dog);
        returned.setWeight(10).setHeight(20).setName("Luck").setDateOfBirth(new Date(0));

        Dog updated = dogController.updateDog(returned, returned.getId());
        assertEquals(updated.getHeight(), 20);
        assertEquals(updated.getWeight(), 10);
        assertEquals(updated.getName(), "Luck");
        assertEquals(updated.getDateOfBirth(), new Date(0));

        Dog fromDb = (Dog) dogController.getDogById(returned.getId()).getBody();
        assertThat(fromDb).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void whenRemove_itIsRemoved() {
        Dog dog = Dog.random();
        Dog returned = dogController.addDog(dog);

        dogController.deleteDog(returned.getId());

        Dog fromDb = (Dog) dogController.getDogById(returned.getId()).getBody();
        assertNull(fromDb);
    }
}