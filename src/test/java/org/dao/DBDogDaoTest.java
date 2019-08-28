package org.dao;

import org.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static io.qala.datagen.RandomShortApi.english;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@ContextConfiguration(value = {"classpath:spring-dispatcher-servlet.xml", "classpath:spring-test-config.xml"})
public class DBDogDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DogDao dogDao;

    @Test
    public void testSqlInjection() {
        Dog dog = dogDao.saveDog(Dog.random().setName("\"' blah"));
        assertNotNull(dog.getId());
        assertEquals(dog.getName(), "\"' blah");
    }

    @Test
    public void whenSaveDogWithNameLength100_itIsSaved() {
        Dog toSave = Dog.random().setName(english(100));
        Dog dog = dogDao.saveDog(toSave);
        assertNotNull(dog.getId());
    }

    @Test(expectedExceptions = Exception.class)
    public void whenSaveDogWithNameLength101_exceptionIsThrown() {
        Dog toSave = Dog.random().setName(english(101));
        Dog dog = dogDao.saveDog(toSave);
        assertNotNull(dog.getId());
    }
}