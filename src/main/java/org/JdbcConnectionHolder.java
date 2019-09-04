package org;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

    public void startTransaction() {
        Connection connection;
        try {
            connection = acquireConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commitTransaction() {
        Connection connection;
        try {
            connection = acquireConnection();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollbackTransaction() {
        Connection connection;
        try {
            connection = acquireConnection();
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            acquireConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
