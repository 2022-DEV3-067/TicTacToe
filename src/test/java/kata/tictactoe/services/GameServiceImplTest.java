package kata.tictactoe.services;

import kata.tictactoe.model.Game;
import kata.tictactoe.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock(name = "random")
    private Random random;
    @InjectMocks
    private GameServiceImpl gameService = new GameServiceImpl();

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(gameService, "gameList", new ArrayList<>());
    }

    @Test
    void startNewGame_SignIsX_XPlayerIsSet() {
        when(random.nextBoolean()).thenReturn(true);

        Game game = gameService.startNewGame("123");

        assertEquals("123", game.getxPlayer());
        assertNull(game.getoPlayer());

    }

    @Test
    void startNewGame_SignIso_oPlayerIsSet() {
        when(random.nextBoolean()).thenReturn(false);

        Game game = gameService.startNewGame("123");

        assertEquals("123", game.getoPlayer());
        assertNull(game.getxPlayer());

    }

    @Test
    void startNewGame_GameAlreadyInProgressForUser_returnsInProgressGame() {
        Game gameMock = mock(Game.class);
        when(gameMock.getResult()).thenReturn(Result.INPROGRESS);
        when(gameMock.getxPlayer()).thenReturn("123");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        Game game = gameService.startNewGame("123");

        assertSame(game, gameMock);
    }

    @Test
    void getCurrentPlayerGame_PlayerDoesNotExists_ReturnsNull() {
        Game game = gameService.getCurrentPlayerGame("123");
        assertNull(game);
    }

    @Test
    void getCurrentPlayerGame_PlayerExistsAndGameOver_ReturnsNull() {
        Game gameMock = mock(Game.class);
        when(gameMock.getResult()).thenReturn(Result.XWINS);
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));
        Game game = gameService.getCurrentPlayerGame("123");
        assertNull(game);
    }

    @Test
    void getCurrentPlayerGame_PlayerExistsAndGameInProgress_ReturnsNull() {
        Game gameMock = mock(Game.class);
        when(gameMock.getoPlayer()).thenReturn("123");
        when(gameMock.getResult()).thenReturn(Result.INPROGRESS);
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));
        Game game = gameService.getCurrentPlayerGame("123");
        assertNotNull(game);
    }

    @Test
    void getCurrentGameById_GameIdExists_ReturnGame() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("123");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));
        Game game = gameService.getCurrentGameById("123");
        assertNotNull(game);
    }

    @Test
    void getCurrentGameById_GameIdNotExists_ReturnNull() {
        Game game = gameService.getCurrentGameById("123");
        assertNull(game);
    }

    @Test
    void makeMove_GameInProgress_insertsMoveInGameState() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("123");
        when(gameMock.getResult()).thenReturn(Result.INPROGRESS);

        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        gameService.makeMove("12", "123", "456");

        verify(gameMock, times(1)).makeMove('x', 1, 2);
    }

    @Test
    void makeMove_GameOver_DoNotMakeFurtherMoves() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("123");
        when(gameMock.getResult()).thenReturn(Result.DRAW);
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        gameService.makeMove("12", "123", "456");

        verify(gameMock, never()).makeMove(anyChar(), anyInt(), anyInt());
    }

    @Test
    void joinGame_GameDoesNotExists_ThrowsIllegalStateException() {
        assertThrowsExactly(IllegalStateException.class,
                () -> gameService.joinGame("abc", "456"));
    }

    @Test
    void joinGame_SameUserJoin_ThrowsIllegalStateException() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("abc");
        when(gameMock.getxPlayer()).thenReturn("456");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        assertThrowsExactly(IllegalStateException.class,
                () -> gameService.joinGame("abc", "456"));
    }

    @Test
    void joinGame_PlayersAlreadySet_ThrowsIllegalStateException() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("abc");
        when(gameMock.getxPlayer()).thenReturn("123");
        when(gameMock.getoPlayer()).thenReturn("789");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        assertThrowsExactly(IllegalStateException.class,
                () -> gameService.joinGame("abc", "456"));
    }

    @Test
    void joinGame_PlayerIsO_setPlayerOiInGame() {
        Game gameMock = mock(Game.class, CALLS_REAL_METHODS);
        when(gameMock.getId()).thenReturn("abc");
        when(gameMock.getxPlayer()).thenReturn("123");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        Game game = gameService.joinGame("abc", "456");

        assertEquals("456", game.getoPlayer());
    }

    @Test
    void joinGame_PlayerIsX_setPlayerXiInGame() {
        Game gameMock = mock(Game.class, CALLS_REAL_METHODS);
        when(gameMock.getId()).thenReturn("abc");
        when(gameMock.getoPlayer()).thenReturn("123");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        Game game = gameService.joinGame("abc", "456");

        assertEquals("456", game.getxPlayer());
    }


}