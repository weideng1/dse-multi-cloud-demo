package com.datastax.powertools.resources;

import com.codahale.metrics.annotation.Timed;
import com.datastax.powertools.MultiCloudServiceConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by sebastianestevez on 6/1/18.
 */
@Path("/v0/multi-cloud-service")
public class MultiCloudServiceResource {
    private final MultiCloudServiceConfig config;
    private final static Logger logger = LoggerFactory.getLogger(MultiCloudServiceResource.class);

    public static class JSONCloudSettings implements Serializable {
        @JsonProperty
        private String testValue;
    }

    public MultiCloudServiceResource(MultiCloudServiceConfig config) {
        // TODO: add managed here
        this.config = config;
    }

    @GET
    @Timed
    @Path("/create-aws")
    @Produces(MediaType.APPLICATION_JSON)
    public String createAwsDeployment(String deploymentName, String region) {
        ProcessBuilder pb = new ProcessBuilder("deploy_aws.sh", "-r", region, "-s", deploymentName);
        pb.directory(new File("/home"));

        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();

            InputStream pInputStream = p.getInputStream();

            int shellExitStatus = p.waitFor();
            System.out.println("Exit status" + shellExitStatus);

            InputStreamReader isr = new InputStreamReader(pInputStream);
            BufferedReader br = new BufferedReader(isr);
            String response = br.lines().reduce((acc, x) -> acc = acc + "\n" + x).get();

            pInputStream.close();

            logger.info("response from cli call:" + response);

            return response;

        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
