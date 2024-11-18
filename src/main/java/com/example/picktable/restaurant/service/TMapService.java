package com.example.picktable.restaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.picktable.restaurant.domain.dto.PathResponseDTO;
import com.example.picktable.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TMapService {
    @Value("${tmap.api.key}")
    private String tmapKey;

    @Transactional
    public Map<String, Double> getCoordinates(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?version=1&addressFlag=F00&coordType=WGS84GEO&fullAddr=%s&appKey=%s",
                            encodedAddress, tmapKey)))
                    .header("accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return parseCoordinates(response.body());
        } catch (InterruptedException | IOException e) {
            System.err.println("Failed to fetch coordinates: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    //경도,위도 받아서 주소로 변환 api Reverse Geocoding 사용 https://apis.openapi.sk.com/tmap/geo/reversegeocoding
    @Transactional
    public String getAddressByCoordinates(double startX, double startY) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String requestUrl = String.format(
                    "https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=1&lat=%f&lon=%f&appKey=%s",
                    startY, startX, tmapKey
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String body = response.body();
            JsonNode rootNode = mapper.readTree(body);
            JsonNode addressInfo = rootNode.path("addressInfo");
            JsonNode dong = addressInfo.path("legalDong");
            return dong.asText();
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to retrieve legalDong: " + e.getMessage());
            return "error";
        }
    }

    @Transactional
    public String getAddressByCoordinates2(double startX, double startY) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String requestUrl = String.format(
                    "https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=1&lat=%f&lon=%f&appKey=%s",
                    startY, startX, tmapKey
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String body = response.body();
            JsonNode rootNode = mapper.readTree(body);
            JsonNode addressInfo = rootNode.path("addressInfo");
            JsonNode dong = addressInfo.path("legalDong");
            JsonNode bunji = addressInfo.path("bunji");
            String bunjiString = bunji.asText();
            bunjiString= String.valueOf(bunjiString.charAt(0));
            String fullAddress = dong.asText() +" "+ bunjiString;
            return fullAddress;
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to retrieve legalDong: " + e.getMessage());
            return "error";
        }
    }

    @Transactional
    public Map<String, Double> parseCoordinates(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);
        JsonNode coordinateNode = rootNode.path("coordinateInfo").path("coordinate");

        if (!coordinateNode.isMissingNode() && coordinateNode.isArray() && coordinateNode.size() > 0) {
            JsonNode firstCoordinate = coordinateNode.get(0);
            double lat = firstCoordinate.get("newLat").asDouble();
            double lon = firstCoordinate.get("newLon").asDouble();
            Map<String, Double> result = new HashMap<>();
            result.put("latitude", lat);
            result.put("longitude", lon);
            return result;
        } else {
            System.err.println("No coordinates found in the response");
            return Collections.emptyMap();
        }
    }

    //api 사용 안한 직선거리 계산
    @Transactional
    public double calculateDistance(double startLat, double startLon, double endX, double endY) {
        // 직선 거리 계산
        double earthRadius = 6371.01; // 지구의 반지름(km)
        double dLat = Math.toRadians(endY - startLat);
        double dLon = Math.toRadians(endX - startLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endY)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // 거리 반환 (킬로미터 단위)
    }

    //최적경로 반환 https://apis.openapi.sk.com/transit/routes
    @Transactional
    public int totalTime(String startX, String startY, String endX, String endY, int lang, String format, int count, String searchDttm) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonBody = String.format(
                    "{\"startX\":\"%s\",\"startY\":\"%s\",\"endX\":\"%s\",\"endY\":\"%s\",\"lang\":%d,\"format\":\"%s\",\"count\":%d,\"searchDttm\":\"%s\"}",
                    startX, startY, endX, endY, lang, format, count, searchDttm);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apis.openapi.sk.com/transit/routes"))
                    .header("accept", "application/json")
                    .header("appKey", tmapKey)
                    .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody)) // 변경된 부분
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            if (body.contains("error")) {
                System.out.println("에러입니다");
                JsonNode rootNode = mapper.readTree(body);
                JsonNode errorNode = rootNode.path("error");
                System.out.println("Error response: " + errorNode.toString());
                return -1;
            }

            JsonNode rootNode = mapper.readTree(body);
            JsonNode itinerariesNode = rootNode.path("metaData").path("plan").path("itineraries");
            if (itinerariesNode.isArray() && itinerariesNode.size() > 0) {
                JsonNode firstItinerary = itinerariesNode.get(0);
                JsonNode totalTimeInfo = firstItinerary.path("totalTime");
                return totalTimeInfo.asInt();
            }
            return 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to calculate transit time: " + e.getMessage());
            return -1;
        }
    }

    @Transactional
    public JsonNode getJsonByTransitRoute(String departure, String destination, int lang, String format, int count, String searchDttm) {
        Map<String, Double> depCoordinates = this.getCoordinates(departure);
        Map<String, Double> destCoordinates = this.getCoordinates(destination);

        String startY = Double.toString(depCoordinates.get("latitude"));
        String startX = Double.toString(depCoordinates.get("longitude"));
        String endY = Double.toString(destCoordinates.get("latitude"));
        String endX = Double.toString(destCoordinates.get("longitude"));

        try {
            String jsonBody = String.format(
                    "{\"startX\":\"%s\",\"startY\":\"%s\",\"endX\":\"%s\",\"endY\":\"%s\",\"lang\":%d,\"format\":\"%s\",\"count\":%d,\"searchDttm\":\"%s\"}",
                    startX, startY, endX, endY, lang, format, count, searchDttm);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apis.openapi.sk.com/transit/routes"))
                    .header("accept", "application/json")
                    .header("appKey", tmapKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(body);

            if (rootNode.has("result") && rootNode.get("result").get("status").asInt() == 11) {
                return getJsonByWalkRoute(departure, destination);
            }
            return rootNode;

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    @Transactional
    public PathResponseDTO getTransitRoute2(String departure, String destination, int lang, String format, int count, String searchDttm) {
        Map<String, Double> depCoordinates = this.getCoordinates(departure);
        Map<String, Double> destCoordinates = this.getCoordinates(destination);

        String startY = Double.toString(depCoordinates.get("latitude"));
        String startX = Double.toString(depCoordinates.get("longitude"));
        String endY = Double.toString(destCoordinates.get("latitude"));
        String endX = Double.toString(destCoordinates.get("longitude"));

        try {
            String jsonBody = String.format(
                    "{\"startX\":\"%s\",\"startY\":\"%s\",\"endX\":\"%s\",\"endY\":\"%s\",\"lang\":%d,\"format\":\"%s\",\"count\":%d,\"searchDttm\":\"%s\"}",
                    startX, startY, endX, endY, lang, format, count, searchDttm);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apis.openapi.sk.com/transit/routes"))
                    .header("accept", "application/json")
                    .header("appKey", tmapKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(body);

            if (rootNode.has("error")) {
                System.out.println("에러입니다");
                return null;
            }
            return mapper.treeToValue(rootNode, PathResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to calculate transit time: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public JsonNode getJsonByWalkRoute(String departure, String destination) {
        Map<String, Double> depCoordinates = this.getCoordinates(departure);
        Map<String, Double> destCoordinates = this.getCoordinates(destination);

        Float startY = depCoordinates.get("latitude").floatValue();
        Float startX = depCoordinates.get("longitude").floatValue();
        Float endY = destCoordinates.get("latitude").floatValue();
        Float endX = destCoordinates.get("longitude").floatValue();

        try {
            String encodedDeparture = URLEncoder.encode(departure, StandardCharsets.UTF_8.toString());
            String encodedDestination = URLEncoder.encode(destination, StandardCharsets.UTF_8.toString());

            String jsonBody = String.format(
                    "{\"startX\":\"%f\",\"startY\":\"%f\",\"endX\":\"%f\",\"endY\":\"%f\",\"startName\":\"%s\",\"endName\":\"%s\"}",
                    startX, startY, endX, endY, encodedDeparture, encodedDestination);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1"))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .header("appKey", tmapKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(body);

            if (rootNode.has("error")) {
                System.out.println("에러입니다");
                return rootNode;
            }

            return rootNode;

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to calculate transit time: " + e.getMessage());
            return null;
        }
    }
}

