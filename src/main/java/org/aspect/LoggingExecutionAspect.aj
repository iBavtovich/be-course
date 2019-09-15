package org.aspect;

public aspect LoggingExecutionAspect {
    static final void println(String s){ System.out.println(s); }

    pointcut logExecution() :
            call(* *(..)) && @annotation(ExecutionLogged);

    before() : logExecution() {
        println("Started execution of method: " + thisJoinPoint.getSignature());
    }

    after() : logExecution() {
        println("Finished execution of method: " + thisJoinPoint.getSignature());
    }
}