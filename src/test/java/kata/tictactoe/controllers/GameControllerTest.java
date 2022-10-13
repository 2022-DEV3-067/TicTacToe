package kata.tictactoe.controllers;

import kata.tictactoe.model.Game;
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

    @Test
    public void goToNewGame_EvenGame_GridEmpty() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        when(mock.getState()).thenReturn(new char[][]{});
        mockMvc.perform(get("/game/123"))
                .andExpect(view().name("game"));
        verify(mock, never()).makeMove('x', 0, 0);
    }

    @Test
    public void goToNewGame_OddGame_GridContainsXMove() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "sign", 'o');
        when(mock.getState()).thenReturn(new char[][]{});
        mockMvc.perform(get("/game/123"))
                .andExpect(view().name("game"));
        verify(mock, times(1)).makeMove('x', 0, 0);
    }

    @Test
    public void makeMove_CoordinateIs21_DelegatesToGameObjectWithCorrectValues() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "sign", 'o');
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
        mockMvc.perform(post("/game/123").param("coordinate", "12"))
                .andExpect(view().name("game"));
        verify(mock, times(1)).makeMove('o', 1, 2);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(mock, times(1)).makeMove(eq('x'), captor.capture(), captor.capture());
        int l = captor.getAllValues().get(0);
        int c = captor.getAllValues().get(1);
        assertTrue((l == 1 && c >= 0 && c < 3) || (l == 2 && (c == 0 || c == 2)));
    }
}