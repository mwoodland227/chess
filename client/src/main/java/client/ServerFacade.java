package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import dataclasses.AuthData;
import handler.LoginRequest;
import handler.LogoutRequest;
import handler.RegisterRequest;
import com.google.gson.Gson;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String url;


    public ServerFacade(String url) {
        this.url = url;
    }

    public AuthData register(String username, String password, String email) throws ClientException{
        var requestData = new RegisterRequest(username, password, email);
        var request = buildRequest("Post", "/user", requestData, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(String username, String password) throws ClientException{
        var requestData = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", requestData, null);
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

    private HttpResponse<String> sendRequest(HttpRequest request) throws ClientException{
        try{
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ClientException("HTTP request failed: " + e.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException{
        int status = response.statusCode();
        if(!isSuccessful(status)){
            String body = response.body();
            if(body != null){
                throw new ClientException("Server error: " + body);
            }
            throw new ClientException("HTTP " + status);
        }
        if(responseClass != null){
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {

        return status / 100 == 2;
    }
}
