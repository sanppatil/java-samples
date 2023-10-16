package com.enodation.service;

import com.enodation.config.AppConfiguration;
import com.enodation.handler.EventHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class LiveStreamConsumer {

    @Autowired
    private BlockingQueue<String> dataQueue;

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private AppConfiguration appConfig;

    public void readFromLivestream() throws IOException, InterruptedException {

        String streamUrl = "http://localhost:8888/weather-stream";

        HttpURLConnection connection = getConnection(streamUrl);
        int responseCode = connection.getResponseCode();

        while (responseCode != HttpURLConnection.HTTP_OK) {
            if (responseCode == HttpURLConnection.HTTP_CONFLICT) {
                connection.disconnect();
                throw new RuntimeException("Connection refused due to conflict. Check that the maxConnections query string value is identical across connections.");

            } else {
                connection.disconnect();
                throw new RuntimeException("Unexpected response code: " + responseCode);
            }
        }

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("event-handler-%d")
                .priority(Thread.MAX_PRIORITY)
                .build();
        ExecutorService executorService = Executors.newSingleThreadExecutor(factory);
        executorService.submit(eventHandler);

        // The stream is always compressed using GZIP
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = reader.readLine();
        while (line != null) {
            if (StringUtils.isNotBlank(line)) {
                dataQueue.put(line);
            }
            line = reader.readLine();
        }

        reader.close();
        connection.disconnect();
        executorService.shutdownNow();
    }

    private HttpURLConnection getConnection(final String streamUrl) throws IOException {
        URL url = new URL(streamUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout((int) Duration.ofSeconds(60).toMillis());
        connection.setConnectTimeout((int) Duration.ofSeconds(10).toMillis());
        connection.setInstanceFollowRedirects(false);
        return connection;
    }


}
