package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;

import dataclasses.AuthData;
import handler.RegisterRequest;
import com.google.gson.Gson;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String url;


    public ServerFacade(String url) {
        this.url = url;
    }

    public AuthData register(String username, String password, String email) {
        var requestData = new RegisterRequest(username, password, email);
        var request = buildRequest("Post", "/user", requestData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url + path))
                .method(method, makeRequestBody(body));
        if(body != null){
            builder.setHeader("Content-Type", "application/json");
        }
        if(authToken != null){
            builder.setHeader("Authorization", authToken);
        }
        return builder.build();
    }

    private BodyPublisher makeRequestBody(Object request){
        if(request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }
}
