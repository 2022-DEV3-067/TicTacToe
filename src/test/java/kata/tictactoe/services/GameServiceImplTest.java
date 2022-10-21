package kata.tictactoe.services;

import kata.tictactoe.model.Game;
import kata.tictactoe.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        try (MockedConstruction<Game> gameMockedConstruction = Mockito.mockConstruction(Game.class, (game, context) -> {
            doNothing().when(game).makeMove(anyChar(), anyInt(), anyInt());
            doCallRealMethod().when(game).setoPlayer(anyString());
            when(game.getoPlayer()).thenCallRealMethod();
            when(game.getxPlayer()).thenCallRealMethod();
        })) {

            when(random.nextBoolean()).thenReturn(false);

            Game game = gameService.startNewGame("123");

            assertEquals("123", game.getoPlayer());
            assertNull(game.getxPlayer());
            verify(game, times(1)).makeMove(eq('x'), anyInt(), anyInt());
        }
    }

    @Test
    void startNewGame_GameAlreadyInProgressForUser_returnsInProgressGame() {
        Game gameMock = mock(Game.class);
        when(gameMock.getResult()).thenReturn(Result.INPROGRESS);
        when(gameMock.getxPlayer()).thenReturn("123");
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        Game game = gameService.startNewGame("123");

        assertTrue(game == gameMock);
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
    void makeMove_GameOver_DoNotMakeFurtherMoves() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("123");
        when(gameMock.getResult()).thenReturn(Result.DRAW);
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        gameService.makeMove("12", "123", "456");

        verify(gameMock, never()).makeMove(anyChar(), anyInt(), anyInt());
    }

    @Test
    void makeMove_LastMove_DoNotMakeFurtherMoves() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("123");
        when(gameMock.getoPlayer()).thenReturn("456");
        when(gameMock.getResult()).thenReturn(Result.INPROGRESS, Result.DRAW);
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        gameService.makeMove("12", "123", "456");

        verify(gameMock, times(1)).makeMove('o', 1, 2);
        verifyNoMoreInteractions(gameMock);
    }

    @Test
    void makeMove_GameInProgress_makeOppositeMove() {
        Game gameMock = mock(Game.class);
        when(gameMock.getId()).thenReturn("123");
        when(gameMock.getoPlayer()).thenReturn("456");
        when(gameMock.getState()).thenReturn(new char[][]{{'x', 'x', 'o'}, {0, 0, 0}, {0, 0, 0}});
        when(gameMock.getResult()).thenReturn(Result.INPROGRESS);
        ReflectionTestUtils.setField(gameService, "gameList", Collections.singletonList(gameMock));

        gameService.makeMove("12", "123", "456");

        verify(gameMock, times(1)).makeMove('o', 1, 2);
        ArgumentCaptor<Integer> moves = ArgumentCaptor.forClass(Integer.class);
        verify(gameMock, times(1)).makeMove(eq('x'), moves.capture(), moves.capture());
        int l = moves.getAllValues().get(0);
        int c = moves.getAllValues().get(1);
        assertTrue((l == 1 && c >= 0 && c < 3) || (l == 2 && (c == 0 || c == 2)));
    }

}