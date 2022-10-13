package kata.tictactoe.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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

        assertFalse(game.canMakeMove(0,1));
    }

    @Test
    void canMakeMove_WrongPosition_ReturnsFalse() {
        char[][] state = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state);

        assertFalse(game.canMakeMove(3,1));
    }

    @Test
    void canMakeMove_PositionAvailable_ReturnsTrue() {
        char[][] state = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state);

        assertTrue(game.canMakeMove(0,2));
    }

    @Test
    void makeXMove_PositionIsAvailable_AddsXInTheRightPlace() {
        char[][] state0 = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeXMove(0,2);

        char[][] state = (char[][]) ReflectionTestUtils.getField(game, "state");
        assertEquals('x', state[0][2]);
    }

    @Test
    void makeOMove_PositionIsAvailable_AddsXInTheRightPlace() {
        char[][] state0 = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        game.makeOMove(0,2);

        char[][] state = (char[][]) ReflectionTestUtils.getField(game, "state");
        assertEquals('o', state[0][2]);
    }

    @Test
    void makeOMove_NoRowIsCompleted_ReturnsINPROGRESS() {
        char[][] state0 = {{'x', 'x', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeOMove(0,2);

        assertEquals(Result.INPROGRESS, result);
    }

    @Test
    void makeOMove_VerticalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'x', 'x', 'o'}, {0, 0, 'o'}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeOMove(2,2);

        assertEquals(Result.OWINS, result);
    }

    @Test
    void makeOMove_HorizontalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'x', 'x', 0}, {'o', 'o', 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeOMove(1,2);

        assertEquals(Result.OWINS, result);
    }

    @Test
    void makeOMove_DiagonalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'x', 'x', 'o'}, {0, 'o', 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeOMove(2,0);

        assertEquals(Result.OWINS, result);
    }

    @Test
    void makeOMove_NoRowIsCompletedAndGridIsFull_ReturnsDRAW() {
        char[][] state0 = {{'o', 'x', 'o'}, {'x', 'o', 'x'}, {'x', 0, 'x'}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeOMove(2,1);

        assertEquals(Result.DRAW, result);
    }

    @Test
    void makeXMove_NoRowIsCompleted_ReturnsINPROGRESS() {
        char[][] state0 = {{'x', 'o', 0}, {0, 0, 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeXMove(0,2);

        assertEquals(Result.INPROGRESS, result);
    }

    @Test
    void makXMove_VerticalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'o', 'x', 0}, {0, 'x', 0}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeXMove(2,1);

        assertEquals(Result.XWINS, result);
    }

    @Test
    void makeXMove_HorizontalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{'o', 'x', 0}, {0, 'x', 'x'}, {0, 'o', 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeXMove(1,0);

        assertEquals(Result.XWINS, result);
    }

    @Test
    void makeXMove_DiagonalRowIsCompleted_ReturnsOWINS() {
        char[][] state0 = {{0, 'o', 'x'}, {0, 'x', 'o'}, {0, 0, 0}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeXMove(2,0);

        assertEquals(Result.XWINS, result);
    }

    @Test
    void makeXMove_NoRowIsCompletedAndGridIsFull_ReturnsDRAW() {
        char[][] state0 = {{'x', 'o', 'x'}, {'x', 'x', 'o'}, {'o', 0, 'o'}};
        ReflectionTestUtils.setField(game, "state", state0);

        Result result = game.makeXMove(2,1);

        assertEquals(Result.DRAW, result);
    }

}