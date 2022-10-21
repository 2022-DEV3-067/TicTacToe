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
            currentGame.makeMove('x', random.nextInt(2), random.nextInt(2));
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
        makeComputerMove(currentGame, sign);
        return currentGame;
    }

    private void makeComputerMove(final Game currentGame, char sign) {
        if (currentGame.getResult() == Result.INPROGRESS) {
            char[][] state = currentGame.getState();
            for (int l = 0; l < 3; l++) {
                for (int c = 0; c < 3; c++) {
                    if (state[l][c] == 0) {
                        currentGame.makeMove(sign == 'o' ? 'x' : 'o', l, c);
                        return;
                    }
                }
            }
        }
    }
}
