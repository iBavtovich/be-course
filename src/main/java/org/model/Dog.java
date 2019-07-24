package org.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

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
}
