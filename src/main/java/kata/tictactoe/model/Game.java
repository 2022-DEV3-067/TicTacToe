package kata.tictactoe.model;

import java.util.Arrays;
import java.util.UUID;

public class Game {
    private final String id = UUID.randomUUID().toString();
    //Internal Convention for this class, O is represented by 1 value and X by 2 value
    private final char[][] state = new char[3][3];
    private String xPlayer;
    private String oPlayer;
    private Result result = Result.INPROGRESS;
    private Integer startPos;
    private Integer endPos;
    private char lastMove;

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

    public void makeMove(char value, int l, int c) {
        if (!canMakeMove(l, c) || value == lastMove) {
            throw new IllegalStateException();
        }
        state[l][c] = value;
        lastMove = value;
        updateState();
    }

    public boolean isYourTurn(String playerId) {
        return playerId.equals(xPlayer) && (lastMove == 'o' || lastMove == '\u0000')
                || playerId.equals(oPlayer) && lastMove == 'x';
    }

    public String getxPlayer() {
        return xPlayer;
    }

    public void setxPlayer(String xPlayer) {
        this.xPlayer = xPlayer;
    }

    public String getoPlayer() {
        return oPlayer;
    }

    public void setoPlayer(String oPlayer) {
        this.oPlayer = oPlayer;
    }

    public Result getResult() {
        return result;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public Integer getEndPos() {
        return endPos;
    }

    public String getOpponent(String playerId) {
        if (playerId.equals(oPlayer)) {
            return xPlayer;
        } else if (playerId.equals(xPlayer)) {
            return oPlayer;
        }
        throw new IllegalArgumentException();
    }

    private void updateState() {
        this.result = Result.DRAW;
        int diag1 = 0; // rename
        int diag2 = 0;
        for (int row = 0; row < 3; row++) {
            int v = 0;
            int h = 0;
            for (int col = 0; col < 3; col++) {
                h += state[row][col];
                v += state[col][row];
                if (state[row][col] == 0) {
                    result = Result.INPROGRESS;
                }
            }
            if (hasCompletedHRow(h, row) || hasCompletedVRow(v, row)) {
                return;
            }
            diag1 += state[row][row];
            diag2 += state[row][2 - row];
        }
        if (hasCompletedDiagonal1(diag1)) {
            return;
        }
        hasCompletedDiagonal2(diag2);
    }

    private void updateState(Result result, int startPos, int endPos) {
        this.result = result;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    private boolean hasCompletedHRow(int h, int row) {
        if (h == 360) {
            updateState(Result.XWINS, row * 10, row * 10 + 2);
            return true;
        } else if (h == 333) {
            updateState(Result.OWINS, row * 10, row * 10 + 2);
            return true;
        }
        return false;
    }

    private boolean hasCompletedVRow(int v, int row) {
        if (v == 360) {
            updateState(Result.XWINS, row, 20 + row);
            return true;
        } else if (v == 333) {
            updateState(Result.OWINS, row, 20 + row);
            return true;
        }
        return false;
    }

    private boolean hasCompletedDiagonal1(int diag1) {
        if (diag1 == 333 || diag1 == 360) {
            updateState(diag1 == 333 ? Result.OWINS : Result.XWINS, 0, 22);
            return true;
        }
        return false;
    }

    private boolean hasCompletedDiagonal2(int diag2) {
        if (diag2 == 333 || diag2 == 360) {
            updateState(diag2 == 333 ? Result.OWINS : Result.XWINS, 2, 20);
            return true;
        }
        return false;
    }
}
