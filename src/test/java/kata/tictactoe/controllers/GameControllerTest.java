package kata.tictactoe.controllers;

import jdk.nashorn.internal.ir.annotations.Ignore;
import kata.tictactoe.model.Game;
import kata.tictactoe.model.GameResult;
import kata.tictactoe.model.Result;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    public void getNewGame() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name(StringStartsWith.startsWith("redirect:/game")));
    }


    @Ignore
    // [TODO] use power mock to spy on new instance of Game
    public void getNewGame_OddGame_GridContainsXMove() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "gamesCount", 1);
        when(mock.getState()).thenReturn(new char[][]{});

        mockMvc.perform(get("/"))
                .andExpect(view().name(StringStartsWith.startsWith("redirect:/game")));

        verify(mock, times(1)).makeMove('x', 0, 0);
    }

    @Test
    public void goToNewGame_EvenGame_GridEmpty() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);

        mockMvc.perform(get("/game/123"))
                .andExpect(view().name("game"));

        verify(mock, never()).makeMove('x', 0, 0);
    }

    @Test
    public void makeMove_CoordinateIs21_DelegatesToGameObjectWithCorrectValues() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "sign", 'o');
        when(mock.makeMove(anyChar(), eq(1), eq(2))).thenReturn(mock(GameResult.class));

        mockMvc.perform(post("/game/123").param("coordinate", "12"))
                .andExpect(view().name("game"));

        verify(mock, times(1)).makeMove('o', 1, 2);
    }

    @Test
    public void makeMove_PlayComputerMove() throws Exception {
        Game mock = mock(Game.class);
        when(mock.getState()).thenReturn(new char[][]{{'x', 'x', 'o'}, {0, 0, 0}, {0, 0, 0}});
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "sign", 'o');
        GameResult gameResult= mock(GameResult.class);
        when(gameResult.getResult()).thenReturn(Result.INPROGRESS);
        when(mock.makeMove(anyChar(), anyInt(), anyInt())).thenReturn(gameResult);

        mockMvc.perform(post("/game/123").param("coordinate", "12"))
                .andExpect(view().name("game"));

        verify(mock, times(1)).makeMove('o', 1, 2);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(mock, times(1)).makeMove(eq('x'), captor.capture(), captor.capture());
        int l = captor.getAllValues().get(0);
        int c = captor.getAllValues().get(1);
        assertTrue((l == 1 && c >= 0 && c < 3) || (l == 2 && (c == 0 || c == 2)));
    }

    @Test
    public void makeMove_IsLastMove_showResults() throws Exception {
        Game mock = mock(Game.class);
        GameResult gameResult = mock(GameResult.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "sign", 'o');
        when(mock.makeMove(anyChar(), eq(1), eq(2))).thenReturn(gameResult);
        when(gameResult.getResult()).thenReturn(Result.OWINS);
        when(gameResult.getStartPos()).thenReturn(0);
        when(gameResult.getEndPos()).thenReturn(20);

        mockMvc.perform(post("/game/123").param("coordinate", "12"))
                .andExpect(view().name("game"))
                .andExpect(model().attribute("winner", true))
                .andExpect(model().attribute("startPos", 0))
                .andExpect(model().attribute("endPos", 20));
    }
}