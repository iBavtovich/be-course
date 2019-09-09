package org.transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TransactionalProxy implements InvocationHandler {

    private Map<String, Method> targetMethods = new HashMap<>();
    private final Object targetObject;
    private final JdbcConnectionHolder connectionHolder;

    public TransactionalProxy(Object targetObject, JdbcConnectionHolder connectionHolder) {
        this.targetObject = targetObject;
        this.connectionHolder = connectionHolder;
        for (Method targetMethod : targetObject.getClass().getDeclaredMethods()) {
            targetMethods.put(targetMethod.getName(), targetMethod);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;

        if (!isTargetMethodAnnotated(method)) {
            return method.invoke(targetObject, args);
        }

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

    private boolean isTargetMethodAnnotated(Method proxyMethod) {
        Method targetMethod = targetMethods.get(proxyMethod.getName());
        if (targetMethod != null) {
            return targetMethod.isAnnotationPresent(Transactional.class);
        }
        return false;
    }
}
