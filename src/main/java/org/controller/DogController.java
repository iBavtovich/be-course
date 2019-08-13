package org.controller;

import lombok.RequiredArgsConstructor;
import org.dao.DogDao;
import org.model.Dog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = DogController.API_PATH)
public class DogController {

    public static final String API_PATH = "/api/dogs";

    private final DogDao dogDao;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDogById(@PathVariable Long id) {
        Dog dog = dogDao.findDogById(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @PutMapping("/{id}")
    public Dog updateDog(@Valid @RequestBody Dog dog, @PathVariable Long id) {
        return dogDao.updateDog(id, dog);
    }

    @PostMapping()
    public Dog addDog(@Valid @RequestBody Dog dog) {
        if (dog.getId() != null) {
            throw new IllegalArgumentException();
        }
        dogDao.saveDog(dog);
        return dog;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id) {
        Dog dogById = dogDao.findDogById(id);
        if (dogById == null) {
            return ResponseEntity.notFound().build();
        }
        dogDao.removeDog(dogById);
        return ResponseEntity.noContent().build();
    }

    public List<Dog> getAllDogs() {
        return dogDao.findAllDogs();
    }

}
