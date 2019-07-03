package org.config;

import org.beans.Dog;
import org.beans.Owner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Dog dog() {
        return new Dog();
    }

    @Bean
    public Owner owner() {
        return new Owner();
    }
}
