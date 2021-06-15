package org.madbunny.tictactoe.client;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.madbunny.tictactoe.core.datamodel.GameState;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class HttpClient {
    private final Gson json = new Gson();
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final String host;
    private final int port;

    public HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Optional<GameState> startNewGame() throws IOException {
        return callHandler("json/startNewGame");
    }

    public Optional<GameState> getGameState() throws IOException {
        return callHandler("json/getGameState");
    }

    public Optional<GameState> setCross(int row, int col) throws IOException {
        return callHandler(String.format("json/setCross/%d/%d", row, col));
    }

    public Optional<GameState> setZero(int row, int col) throws IOException {
        return callHandler(String.format("json/setZero/%d/%d", row, col));
    }

    private Optional<GameState> callHandler(String path) throws IOException {
        var request = new Request.Builder()
                .url(String.format("%s:%d/%s", host, port, path))
                .build();

        var call = client.newCall(request);
        var response = call.execute();
        var body = response.body();
        if (response.code() != 200 || body == null) {
            return Optional.empty();
        }

        var state = json.fromJson(Objects.requireNonNull(body).string(), GameState.class);
        return Optional.of(state);
    }
}
