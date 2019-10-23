package org.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.model.Dog;

import java.util.List;

@RequiredArgsConstructor
public class HibernateDogDao implements DogDao {

    private final SessionFactory sessionFactory;

    @Override
    public Dog findDogById(long id) {
        return getCurrentSession().find(Dog.class, id);
    }

    @Override
    public void removeDog(Dog toRemove) {
        getCurrentSession().remove(toRemove);
    }

    @Override
    public Dog saveDog(Dog toSave) {
        Session currentSession = getCurrentSession();
        return toSave.setId((Long) currentSession.save(toSave));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Dog> findAllDogs() {
        return getCurrentSession().createQuery("from Dog").getResultList();
    }

    @Override
    public Dog updateDog(Long id, Dog dog) {
        getCurrentSession().update(dog.setId(id));
        return dog;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
