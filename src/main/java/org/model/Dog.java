package org.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.integer;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class Dog {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Past
    private Date dateOfBirth;

    @Min(1)
    private int height;

    @Min(1)
    private int weight;

    public static Dog fromResultSet(ResultSet resultSet) throws SQLException {
        Date birthDate = resultSet.getTimestamp("BIRTH_DATE") != null
                ? new Date(resultSet.getTimestamp("BIRTH_DATE").getTime())
                : null;
        return new Dog().setName(resultSet.getString("NAME"))
                        .setDateOfBirth(birthDate)
                        .setId(resultSet.getLong("ID"))
                        .setHeight(resultSet.getInt("HEIGHT"))
                        .setWeight(resultSet.getInt("WEIGHT"));
    }

    public static Dog random() {
        return new Dog().setWeight(integer(1, 100)).setHeight(integer(1, 100)).setName(english(1, 100));
    }
}
