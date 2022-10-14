package kata.tictactoe.model;

import java.util.Arrays;
import java.util.UUID;

public class Game {
    private final String id = UUID.randomUUID().toString();
    //Internal Convention for this class, O is represented by 1 value and X by 2 value
    private final char[][] state = new char[3][3];

    public String getId() {
        return id;
    }

    public boolean canMakeMove(int l, int c) {
        return l >= 0 && l < 3
                && c >= 0 && c < 3
                && state[l][c] == 0;
    }

    public char[][] getState() {
        char[][] stateCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            stateCopy[i] = Arrays.copyOf(state[i], 3);
        }
        return stateCopy;
    }

    public GameResult makeMove(char value, int l, int c) {
        if (!canMakeMove(l, c)) {
            throw new IllegalStateException();
        }
        state[l][c] = value;
        return getResult();
    }

    private GameResult getResult() {
        Result result = Result.DRAW;
        int d1 = 0;
        int d2 = 0;
        for (int l = 0; l < 3; l++) {
            int v = 0;
            int h = 0;
            for (int c = 0; c < 3; c++) {
                h += state[l][c];
                v += state[c][l];
                if (state[l][c] == 0) {
                    result = Result.INPROGRESS;
                }
            }
            if (h == 360) {
                return new GameResult(Result.XWINS, l * 10, l * 10 + 2);
            } else if (v == 360) {
                return new GameResult(Result.XWINS, l, 20 + l);
            } else if (h == 333) {
                return new GameResult(Result.OWINS, l * 10, l * 10 + 2);
            } else if (v == 333) {
                return new GameResult(Result.OWINS, l, 20 + l);
            }
            d1 += state[l][l];
            d2 += state[l][2 - l];
        }
        if (d1 == 333 || d1 == 360) {
            return new GameResult(d1 == 333 ? Result.OWINS : Result.XWINS, 0, 22);
        }
        if (d2 == 333 || d2 == 360) {
            return new GameResult(d2 == 333 ? Result.OWINS : Result.XWINS, 2, 20);
        }
        return new GameResult(result);
    }
}
