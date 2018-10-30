package com.datastax.powertools.resources;

import com.codahale.metrics.annotation.Timed;
import com.datastax.powertools.MultiCloudServiceConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.*;
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
    @Consumes(MediaType.APPLICATION_JSON)
    public String createAwsDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region, HashMap <String, String> params) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        String paramString = paramsToAWSString(params);
        //ProcessBuilder pb = new ProcessBuilder("./deploy_aws.sh", "-r", region, "-s", deploymentName);
        //./deploy_aws.sh -r us-east-2 -s test1 -p "ParameterKey=KeyName,ParameterValue=assethubkey  ParameterKey=CreateUser,ParameterValue=sebastian.estevez-datastax.com ParameterKey=Org,ParameterValue=presales ParameterKey=VPC,ParameterValue=vpc-75c83d1c ParameterKey=AvailabilityZones,ParameterValue=us-east-2a,us-east-2b,us-east-2c ParameterKey=Subnets,ParameterValue=subnet-4bc4ee01,subnet-5fcd3f36,subnet-ac485dd4"
        ProcessBuilder pb = new ProcessBuilder("./deploy_aws.sh", "-r", region, "-s", deploymentName, "-p", paramString);

        return runPB(pb);
    }

    private String paramsToAWSString(HashMap<String, String> params) {
        String paramString = "\""+
                "ParameterKey=KeyName,ParameterValue=assethubkey " +
                "ParameterKey=VPC,ParameterValue=vpc-75c83d1c " +
                "ParameterKey=AvailabilityZones,ParameterValue=us-east-2a,us-east-2b,us-east-2c " +
                "ParameterKey=Subnets,ParameterValue=subnet-4bc4ee01,subnet-5fcd3f36,subnet-ac485dd4 ";
        for (Map.Entry<String, String> paramKV : params.entrySet()) {
            paramString+= String.format("\""+ "ParameterKey=%s,ParameterValue=%s ", paramKV.getKey(), paramKV.getValue());
        }
        paramString +="\"";

        return paramString;
    }

    private void validateParams(HashMap<String, String> params) {
        if(params.get("user") == null){
            params.put("user", "none");
        }
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
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGcpDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region, HashMap<String, String> params) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        String paramString = paramsToGCPString(params);

        // the region for gcp right now is hard coded in the params file
        ProcessBuilder pb = new ProcessBuilder("./deploy_gcp.sh", "-d", deploymentName, "-p", paramString);

        return runPB(pb);
    }

    private String paramsToGCPString(HashMap<String, String> params) {
        //      --properties "string-key:'string-value',integer-key:12345"
        String paramsString = "zones:'us-east1-b'," +
                "network: 'default'," +
                "machineType: 'n1-standard-2'," +
                "nodesPerZone: 3," +
                "dataDiskType: 'pd-ssd'," +
                "diskSize: 60," +
                "sshKeyValue: 'AAAAB3NzaC1yc2EAAAADAQABAAACAQCzmNOzPiUcl45ZOJSh/5kUU7dmm3xUp+j++l9zLxLov/De9RukvHWPTRNtAHdWR0EatTSqsmlvDUm8UkKVuPdQ223MiZYlL53Q3ZXzGnAzShtbL8VIMvH+9jlaNM/yfA6Ox4jE/sLcoy5giML0/3LNkzqHTJVxmGpAqUt4DJL6MfIpbOLBhdDJVKuVO2ERS/k55hekvnhKRqlKICMt62MzoR78poZM8CmbMOs3YgJDqumXaJRaUKtWBbhGmdU6hf2Jd3TRoI6V8rwrR40HZrdtSi2ECc1HRRwO1EIJ61Q924TFfrY8M+fGnmy15jmXBWcja+yOkyQV9K/GdUs9yHvmaW+svSzCpAatvny+ccxR+6bU9H6M7Tab2uuP3tpS+seCeD5+OADCaCQz8sdcTmrtTNQhUcTKgaD1ONkNQE6Fth8OLxPfDsyl5pNv1gXZU5uRCUIgBJXNsA92KltcI3ltsl9BXbkH9Bcum+Uhf/66/24/sr9LzpRyOjkGxk4lwKZUZ19jPx4O03hWDAeCwFCesqDu0P2rX3xbUPwSgPTdjyR9bzkPNret8zD+oNPMWKISPy43atDUgR04/vmsjW0/6EUb/l7vX8vYVta3S2l1c9OsAkGdhg/xxw0N44jGG65wYQ0HttbrzSdHULIOe2lfe9KsLEWXjVcSQIJdT1s9CQ=='\"";
        String labels = " --labels ";
        for (Map.Entry<String, String> paramKV : params.entrySet()) {
            if (paramKV.getKey().equals("deployerapp") || paramKV.getKey().equals("createuser") || paramKV.getKey().equals("org")){
                labels += String.format("%s=%s,", paramKV.getKey(), paramKV.getValue());
            }else {
                paramsString+= String.format("%s:%s,", paramKV.getKey(), paramKV.getValue());
            }
        }
        paramsString += labels;

        return paramsString;
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
    @Consumes(MediaType.APPLICATION_JSON)
    public String createAzureDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region, HashMap<String, String> params) {
        if (region == null || deploymentName == null){
            return "please provide region and deploymentName as query parameters";
        }
        String paramString = paramsToAzureString(params);
        ProcessBuilder pb = new ProcessBuilder("./deploy_azure.sh", "-l", region, "-g", deploymentName);

        return runPB(pb);
    }

    private String paramsToAzureString(HashMap<String, String> params) {
        // az group deployment create --resource-group jason
        // --parameters "{\"newStorageAccountName\":
        // {\"value\": \"jasondisks321\"},\"adminUsername\": {\"value\": \"jason\"},
        // \"adminPassword\": {\"value\": \"122130869@qq\"},
        // \"dnsNameForPublicIP\": {\"value\": \"jasontest321\"}}"
        // --template-uri https://raw.githubusercontent.com/Azure/azure-quickstart-templates/master/docker-simple-on-ubuntu/azuredeploy.json

        String paramsString = "{\n" +
                "  \"location\": {\n" +
                "    \"value\": \"westus\"\n" +
                "  },\n" +
                "  \"nodeCount\": {\n" +
                "    \"value\": 3\n" +
                "  },\n" +
                "  \"vmSize\": {\n" +
                "    \"value\": \"Standard_DS4_v2\"\n" +
                "  },\n" +
                "  \"namespace\": {\n" +
                "    \"value\": \"dc0\"\n" +
                "  },\n" +
                "  \"adminUsername\": {\n" +
                "    \"value\": \"ubuntu\"\n" +
                "  },\n" +
                "  \"publicIpOnNodes\": {\n" +
                "    \"value\": \"yes\"\n" +
                "  },\n" +
                "  \"vnetName\": {\n" +
                "    \"value\": \"vnet\"\n" +
                "  },\n" +
                "  \"subnetName\": {\n" +
                "    \"value\": \"subnet\"\n" +
                "  }\n" +
                "}\n";
        JSONObject jsonParams = new JSONObject(paramsString);

        for (Map.Entry<String, String> paramKV : params.entrySet()) {
            JSONObject value = new JSONObject(String.format("{value: %s}", paramKV.getValue()));
            jsonParams.append(paramKV.getKey(), value);
        }

        return jsonParams.toString();
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

    @GET
    @Timed
    @Path("/gather-ips")
    @Produces(MediaType.APPLICATION_JSON)
    public String gatherIps(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null)
            region = "us-east-2";
        if (deploymentName == null)
            return "please provide deploymentName as a query parameter";
        ProcessBuilder pb = new ProcessBuilder("./gather_ips.sh", "-r", region, "-s", deploymentName, "-d", deploymentName, "-g", deploymentName);

        return runPB(pb);
    }

    @GET
    @Timed
    @Path("/lcm-install-deployment")
    @Produces(MediaType.APPLICATION_JSON)
    public String lcmInstallDeployment(@QueryParam("deploymentName") String deploymentName, @QueryParam("region") String region) {
        if (region == null)
            region = "us-east-2";
        if (deploymentName == null)
            return "please provide deploymentName as a query parameter";
        String ips = gatherIps(deploymentName, region);

        logger.info("IP Addresses (raw): \n" + ips);
        logger.info("IP Addresses (escape utils): \n"+StringEscapeUtils.escapeJava(ips));


        return lcmInstallIps(ips);
    }

    @GET
    @Timed
    @Path("/lcm-install-ips")
    @Produces(MediaType.APPLICATION_JSON)
    public String lcmInstallIps(@QueryParam("ips") String ips) {
        ips = ips.replaceAll("\n", ";");

        logger.info("IP Addresses (replaced): \n" + ips);

        Pattern p = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}");   // the pattern to search for
        Matcher m = p.matcher(ips);

        String lcmIp = "";
        if (m.find()) {
            lcmIp = m.group(0);
        }

        logger.info("LCM IP: \n" + lcmIp);

        ProcessBuilder pb = new ProcessBuilder(
                "python",
                "../setup.py",
                "-lcm", lcmIp,
                "-u", "ubuntu",
                // TODO: this needs to be dynamic at some point
                "-k", "/dse-multi-cloud-demo/config/assethubkey",
                // TODO: make dynamic
                "-n", "dse-cluster",
                "-s", ips);

        return runPB(pb);
    }

    @GET
    @Timed
    @Path("/create-multi-cloud")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createMultiCloudDeployment(@QueryParam("deploymentName") String deploymentName, HashMap<String,String> params) {
        validateParams(params);
        CompletableFuture<String> awsFuture
                = CompletableFuture.supplyAsync(() -> "AWS: \n" + createAwsDeployment(deploymentName, "us-east-2", params));
        CompletableFuture<String> gcpFuture
                = CompletableFuture.supplyAsync(() -> "\nGCP: \n" + createGcpDeployment(deploymentName, "ignored", params));
        CompletableFuture<String> azureFuture
                = CompletableFuture.supplyAsync(() -> "\nAzure:\n " + createAzureDeployment(deploymentName, "westus2", params));

        try {
            String combined = Stream.of(awsFuture, gcpFuture, azureFuture)
                    .map(CompletableFuture::join)
                    .collect(Collectors.joining(""));
            return combined;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Timed
    @Path("/terminate-multi-cloud")
    @Produces(MediaType.APPLICATION_JSON)
    public String terminateMultiCloudDeployment(@QueryParam("deploymentName") String deploymentName) {
        CompletableFuture<String> awsFuture
                = CompletableFuture.supplyAsync(() -> "AWS: \n" + terminateAwsDeployment(deploymentName, "us-east-2"));
        CompletableFuture<String> gcpFuture
                = CompletableFuture.supplyAsync(() -> "\nGCP: \n" + terminateGcpDeployment(deploymentName, "ignored"));
        CompletableFuture<String> azureFuture
                = CompletableFuture.supplyAsync(() -> "\nAzure:\n " + terminateAzureDeployment(deploymentName, "westus2"));

        try {
            String combined = Stream.of(awsFuture, gcpFuture, azureFuture)
                    .map(CompletableFuture::join)
                    .collect(Collectors.joining(""));
            return combined;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
