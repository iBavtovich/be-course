package org.dao;

import lombok.RequiredArgsConstructor;
import org.model.Dog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
public class DBDogDao implements DogDao {

    private static final RowMapper<Dog> DOG_ROW_MAPPER = (rs, rowNum) -> Dog.fromResultSet(rs);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Dog findDogById(long id) {
        List<Dog> dogs = jdbcTemplate.query("SELECT * FROM DOG WHERE ID = ?", new Object[]{id}, DOG_ROW_MAPPER);
        return dogs.isEmpty() ? null : dogs.get(0);
    }

    @Override
    public void removeDog(Dog toRemove) {
        jdbcTemplate.update("DELETE FROM DOG WHERE ID = ?", toRemove.getId());
    }

    @Override
    public Dog saveDog(Dog toSave) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO DOG(NAME, BIRTH_DATE, HEIGHT, WEIGHT) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, toSave.getName());
            ps.setTimestamp(2, toSave.getDateOfBirth() != null ? new Timestamp(toSave.getDateOfBirth().getTime()) : null);
            ps.setInt(3, toSave.getHeight());
            ps.setInt(4, toSave.getWeight());
            return ps;
        }, keyHolder);
        return toSave.setId(keyHolder.getKey().longValue());
    }

    @Override
    public List<Dog> findAllDogs() {
        return jdbcTemplate.query("SELECT * FROM DOG", DOG_ROW_MAPPER);
    }

    @Override
    public Dog updateDog(Long id, Dog forUpdate) {
        jdbcTemplate.update("UPDATE DOG SET NAME = ?, BIRTH_DATE = ?, HEIGHT = ?, WEIGHT = ? WHERE ID = ?",
                forUpdate.getName(), forUpdate.getDateOfBirth() != null ? new Timestamp(forUpdate.getDateOfBirth().getTime()) : null,
                forUpdate.getHeight(), forUpdate.getWeight(), forUpdate.getId());
        return forUpdate.setId(id);
    }
}
