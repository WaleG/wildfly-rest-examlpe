package org.example.config.cdi;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Valentyn.Moliakov
 */
@Dependent
public class InitializerBean {
    @Resource(lookup = "java:jboss/datasources/wildflyDS")
    private DataSource myDataSource;

    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("liquibase/db-changelog.xml");
        return config;
    }

    @Produces
    @LiquibaseType
    public DataSource createDataSource() throws SQLException {
        return myDataSource;
    }

    @Produces
    @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }
}