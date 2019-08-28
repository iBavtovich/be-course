package org;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class JdbcConnectionHolder {

    private final DataSource dataSource;
    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public Connection acquireConnection() {
        try {
            if (connectionThreadLocal.get() == null || connectionThreadLocal.get().isClosed()) {
                connectionThreadLocal.set(dataSource.getConnection());
            }
        } catch(SQLException e){
                throw new RuntimeException(e);
        }
        return connectionThreadLocal.get();
    }
}
