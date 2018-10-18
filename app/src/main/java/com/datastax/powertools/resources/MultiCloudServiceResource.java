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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.datastax.powertools.PBUtil.runPB;

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
    public String createAwsDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        ProcessBuilder pb = new ProcessBuilder("./deploy_aws.sh", "-r", region, "-s", deploymentName);

        return runPB(pb);
    }

    @GET
    @Timed
    @Path("/terminate-aws")
    @Produces(MediaType.APPLICATION_JSON)
    public String terminateAwsDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        ProcessBuilder pb = new ProcessBuilder("./teardown.sh", "-r", region, "-s", deploymentName);

        return runPB(pb);
    }
    @GET
    @Timed
    @Path("/create-gcp")
    @Produces(MediaType.APPLICATION_JSON)
    public String createGcpDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        // the region for gcp right now is hard coded in the params file
        ProcessBuilder pb = new ProcessBuilder("./deploy_gcp.sh", "-d", deploymentName);

        return runPB(pb);
    }

    @GET
    @Timed
    @Path("/terminate-gcp")
    @Produces(MediaType.APPLICATION_JSON)
    public String terminateGcpDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        ProcessBuilder pb = new ProcessBuilder("./teardown.sh", "-r", region, "-d", deploymentName);

        return runPB(pb);
    }
    @GET
    @Timed
    @Path("/create-azure")
    @Produces(MediaType.APPLICATION_JSON)
    public String createAzureDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        ProcessBuilder pb = new ProcessBuilder("./deploy_azure.sh", "-l", region, "-g", deploymentName);

        return runPB(pb);
    }

    @GET
    @Timed
    @Path("/terminate-azure")
    @Produces(MediaType.APPLICATION_JSON)
    public String terminateAzureDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        ProcessBuilder pb = new ProcessBuilder("./teardown.sh", "-r", region, "-g", deploymentName);

        return runPB(pb);
    }
}
