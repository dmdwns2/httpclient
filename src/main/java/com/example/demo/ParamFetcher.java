package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;


@AllArgsConstructor
public class ParamFetcher {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public Param getParam() throws IOException {
        return parseAndValidate(sendRequest());
    }

    private HttpResponse sendRequest() throws IOException {
        try {
            HttpResponse response = httpClient.execute(null, generateRequest());
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("[ERROR] Status code : " + response.getStatusLine().getStatusCode());
            }
            return response;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private HttpRequest generateRequest() {
        return new HttpGet("ex) /param"); // enter url you need
    }

    private Param parseAndValidate(HttpResponse response) throws IOException {
        try {
            Param param = objectMapper.readValue(response.getEntity().getContent(), Param.class);
            if (StringUtils.isBlank(param.getParam())) {
                throw new IOException("ex) [ERROR] param is empty");
            }
            return param;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
