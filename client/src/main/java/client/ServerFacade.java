package client;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import dataclasses.AuthData;
import dataclasses.GameData;
import dataclasses.UserData;
import handler.*;
import com.google.gson.Gson;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String url;


    public ServerFacade(String url) {
        this.url = url;
    }

    public UserData register(String username, String password, String email) throws ClientException{
        var requestData = new RegisterRequest(username, password, email);
        var request = buildRequest("Post", "/user", requestData, null);
        var response = sendRequest(request);
        String body = "already taken";
        return handleResponse(response, UserData.class, body);
    }

    public AuthData login(String username, String password) throws ClientException{
        var requestData = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", requestData, null);
        var response = sendRequest(request);
        String body = "bad request";
        return handleResponse(response, AuthData.class, body);
    }

    public void logout(String authToken) throws ClientException{
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        String body = "bad request";
        handleResponse(response, null, body);
    }

    public GameData createGame(String authToken, String gameName) throws ClientException{
        var requestData = new CreateGameRequest(gameName);
        var request = buildRequest("POST", "/game", requestData, authToken);
        var response = sendRequest(request);
        String body = "bad request";
        return handleResponse(response, GameData.class, body);
    }

    public void joinGame(String authToken, int gameID, String color) throws ClientException{
        var requestData = new JoinGameRequest(color, gameID);
        var request = buildRequest("PUT", "/game", requestData, authToken);
        var response = sendRequest(request);
        String body = "bad request";
        handleResponse(response, null, body);
    }

    public List<GameData> listGames(String authToken) throws ClientException{
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);

        String message = "bad request";
        handleResponse(response, null, message);

        String body = response.body();
        ListGamesResponse responseObj = new Gson().fromJson(body, ListGamesResponse.class);
        return responseObj.games();
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

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass, String body) throws ClientException{
        int status = response.statusCode();
        if(!isSuccessful(status)){
            String serverError = response.body();
            if(serverError != null){
                throw new ClientException(body);
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
