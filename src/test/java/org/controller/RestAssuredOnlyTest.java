package org.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.model.Dog;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.*;
import static io.restassured.RestAssured.given;
import static org.controller.DogController.API_PATH;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.Long;

public class RestAssuredOnlyTest {

    private static final String BASE_PATH = "/be-course-1.0-SNAPSHOT/";
    public static final String BASE_URI = "http://localhost";

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = 8080;
        RestAssured.basePath = BASE_PATH;
    }

    @Test
    public void whenTryToGetNotExistedDog_notFoundStatusCode() {
        getDogById(negativeLong()).then().statusCode(404);
    }

    @Test
    public void whenTryToAddInvalidDog_exception() {
        Dog toSave = new Dog().setHeight(integer(1, 100)).setWeight(integer(1, 100)).setName(english(101));
        saveDog(toSave).then().statusCode(400);
    }

    @Test
    public void getByIdReturnsCorrectDog() {
        long existingId = 0L;
        Response response = getDogById(existingId);
        response.then().statusCode(200);
        Dog fromDb = response.as(Dog.class);
        assertEquals(fromDb.getId().longValue(), existingId);
        assertEquals(fromDb.getName(), "ABBA");
    }

    @Test
    public void whenAddNewDog_itSaved() {
        Dog toSave = new Dog().setHeight(integer(1, 100)).setWeight(integer(1, 100)).setName(english(1, 100));

        Response response = saveDog(toSave);
        response.then().statusCode(200).body(notNullValue());
        Dog fromResponse = response.as(Dog.class);

        assertNotNull(fromResponse.getId());
        Dog fromDb = getDogById(fromResponse.getId()).as(Dog.class);
        assertEquals(fromDb, fromResponse);
    }

    @Test
    public void whenRemoveDog_itIsRemoved() {
        Dog toSave = new Dog().setHeight(integer(1, 100)).setWeight(integer(1, 100)).setName(english(1, 100));
        Dog fromResponse = saveDog(toSave).as(Dog.class);

        Response deleteResponse = removeDogById(fromResponse.getId());
        deleteResponse.then().statusCode(204);

        getDogById(fromResponse.getId()).then().body(Matchers.isEmptyOrNullString());
    }

    private Response getDogById(long id) {
        return given().when().get(API_PATH + "/" + id);
    }

    private Response saveDog(Dog toSave) {
        return given().body(toSave).contentType(ContentType.JSON).when().post(API_PATH);
    }

    private Response removeDogById(Long id) {
        return given().contentType(ContentType.JSON).when().delete(API_PATH + "/" + id);
    }
}
