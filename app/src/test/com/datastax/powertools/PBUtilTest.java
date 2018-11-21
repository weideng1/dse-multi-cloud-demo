package com.datastax.powertools;

import org.testng.annotations.Test;

import javax.ws.rs.core.StreamingOutput;

import java.io.InputStream;
import java.util.Map;

import static org.testng.Assert.*;


/*
 *
 * @author Sebastián Estévez on 10/19/18.
 *
 */


public class PBUtilTest {

    @Test
    public void testRunPB() {
        ProcessBuilder pb = new ProcessBuilder("ls");

        String result = PBUtil.runPbAsString(pb);
        StreamingOutput resSO = PBUtil.runPB(pb);
        Map<String, Object> streamAndStatus = PBUtil.runPbAsInputStream(pb);

        System.out.println(result);
    }
}