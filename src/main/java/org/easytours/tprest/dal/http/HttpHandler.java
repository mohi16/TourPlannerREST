package org.easytours.tprest.dal.http;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.easytours.tpmodel.http.HttpMethod;
import org.easytours.tpmodel.http.HttpStatusCode;
import org.easytours.tpmodel.utils.TimeUtils;
import org.easytours.tpmodel.utils.Triple;
import org.easytours.tprest.config.Config;
import org.easytours.tprest.utils.Pair;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class HttpHandler {
    public static Triple<Double, Long, byte[]> sendMapQuestRequest(String from, String to) throws Exception {
        HttpResponse<String> jsonResponse = sendRequest(
                Config.getConfig().getMapquestDirectionsUrl() +
                        "?key=" + Config.getConfig().getMapquestApiKey() +
                        "&from=" + from +
                        "&to=" + to +
                        "&unit=k",
                HttpMethod.GET
        );
        if (!HttpStatusCode.isSame(HttpStatusCode.OK, jsonResponse.statusCode())) {
            throw new Exception("something went wrong but i dont know what :(");
        }

        String body = jsonResponse.body();
        ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        //String json = "{distance:";
        String sessionId = "";
        double distance = 0;
        long time = 0;
        if (node.has("route")) {
            JsonNode route = node.get("route");
            if (route.has("distance")) {
                distance = route.get("distance").asDouble();
            } else {
                throw new Exception("no distance");
            }
            //json += ",time:";
            if (route.has("formattedTime")) {
                System.out.println(route.get("formattedTime").asText());
                LocalTime ltime = LocalTime.parse(route.get("formattedTime").asText(), DateTimeFormatter.ofPattern("H:m:s"));
                String[] timeParts = route.get("formattedTime").asText().split(":");
                time = TimeUtils.constructTime(
                        Integer.parseInt(timeParts[0]),
                        Integer.parseInt(timeParts[1]),
                        Integer.parseInt(timeParts[2])
                );
            } else {
                throw new Exception("no time");
            }
            //json += "}";
            if (route.has("sessionId")) {
                sessionId = route.get("sessionId").asText();
            } else {
                throw new Exception("no session id");
            }
        }

        HttpResponse<byte[]> imageResponse = sendRequestBinary(
                Config.getConfig().getMapquestStaticMapUrl() +
                        "?key=" + Config.getConfig().getMapquestApiKey() +
                        "&session=" + sessionId,
                HttpMethod.GET
        );
        if (!HttpStatusCode.isSame(HttpStatusCode.OK, imageResponse.statusCode())) {
            throw new Exception("something went wrong but i dont know what :(");
        }

        return new Triple<>(distance, time, imageResponse.body());
    }

    public static HttpResponse<String> sendRequest(String url, HttpMethod method) throws Exception {
        HttpClient client = getClient();

        return client.send(getRequest(url, method.name(), HttpRequest.BodyPublishers.noBody()), HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<byte[]> sendRequestBinary(String url, HttpMethod method) throws Exception {
        HttpClient client = getClient();

        return client.send(getRequest(url, method.name(), HttpRequest.BodyPublishers.noBody()), HttpResponse.BodyHandlers.ofByteArray());
    }

    private static HttpClient getClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    private static HttpRequest getRequest(String url, String method, HttpRequest.BodyPublisher body) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .method(method, body)
                .header("Content-Type", "application/json")
                .build();
    }
}
