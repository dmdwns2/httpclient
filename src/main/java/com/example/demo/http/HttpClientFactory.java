package com.example.demo.http;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.protocol.HttpContext;

import java.util.Objects;

public class HttpClientFactory {
    private HttpClientFactory() {
    }

    public static CloseableHttpClient create(HttpHost defaultHost, String apiKey) {
        return HttpClients.custom()
                .setRoutePlanner(createDefaultHostResolver(defaultHost))
                .addInterceptorLast(createApiKeyInterceptor(apiKey))
                .build();
    }

    private static HttpRoutePlanner createDefaultHostResolver(HttpHost defaultHost) {
        return new DefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE) {
            @Override
            public HttpRoute determineRoute(HttpHost host,
                                            HttpRequest request, HttpContext context) throws HttpException {
                if (Objects.isNull(host)) {
                    host = defaultHost;
                }
                return super.determineRoute(host, request, context);
            }
        };
    }

    private static HttpRequestInterceptor createApiKeyInterceptor(String apiKey) {
        return (httpRequest, httpContext) ->
                httpRequest.setHeader("Authorization", String.join(" ", "Bearer", apiKey));
    }
}
