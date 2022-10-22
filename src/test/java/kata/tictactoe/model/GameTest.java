package kata.tictactoe.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game = new Game();

    @Test
    void getId_IdNotNull() {
        assertNotNull(game.getId());
    }

    @Test
    void canMakeMove_PositionNotAvailable_ReturnsFalse() {
        char[][] state = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state);

        assertFalse(game.canMakeMove(0, 1));
    }

    @Test
    void canMakeMove_WrongPosition_ReturnsFalse() {
        char[][] state = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state);

        assertFalse(game.canMakeMove(3, 1));
    }

    @Test
    void canMakeMove_PositionAvailable_ReturnsTrue() {
        char[][] state = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state);

        assertTrue(game.canMakeMove(0, 2));
    }

    @Test
    void makeMove_PlayerPlaysTwice_throwsIllegalStateException() {
        char[][] state = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state);
        ReflectionTestUtils.setField(game, "lastMove", 'x');

        assertThrowsExactly(IllegalStateException.class, () -> game.makeMove('x', 0, 2));
    }

    @Test
    void makeXMove_PositionIsAvailable_AddsXInTheRightPlace() {
        char[][] state0 = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('x', 0, 2);

        assertEquals('x', game.getState()[0][2]);
    }

    @Test
    void makeOMove_PositionIsAvailable_AddsXInTheRightPlace() {
        char[][] state0 = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('o', 0, 2);

        assertEquals('o', game.getState()[0][2]);
    }

    @Test
    void makeOMove_NoRowIsCompleted_ReturnsINPROGRESS() {
        char[][] state0 = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('o', 0, 2);

        assertEquals(Result.INPROGRESS, game.getResult());
        assertNull(game.getStartPos());
        assertNull(game.getEndPos());
    }

    @Test
    void makeOMove_VerticalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'x', 'x', 'o'}, {0, 0, 'o'}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('o', 2, 2);

        assertEquals(Result.OWINS, game.getResult());
        assertEquals(2, game.getStartPos());
        assertEquals(22, game.getEndPos());
    }

    @Test
    void makeOMove_HorizontalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'x', 'x', 0}, {'o', 'o', 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('o', 1, 2);

        assertEquals(Result.OWINS, game.getResult());
        assertEquals(10, game.getStartPos());
        assertEquals(12, game.getEndPos());
    }

    @Test
    void makeOMove_DiagonalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'x', 'x', 'o'}, {0, 'o', 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('o', 2, 0);

        assertEquals(Result.OWINS, game.getResult());
        assertEquals(2, game.getStartPos());
        assertEquals(20, game.getEndPos());
    }

    @Test
    void makeOMove_NoRowIsCompletedAndGridIsFull_ReturnsDRAW() {
        char[][] state0 = {{'o', 'x', 'o'}, {'x', 'o', 'x'}, {'x', 0, 'x'}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('o', 2, 1);

        assertEquals(Result.DRAW, game.getResult());
        assertNull(game.getStartPos());
        assertNull(game.getEndPos());
    }

    @Test
    void makeXMove_NoRowIsCompleted_ReturnsINPROGRESS() {
        char[][] state0 = {{'x', 'o', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('x', 0, 2);

        assertEquals(Result.INPROGRESS, game.getResult());
        assertNull(game.getStartPos());
        assertNull(game.getEndPos());
    }

    @Test
    void makXMove_VerticalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'o', 'x', 0}, {0, 'x', 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('x', 2, 1);

        assertEquals(Result.XWINS, game.getResult());
        assertEquals(1, game.getStartPos());
        assertEquals(21, game.getEndPos());
    }

    @Test
    void makeXMove_HorizontalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'o', 'x', 0}, {0, 'x', 'x'}, {0, 'o', 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('x', 1, 0);

        assertEquals(Result.XWINS, game.getResult());
        assertEquals(10, game.getStartPos());
        assertEquals(12, game.getEndPos());
    }

    @Test
    void makeXMove_DiagonalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{0, 'o', 'x'}, {0, 'x', 'o'}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('x', 2, 0);

        assertEquals(Result.XWINS, game.getResult());
        assertEquals(2, game.getStartPos());
        assertEquals(20, game.getEndPos());
    }

    @Test
    void makeXMove_NoRowIsCompletedAndGridIsFull_ReturnsDRAW() {
        char[][] state0 = {{'x', 'o', 'x'}, {'x', 'x', 'o'}, {'o', 0, 'o'}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeMove('x', 2, 1);

        assertEquals(Result.DRAW, game.getResult());
        assertNull(game.getStartPos());
        assertNull(game.getEndPos());
    }

    @Test
    void getState_getCopyOfGameState() {
        char[][] state0 = {{'x', 'o', 'x'}, {'x', 'x', 'o'}, {'o', 0, 'o'}};
        ReflectionTestUtils.setField(game, "state", state0);

        char[][] state = game.getState();

        for (int i = 0; i < 3; i++) {
            assertTrue(Arrays.equals(state0[i], state[i]));
        }
        assertNotSame(state, state0);
    }

    @Test
    void getOpponent_NotAPlayer_throwsIllegalArgumentException() {
        game.setxPlayer("abc");
        game.setoPlayer("def");

        assertThrowsExactly(IllegalArgumentException.class,
                () -> game.getOpponent("123"));
    }

    @Test
    void getOpponent_PlayerIsO_OpponentIsX() {
        game.setxPlayer("123");
        game.setoPlayer("456");

        assertEquals("123", game.getOpponent("456"));
    }

    @Test
    void getOpponent_PlayerIsX_OpponentIsO() {
        game.setxPlayer("123");
        game.setoPlayer("456");

        assertEquals("456", game.getOpponent("123"));
    }

    @Test
    void isYourTurn_PlayerIsXAndLastMoveIsX_ReturnsFalse() {
        game.setxPlayer("123");
        ReflectionTestUtils.setField(game, "lastMove", 'x');

        assertFalse(game.isYourTurn("123"));
    }

    @Test
    void isYourTurn_PlayerIsXAndLastMoveIsO_ReturnsTrue() {
        game.setxPlayer("123");
        ReflectionTestUtils.setField(game, "lastMove", 'o');

        assertTrue(game.isYourTurn("123"));
    }

    @Test
    void isYourTurn_PlayerIsOAndLastMoveIsX_ReturnsTrue() {
        game.setoPlayer("123");
        ReflectionTestUtils.setField(game, "lastMove", 'x');

        assertTrue(game.isYourTurn("123"));
    }

    @Test
    void isYourTurn_PlayerIsOAndLastMoveIsO_ReturnsFalse() {
        game.setoPlayer("123");
        ReflectionTestUtils.setField(game, "lastMove", 'o');

        assertFalse(game.isYourTurn("123"));
    }

    @Test
    void isYourTurn_PlayerIsOAndFirstMove_ReturnsFalse() {
        game.setoPlayer("123");

        assertFalse(game.isYourTurn("123"));
    }

    @Test
    void isYourTurn_PlayerIsXAndFirstMove_ReturnsTrue() {
        game.setxPlayer("123");

        assertTrue(game.isYourTurn("123"));
    }

    @Test
    void opponentHasJoined_xPlayerNull_ReturnsFalse() {
        game.setoPlayer("123");

        assertFalse(game.opponentHasJoined());
    }

    @Test
    void opponentHasJoined_oPlayerNull_ReturnsFalse() {
        game.setxPlayer("123");

        assertFalse(game.opponentHasJoined());
    }

    @Test
    void opponentHasJoined_PlayersNotNull_ReturnsTrue() {
        game.setxPlayer("123");
        game.setoPlayer("456");

        assertTrue(game.opponentHasJoined());
    }

    @AfterEach
    void clearState() {
        ReflectionTestUtils.setField(game, "lastMove", '\u0000');
        game.setxPlayer(null);
        game.setoPlayer(null);
    }
}