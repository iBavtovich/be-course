package org.model;

import org.testng.annotations.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;

import static io.qala.datagen.RandomShortApi.english;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DogValidationTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void whenNameIsNull_validationFailed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(1);
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenNameIsEmpty_validationFailed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(1).setName("");
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenNameIsTooShort_validationFailed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(1).setName("");
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenNameIsMinimal_validationPassed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(1).setName(english(1));
        Set violations = validator.validate(dog);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void whenNameIsTooLong_validationFailed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(1).setName(english(101));
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenNameIs100Symbols_validationPassed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(1).setName(english(100));
        Set violations = validator.validate(dog);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void whenWeightIsZero_validationFailed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(1).setWeight(0).setName(english(5));
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenHeightIsZero_validationFailed() {
        Dog dog = new Dog().setDateOfBirth(new Date(0)).setHeight(0).setWeight(1).setName(english(5));
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenDateOfBirthInFuture_validationFailed() {
        Date now = new Date();
        Dog dog = new Dog().setDateOfBirth(new Date(now.getTime() + 1000000)).setHeight(1).setWeight(1).setName(english(5));
        Set violations = validator.validate(dog);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void whenValidDog_noViolations() {
        Date now = new Date();
        Dog dog = new Dog().setDateOfBirth(new Date(now.getTime() - 1)).setHeight(1).setWeight(1).setName(english(5));
        Set violations = validator.validate(dog);
        assertTrue(violations.isEmpty());
    }



}