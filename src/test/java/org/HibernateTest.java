package org;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("hibernate")
@ContextConfiguration(value = {"classpath:spring-dispatcher-servlet.xml", "classpath:spring-test-config.xml"})
public @interface HibernateTest {
}
