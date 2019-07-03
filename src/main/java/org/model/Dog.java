package org.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class Dog {

    private Long id;
    private String name;
    private Date dateOfBirth;
    private Integer height;
    private Integer weight;
}
