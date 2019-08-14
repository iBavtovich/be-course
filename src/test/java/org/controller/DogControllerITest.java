package org.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.hamcrest.Matchers;
import org.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.controller.DogController.API_PATH;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.lang.Long;


@Test
@ContextConfiguration(value = {"classpath:spring-dispatcher-servlet.xml"})
@WebAppConfiguration
public class DogControllerITest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    @BeforeClass
    public void initContext() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @AfterClass
    public void resetContext() {
        RestAssuredMockMvc.reset();
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
        long existingId = 1L;
        Dog fromDb = getDogById(existingId).as(Dog.class);
        assertEquals(fromDb.getId().longValue(), existingId);
        assertEquals(fromDb.getName(), "ABBA");
    }

    @Test
    public void whenAddNewDog_itSaved() {
        Dog toSave = new Dog().setHeight(integer(1, 100)).setWeight(integer(1, 100)).setName(english(1, 100));

        MockMvcResponse response = saveDog(toSave);
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

        MockMvcResponse deleteResponse = removeDogById(fromResponse.getId());
        deleteResponse.then().statusCode(204);

        getDogById(fromResponse.getId()).then().body(Matchers.isEmptyOrNullString());
    }

    private MockMvcResponse getDogById(long id) {
        return given().when().get(API_PATH + "/" + id);
    }

    private MockMvcResponse saveDog(Dog toSave) {
        return given().body(toSave).contentType(ContentType.JSON).when().post(API_PATH);
    }

    private MockMvcResponse removeDogById(Long id) {
        return given().contentType(ContentType.JSON).when().delete(API_PATH + "/" + id);
    }
}
