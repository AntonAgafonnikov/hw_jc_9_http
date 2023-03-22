package ru.netology.task1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String FACTS_ABOUT_CATS_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public  static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build()
        ) {

            // создание объекта запроса с произвольными заголовками
            HttpGet request = new HttpGet(FACTS_ABOUT_CATS_URL);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            // отправка запроса
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                List<Facts> facts = mapper.readValue(
                        response.getEntity().getContent(),
                        new TypeReference<>() {}
                );

                facts.stream()
                        .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                        .forEach(System.out::println);
            }
        }
    }
}