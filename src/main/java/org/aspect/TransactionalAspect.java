package org.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.transactional.JdbcConnectionHolder;

@RequiredArgsConstructor
public class TransactionalAspect {

    private final JdbcConnectionHolder connectionHolder;

    public Object wrapExecutionInTransaction(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        try {
            connectionHolder.startTransaction();
            result = pjp.proceed();
            connectionHolder.commitTransaction();
        } catch (Exception e) {
            connectionHolder.rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            connectionHolder.closeConnection();
        }
        return result;
    }

}