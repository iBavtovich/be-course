package org.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@Getter @Setter
public class DataSourceFactoryBean implements FactoryBean<DataSource> {

    private String jndiObjectLocation;

    @Override
    public DataSource getObject() {
        try {
            Context ctx = new InitialContext();
            Object obj = ctx.lookup("java:/comp/env/" + jndiObjectLocation);
            return (DataSource) obj;
        } catch (Exception e) {
            throw new RuntimeException("Can't acquire DataSource via JNDI", e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }
}
