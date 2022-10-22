package kata.tictactoe.services;

import kata.tictactoe.model.Game;

public interface GameService {

    Game startNewGame(final String playerId);

    Game getCurrentPlayerGame(final String playerId);

    Game getCurrentGameById(String gameId);

    Game makeMove(final String coordinates, final String gameId, final String playerId);

    Game joinGame(final String gameId, final String playerId);
}
