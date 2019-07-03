package org.config;

import org.beans.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnotherConfig {

    @Bean
    public A a() {
        return new A();
    }
}
