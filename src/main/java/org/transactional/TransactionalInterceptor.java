package org.transactional;

import lombok.RequiredArgsConstructor;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class TransactionalInterceptor implements MethodInterceptor {

    private final JdbcConnectionHolder connectionHolder;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (!method.isAnnotationPresent(Transactional.class)) {
                return proxy.invokeSuper(obj, args);
            } else {
                Object result;
                try {
                    connectionHolder.startTransaction();
                    result = proxy.invokeSuper(obj, args);
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
}
