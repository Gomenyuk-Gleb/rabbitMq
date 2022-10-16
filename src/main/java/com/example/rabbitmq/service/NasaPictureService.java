package com.example.rabbitmq.service;

import com.example.rabbitmq.request.PictureRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NasaPictureService {

    Map<String, byte[]> pictureDB = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate;

    public byte[] findLargePicture(PictureRequest pictureRequest) throws JsonProcessingException {
        var buildURL = buildURL(pictureRequest.getSol(), pictureRequest.getCamera());
        var forEntity = restTemplate.getForEntity(buildURL, String.class);
        var objectMapper = new ObjectMapper();
        var jsonNode = objectMapper.readTree(forEntity.getBody());
        var img_src = jsonNode.findValues("img_src").parallelStream()
                .map(x -> getHeader(x.asText()))
                .max((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        return downloadPicture(img_src.get().getKey());
    }

    private byte[] downloadPicture(String url) {
        byte[] picture;
        var response = restTemplate.getForEntity(url, byte[].class);
        picture = response.getBody();
        return picture;
    }

    public String buildURL(Integer sol, String cameraName) {
        return "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + sol + "&camera=" + cameraName + "&api_key=DEMO_KEY";
    }

    public Pair<String, Long> getHeader(String url) {
        final var factory = new HttpComponentsClientHttpRequestFactory();
        final var httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);

        return Pair.of(url, forEntity.getHeaders().getContentLength());
    }

    public void addPictureToDB(String commandId, byte[] nasaPictureBytes) {
        pictureDB.put(commandId, nasaPictureBytes);
    }

    public byte[] findPictureInDB(String commandId) {
        var picture = pictureDB.get(commandId);
        if (Objects.nonNull(picture)) {
            return picture;
        }
        throw new RuntimeException("This picture dose not exist");
    }
}
