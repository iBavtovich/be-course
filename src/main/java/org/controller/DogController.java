package org.controller;

import org.model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequestMapping(value = "/api/dogs")
public class DogController {

    private static volatile BlockingQueue<Dog> dogs = new LinkedBlockingQueue<Dog>() {{
        this.add(new Dog().setId(0L).setName("ABBA").setDateOfBirth(new Date()).setHeight(1).setWeight(1));
    }};
    private static Long index = 1L;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDogById(@PathVariable Long id) {
        Dog dog = findDogById(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @PutMapping("/{id}")
    public Dog updateDog(@Valid @RequestBody Dog dog, @PathVariable Long id) {
        Dog dogById = findDogById(id);
        if (dogById != null) {
            dogs.remove(dogById);
        }
        dogs.add(dog);
        return dog.setId(id);
    }

    @PostMapping()
    public Dog addDog(@Valid @RequestBody Dog dog) {
        if (dog.getId() != null) {
            throw new IllegalArgumentException();
        }
        dogs.add(dog.setId(index++));
        return dog;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id) {
        Dog dogById = findDogById(id);
        if (dogById == null) {
            return ResponseEntity.notFound().build();
        }
        dogs.remove(dogById);
        return ResponseEntity.noContent().build();
    }

    public List<Dog> getAllDogs() {
        return new ArrayList<>(dogs);
    }

    private Dog findDogById(@PathVariable Long id) {
        for (Dog dog : dogs) {
            if (dog.getId().equals(id)) {
                return dog;
            }
        }
        return null;
    }
}
