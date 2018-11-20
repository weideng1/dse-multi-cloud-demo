package com.datastax.powertools;

/*
 *
 * @author Sebastián Estévez on 11/19/18.
 *
 */


import org.glassfish.jersey.server.ChunkedOutput;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StreamUtil {

    private final ChunkedOutput<String> out;

    public StreamUtil(ChunkedOutput<String> out) {
        this.out = out;
    }

    public int getOr99(CompletableFuture<Integer> integerFuture) {
        try {
            return integerFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return 99;
        }
    }

    public <U> Map<String, Object> streamToOut(Map<String, Object> streamAndStatus) {
        try {
            InputStream chunkIS = ((InputStream) streamAndStatus.get("stream"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(chunkIS));
            String line;
            while ((line = reader.readLine()) != null) {
                out.write(line);

                // For testing
                Thread.sleep(50);

            }
            chunkIS.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return streamAndStatus;
    }

}
