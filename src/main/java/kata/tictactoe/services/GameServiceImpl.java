package kata.tictactoe.services;

import kata.tictactoe.model.Game;
import kata.tictactoe.model.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    private List<Game> gameList = new ArrayList<>();
    private Random random = new Random();

    @Override
    public Game startNewGame(final String playerId) {
        Game game = getCurrentPlayerGame(playerId);
        if (game != null) {
            return game;
        }
        char sign = random.nextBoolean() ? 'x' : 'o';
        Game currentGame = new Game();
        if (sign == 'o') {
            currentGame.setoPlayer(playerId);
        } else {
            currentGame.setxPlayer(playerId);
        }
        gameList.add(currentGame);
        return currentGame;
    }

    @Override
    //assuming each player can have only one game in progress
    public Game getCurrentPlayerGame(String playerId) {
        return gameList.stream()
                .filter(game -> Result.INPROGRESS.equals(game.getResult())
                        && (playerId.equals(game.getoPlayer()) || playerId.equals(game.getxPlayer())))
                .findAny().orElse(null);
    }

    @Override
    public Game getCurrentGameById(String gameId) {
        return gameList.stream()
                .filter(game -> game.getId().equals(gameId))
                .findAny().orElse(null);
    }

    @Override
    public Game makeMove(String coordinates, String gameId, String playerId) {
        Game currentGame = getCurrentGameById(gameId);
        char sign = playerId.equals(currentGame.getoPlayer()) ? 'o' : 'x';
        if (currentGame.getResult() == Result.INPROGRESS) {
            currentGame.makeMove(sign,
                    Integer.valueOf(coordinates.substring(0, 1)),
                    Integer.valueOf(coordinates.substring(1, 2)));
        }
        return currentGame;
    }

    @Override
    public Game joinGame(String gameId, String playerId) {
        Game currentGame = getCurrentGameById(gameId);
        boolean notFound = currentGame == null;
        boolean reserved = notFound
                || (currentGame.getoPlayer() != null && currentGame.getxPlayer() != null);
        boolean alreadyJoined = notFound
                || playerId.equals(currentGame.getoPlayer())
                || playerId.equals(currentGame.getxPlayer());
        if (alreadyJoined || reserved) {
            throw new IllegalStateException();
        }
        if (currentGame.getxPlayer() == null) {
            currentGame.setxPlayer(playerId);
        } else {
            currentGame.setoPlayer(playerId);
        }
        return currentGame;
    }
}
