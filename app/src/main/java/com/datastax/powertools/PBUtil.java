package com.datastax.powertools;

/*
 *
 * @author Sebastián Estévez on 10/18/18.
 *
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PBUtil {

    private final static Logger logger = LoggerFactory.getLogger(PBUtil.class);

    public static String getResponseAsString(StreamingOutput streamingOutput){

        ByteArrayOutputStream or = new ByteArrayOutputStream();
        try {
            streamingOutput.write(or);
            String responseAsString = new String(or.toByteArray(), "UTF-8");

            return responseAsString;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static StreamingOutput runPB(ProcessBuilder pb){
        logger.info("running command:" + pb.command());

        pb.directory(new File("../iaas"));

        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();

            int shellExitStatus = p.waitFor();
            System.out.println("Exit status" + shellExitStatus);

            InputStream is = p.getInputStream();

            StreamingOutput out = new StreamingOutput() {
                @Override
                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                    is.transferTo(outputStream);
                }
            };

            return out;

        }catch (Exception e){
             InputStream is = new ByteArrayInputStream(e.getMessage().getBytes());

             StreamingOutput out = new StreamingOutput() {
                @Override
                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                    is.transferTo(outputStream);
                }
            };
            return out;
        }
}

public static Map<String, Object> runPbAsInputStream(ProcessBuilder pb){

    HashMap<String, Object> streamAndStatus = new HashMap<>();

        logger.info("running command:" + pb.command());

        String commandString = ("\nRunning command: " + pb.command().toString() + "\n Resuts: \n");
        InputStream commandStream = new ByteArrayInputStream( commandString.getBytes());

        pb.directory(new File("../iaas"));

        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();

            int shellExitStatus = p.waitFor();
            System.out.println("Exit status" + shellExitStatus);

            //InputStreamReader isr = new InputStreamReader(pInputStream);
            //BufferedReader br = new BufferedReader(isr);
            //String response = br.lines().reduce((acc, x) -> acc = acc + "\n" + x).get();

            streamAndStatus.put("status", shellExitStatus);

            InputStream commandAndResutStream = new SequenceInputStream(commandStream, p.getInputStream());
            streamAndStatus.put("stream", commandAndResutStream);

            return streamAndStatus;

        }catch (Exception e){
            InputStream is = new ByteArrayInputStream( e.getMessage().getBytes());
            streamAndStatus.put("status", 2);
            streamAndStatus.put("stream", is);
            return streamAndStatus;
        }
    }

    public static String runPbAsString(ProcessBuilder pb){

        logger.info("running command:" + pb.command());

        pb.directory(new File("../iaas"));

        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();

            int shellExitStatus = p.waitFor();
            System.out.println("Exit status" + shellExitStatus);

            final InputStream pInputStream = p.getInputStream();

            InputStreamReader isr = new InputStreamReader(pInputStream);
            BufferedReader br = new BufferedReader(isr);
            String response = br.lines().reduce((acc, x) -> acc = acc + "\n" + x).get();

            return response;

        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
