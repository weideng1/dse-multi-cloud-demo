package com.datastax.powertools;

import com.datastax.powertools.resources.MultiCloudServiceResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 * Created by sebastianestevez on 3/26/18.
 */
public class MultiCloudServiceApplication extends Application<MultiCloudServiceConfig> {

    public static void main(String[] args) throws Exception{
        new MultiCloudServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MultiCloudServiceConfig> bootstrap) {

        bootstrap.addBundle(new AssetsBundle("/assets/","/", "index.html"));

        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        super.initialize(bootstrap);
    }

    public void run(MultiCloudServiceConfig MultiCloudServiceConfig, Environment environment) throws Exception {

        //TODO: Add managed here

        // Enable CORS for npm dev mode
        final FilterRegistration.Dynamic cors = environment.servlets()
                .addFilter("cors", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,authorization,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true,
                "/*");


        MultiCloudServiceResource MultiCloudServiceResource = new MultiCloudServiceResource(MultiCloudServiceConfig);
        environment.jersey().register(MultiCloudServiceResource);
    }
}
