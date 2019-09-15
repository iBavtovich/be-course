package org.aspect;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target(value=METHOD)
public @interface ExecutionLogged {
}
