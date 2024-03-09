package com.example.demo;

import com.example.demo.http.HttpClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class DemoApplication {
    private static final String API_KEY = "enter your JWT token"; // ex) DAFKSDJFklsjflskadjfklsadjf23zxckvlj...
    private static final String API_HOSTNAME = "enter hostname"; // ex) dmdwns2.github.com
    private static final int API_PORT = 80; // enter api_port
    private static final String API_SCHEME = "enter scheme"; // ex) http

    public static void main(String[] args) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        try (CloseableHttpClient httpClient = HttpClientFactory.create(new HttpHost(
                API_HOSTNAME, API_PORT, API_SCHEME), API_KEY)) {
            ParamFetcher paramFetcher = new ParamFetcher(httpClient, objectMapper);
            Param param = paramFetcher.getParam(); // use this
        }
    }
}
