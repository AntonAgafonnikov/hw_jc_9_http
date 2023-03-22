package ru.netology.task2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class DailyContentNASA {
    private final String copyright;
    private final String date; //TODO попробовать сделать Date
    private final String explanation;
    private final String hdurl;
    private final String mediaType;
    private final String serviceVersion;
    private final String title;
    private final String url;

    public DailyContentNASA (
            @JsonProperty("copyright") String copyright,
            @JsonProperty("date") String date,
            @JsonProperty("explanation") String explanation,
            @JsonProperty("hdurl") String hdurl,
            @JsonProperty("media_type") String mediaType,
            @JsonProperty("service_version") String serviceVersion,
            @JsonProperty("title") String title,
            @JsonProperty("url") String url
    ) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.mediaType = mediaType;
        this.serviceVersion = serviceVersion;
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "\n Daily content" +
                "\n copyright: " + copyright +
                "\n date: " + date +
                "\n explanation: " + explanation +
                "\n hdurl: " + hdurl +
                "\n media_type: " + mediaType +
                "\n serviceVersion: " + serviceVersion +
                "\n title: " + title +
                "\n url: " + url;
    }

}
