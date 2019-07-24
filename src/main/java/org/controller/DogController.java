package org.controller;

import org.model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = DogController.API_PATH)
public class DogController {

    public static final String API_PATH = "/api/dogs";

    private static List<Dog> dogs = new CopyOnWriteArrayList<Dog>() {{
        this.add(new Dog().setId(0L).setName("ABBA").setDateOfBirth(new Date()).setHeight(1).setWeight(1));
    }};
    private static AtomicLong index = new AtomicLong(1);

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
        dogs.add(dog.setId(index.incrementAndGet()));
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
