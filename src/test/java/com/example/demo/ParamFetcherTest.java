package com.example.demo;

import com.example.demo.http.HttpClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.*;

import java.io.IOException;

class ParamFetcherTest {
    private static final String API_KEY = "someApiKey";
    private static MockWebServer mockWebServer;
    private static CloseableHttpClient httpClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        httpClient = HttpClientFactory.create(new HttpHost("localhost", mockWebServer.getPort(), "http"), API_KEY);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.close();
        httpClient.close();
    }

    @Test
    @DisplayName("input-param 정상 동작 테스트")
    void input_param_is_resolved() throws InterruptedException, IOException {
        // given
        ParamFetcher paramFetcher = new ParamFetcher(httpClient, new ObjectMapper());
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("{\"param\": \"someParam\"}"));

        // when
        Param param = paramFetcher.getParam();
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        // then
        Assertions.assertEquals("/param", recordedRequest.getPath());
        Assertions.assertEquals("Bearer " + API_KEY, recordedRequest.getHeader("Authorization"));
        Assertions.assertEquals("someParam", param.getParam());
    }

    @Test
    @DisplayName("input-param 요청 실패 테스트")
    void input_param_request_fails() {
        // given
        ParamFetcher paramFetcher = new ParamFetcher(httpClient, new ObjectMapper());
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        // when
        IOException exception = Assertions.assertThrows(IOException.class, paramFetcher::getParam);

        // then
        Assertions.assertTrue(exception.getMessage().contains("500"));
    }
}