package kata.tictactoe.controllers;

import kata.tictactoe.model.Game;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void goToNewGame_EvenGame_GridEmpty() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        when(mock.getState()).thenReturn(new char[][]{});
        mockMvc.perform(get("/game/123"))
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("grid"));
        verify(mock, never()).makeXMove(0, 0);
    }

    @Test
    public void goToNewGame_OddGame_GridContainsXMove() throws Exception {
        Game mock = mock(Game.class);
        ReflectionTestUtils.setField(gameController, "currentGame", mock);
        ReflectionTestUtils.setField(gameController, "sign", 'o');
        when(mock.getState()).thenReturn(new char[][]{});
        mockMvc.perform(get("/game/123"))
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("grid"));
        verify(mock, times(1)).makeXMove(0, 0);
    }
}