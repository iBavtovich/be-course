package org.controller;

import lombok.RequiredArgsConstructor;
import org.model.Dog;
import org.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = DogController.API_PATH)
public class DogController {

    public static final String API_PATH = "/api/dogs";

    private final DogService dogService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getDogById(@PathVariable Long id) {
        Dog dog = dogService.findDogById(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @PutMapping("/{id}")
    public Dog updateDog(@Valid @RequestBody Dog dog, @PathVariable Long id) {
        return dogService.updateDog(id, dog);
    }

    @PostMapping()
    public Dog addDog(@Valid @RequestBody Dog dog) {
        if (dog.getId() != null) {
            throw new IllegalArgumentException();
        }
        return dogService.saveDog(dog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id) {
        Dog dogById = dogService.findDogById(id);
        if (dogById == null) {
            return ResponseEntity.notFound().build();
        }
        dogService.removeDog(dogById);
        return ResponseEntity.noContent().build();
    }

    public List<Dog> getAllDogs() {
        return dogService.findAllDogs();
    }

}
