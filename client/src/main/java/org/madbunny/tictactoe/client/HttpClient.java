package org.madbunny.tictactoe.client;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.madbunny.tictactoe.core.datamodel.GameState;

import java.util.Objects;
import java.util.Optional;

public class HttpClient {
    private final Gson json = new Gson();
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final String host;
    private final int port;

    public HttpClient(String host, int port) {
        this.host = enrichHostWithSchema(host);
        this.port = port;
    }

    public Optional<GameState> startNewGame() {
        return callHandler("json/startNewGame");
    }

    public Optional<GameState> getGameState() {
        return callHandler("json/getGameState");
    }

    public Optional<GameState> setCross(int row, int col) {
        return callHandler(String.format("json/setCross/%d/%d", row, col));
    }

    public Optional<GameState> setZero(int row, int col) {
        return callHandler(String.format("json/setZero/%d/%d", row, col));
    }

    public boolean ping() {
        try {
            var request = buildRequest("ping");
            var response = client.newCall(request).execute();
            return response.code() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private Request buildRequest(String path) {
        return new Request.Builder()
                .url(String.format("%s:%d/%s", host, port, path))
                .build();
    }

    private Optional<GameState> callHandler(String path) {
        var request = buildRequest(path);
        try {
            var response = client.newCall(request).execute();
            var body = response.body();
            if (response.code() != 200 || body == null) {
                return Optional.empty();
            }

            var state = json.fromJson(Objects.requireNonNull(body).string(), GameState.class);
            return Optional.of(state);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String enrichHostWithSchema(String host) {
        return host.startsWith("http") ? host : String.format("http://%s", host);
    }
}
