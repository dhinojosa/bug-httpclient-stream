package com.xyzcorp.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Runner {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Yo!");

        String urlString = "https://gist.githubusercontent" +
            ".com/dhinojosa/877425fb98a939a816e2c56f02bbedd0/raw" +
            "/84158bf19d58dca011cecf267eae0307232b76f3/countries.json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request =
            HttpRequest
                .newBuilder()
                .uri(URI.create(urlString))
                .build();

        CompletableFuture<HttpResponse<String>> future =
            client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        CompletableFuture<String> stringCompletableFuture = future
            .thenApply(HttpResponse::body)
            .thenApply(JSONDeserializer::processJson)
            .thenApplyAsync(m -> CountryFunctions.findLanguagesByRegion(m, "en", "Americas"));

        stringCompletableFuture
            .orTimeout(10, TimeUnit.SECONDS)
            .thenAccept(System.out::println);

        Thread.sleep(40000);
    }
}
