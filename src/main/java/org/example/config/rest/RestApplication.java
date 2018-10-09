package org.example.config.rest;

import io.swagger.jaxrs.config.BeanConfig;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppObjectMapperProvider;
import org.example.rest.ProjectEndpoint;
import org.example.rest.UserEndpoint;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
@Slf4j
public class RestApplication extends Application {
    public RestApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("v1");
        beanConfig.setDescription("App for users and projects management");
        beanConfig.setTitle("App Title");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("org.example.rest");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        log.info(">getClasses()");

        Set<Class<?>> resources = new HashSet<>();
        resources.add(ProjectEndpoint.class);
        resources.add(UserEndpoint.class);

        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        return resources;
    }

    @Override
    public Set<Object> getSingletons() {
        log.info(">getSingletons()");

        Set<Object> singletons = new HashSet<>();
        singletons.add(AppObjectMapperProvider.class);

        return singletons;
    }
}