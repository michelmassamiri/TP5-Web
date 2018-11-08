package fr.ub.m2gl;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class ContactManager extends ResourceConfig {
    public ContactManager() {
        // Register resources and providers using package-scanning.
        packages("fr.ub.m2gl");

        register(MyObjectMapperProvider.class);

        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");
   }

}
