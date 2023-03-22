package ru.netology.task2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static final String DAILY_CONTENT_NASA_URL =
            "https://api.nasa.gov/planetary/apod?api_key=CYfR9u0vF50S0fmAajft0rKcW1PvJ0HfcCt36eii";
    public static final String RESOURCES_DIRECTORY = "src/main/resources/";
    public static ObjectMapper mapper = new ObjectMapper();

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
            HttpGet request = new HttpGet(DAILY_CONTENT_NASA_URL);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            // отправка запроса
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                DailyContentNASA dailyContentNASA = mapper.readValue(
                        response.getEntity().getContent(),
                        new TypeReference<>() {
                        }
                );

                // получим адреc следующего запроса и сформируем имя файла из него
                String dailyMediaURL = dailyContentNASA.getUrl();
                String fileName = getFileName(dailyMediaURL);

                // ВАРИАНТ 1: создание объекта запроса
                request = new HttpGet(dailyMediaURL);
                // отправка запроса,
                try (CloseableHttpResponse responseMedia = httpClient.execute(request);
                     // получение и запись массива байтов в файлов
                     FileOutputStream fos = new FileOutputStream(RESOURCES_DIRECTORY + fileName)) {
                    fos.write(responseMedia.getEntity().getContent().readAllBytes());
                }
                if(new File(RESOURCES_DIRECTORY + fileName).exists()) {
                    System.out.println(fileName + " - успешно сохранён в директорию: " + RESOURCES_DIRECTORY);
                }

                // ВАРИАНТ 2: сохраним файл из запроса с помощью библиотеки Apache Commons IO
                FileUtils.copyURLToFile(new URL(dailyMediaURL),
                        new File(RESOURCES_DIRECTORY + "APACHE_" + fileName));

                if(new File(RESOURCES_DIRECTORY + "APACHE_" + fileName).exists()) {
                    System.out.println("APACHE_" + fileName + " - успешно сохранён в директорию: " + RESOURCES_DIRECTORY);
                }
            }
        }
    }

    private static String getFileName(String url) {
        int index = url.lastIndexOf('/') + 1;
        return url.substring(index);
    }
}
