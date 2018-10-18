package com.datastax.powertools;

/*
 *
 * @author Sebastián Estévez on 10/18/18.
 *
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PBUtil {

    private final static Logger logger = LoggerFactory.getLogger(PBUtil.class);

    public static String runPB(ProcessBuilder pb){

        pb.directory(new File("../iaas"));

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
