package org;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class TransactionalProxy implements InvocationHandler {

    private final Object targetObject;
    private final JdbcConnectionHolder connectionHolder;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            connectionHolder.startTransaction();
            result = method.invoke(targetObject, args);
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
