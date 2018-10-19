package com.datastax.powertools;

import org.testng.annotations.Test;

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
        String result = PBUtil.runPB(pb);

        System.out.println(result);
    }
}