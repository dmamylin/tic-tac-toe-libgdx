package org.madbunny.tictactoe.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.madbunny.tictactoe.client.helper.Field;
import org.madbunny.tictactoe.client.helper.Service;
import org.madbunny.tictactoe.core.FieldCell;
import org.madbunny.tictactoe.core.datamodel.GameState;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class TestGameplay {
    private Service service;

    @BeforeEach
    void startService() {
        service = new Service();
        service.start();
    }

    @AfterEach
    void stopService() {
        service.stop();
    }

    @Test
    void ping() {
        Assertions.assertTrue(service.getClient().ping());
    }

    @Test
    void startNewGameIdempotency() {
        var initialState = service.getClient().getGameState();
        Assertions.assertTrue(initialState.isPresent());
        Assertions.assertNull(initialState.get().victory);
        Assertions.assertNull(initialState.get().error);
        Assertions.assertLinesMatch(Field.createEmpty().streamRows(), streamField(initialState));

        var state = service.getClient().startNewGame();
        Assertions.assertTrue(state.isPresent());
        Assertions.assertNull(state.get().victory);
        Assertions.assertNull(state.get().error);
        Assertions.assertLinesMatch(Field.createEmpty().streamRows(), streamField(state));
    }

    @Test
    void playGame() {
        var refState = Field.createEmpty();
        var state = service.getClient().getGameState();
        Assertions.assertTrue(state.isPresent());
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));

        // A..
        // ...
        // ...
        var player = state.get().currentPlayer;
        refState.setValue(player, 0, 0);
        state = doTurn(player, 0, 0);
        Assertions.assertTrue(state.isPresent());
        Assertions.assertEquals(player, state.get().field[0].charAt(0));
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));
        Assertions.assertNotEquals(player, state.get().currentPlayer);

        // A..
        // B..
        // ...
        player = state.get().currentPlayer;
        refState.setValue(player, 1, 0);
        state = doTurn(player, 1, 0);
        Assertions.assertTrue(state.isPresent());
        Assertions.assertEquals(player, state.get().field[1].charAt(0));
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));

        // AA.
        // B..
        // ...
        player = state.get().currentPlayer;
        refState.setValue(player, 0, 1);
        state = doTurn(player, 0, 1);
        Assertions.assertTrue(state.isPresent());
        Assertions.assertEquals(player, state.get().field[0].charAt(1));
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));

        // AA.
        // BB.
        // ...
        player = state.get().currentPlayer;
        refState.setValue(player, 1, 1);
        state = doTurn(player, 1, 1);
        Assertions.assertTrue(state.isPresent());
        Assertions.assertEquals(player, state.get().field[1].charAt(1));
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));

        // AAA
        // BB.
        // ...
        player = state.get().currentPlayer;
        final var winner = player;
        refState.setValue(player, 0, 2);
        state = doTurn(player, 0, 2);
        Assertions.assertTrue(state.isPresent());
        Assertions.assertEquals(player, state.get().field[0].charAt(2));
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));

        // Check victory
        Assertions.assertNotNull(state.get().victory);
        Assertions.assertEquals(winner, state.get().victory.winner);

        // AAA
        // BB.
        // ...
        // Try to play after victory
        player = state.get().currentPlayer;
        state = doTurn(player, 1, 2);
        Assertions.assertTrue(state.isPresent());
        Assertions.assertEquals(FieldCell.EMPTY, state.get().field[1].charAt(2));
        Assertions.assertLinesMatch(refState.streamRows(), streamField(state));

        // Victory state didn't change
        Assertions.assertNotNull(state.get().victory);
        Assertions.assertEquals(winner, state.get().victory.winner);

        // Clean up the field
        state = service.getClient().startNewGame();
        Assertions.assertTrue(state.isPresent());
        Assertions.assertLinesMatch(Field.createEmpty().streamRows(), streamField(state));
        Assertions.assertNull(state.get().victory);
    }

    private Optional<GameState> doTurn(char currentPlayer, int row, int col) {
        switch (currentPlayer) {
            case FieldCell.CROSS:
                return service.getClient().setCross(row, col);
            case FieldCell.ZERO:
                return service.getClient().setZero(row, col);
        }
        return Optional.empty();
    }

    private static Stream<String> streamField(Optional<GameState> state) {
        return state.isEmpty() ? Stream.empty() : Arrays.stream(state.get().field);
    }
}
