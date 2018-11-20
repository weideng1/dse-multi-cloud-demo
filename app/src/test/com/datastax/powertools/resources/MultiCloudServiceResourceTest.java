package com.datastax.powertools.resources;

import com.datastax.powertools.MultiCloudServiceConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.*;


/*
 *
 * @author Sebastián Estévez on 10/19/18.
 *
 */


public class MultiCloudServiceResourceTest {

    MultiCloudServiceConfig config;
    MultiCloudServiceResource msr;
    HashMap<String, String> params;

    @Test
    public void testLcmInstallIps() {

        String ips = "18.224.212.187:172.31.36.89:AWS:0\r"+
        "18.188.99.215:172.31.20.98:AWS:1\r"+
        "18.191.102.18:172.31.5.139:AWS:2\r"+
        "35.231.127.21:10.142.0.8:GCP:0\r"+
        "35.190.169.205:10.142.0.10:GCP:1\r"+
        "35.237.73.192:10.142.0.9:GCP:2\r"+
        "40.80.158.77:10.0.0.6:Azure:0\r"+
        "40.78.58.209:10.0.0.5:Azure:1\r"+
        "104.42.156.24:10.0.0.4:Azure:2\r";

        msr.lcmInstallIps(ips, params);
    }

    @BeforeMethod
    public void setUp() {
        params = new HashMap<String, String>();
        config = new MultiCloudServiceConfig();
        msr = new MultiCloudServiceResource(config);

    }

    @Test
    public void testLcmInstallDeployment() {
        msr.lcmInstallDeployment(params, "test1", "us-east-2");
    }
}