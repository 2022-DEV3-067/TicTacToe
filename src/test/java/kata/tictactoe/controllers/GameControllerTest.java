package kata.tictactoe.controllers;

import kata.tictactoe.model.Game;
import kata.tictactoe.model.Result;
import kata.tictactoe.services.GameService;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;
    private MockMvc mockMvc;
    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void getNewGame() throws Exception {
        when(gameService.startNewGame(anyString())).thenReturn(mock(Game.class));

        mockMvc.perform(get("/"))
                .andExpect(view().name(StringStartsWith.startsWith("redirect:/game")));
    }

    @Test
    void goToNewGame_EvenGame_GridEmpty() throws Exception {
        MockHttpSession session = new MockHttpSession(null, "456");
        when(gameService.getCurrentGameById(anyString())).thenReturn(mock(Game.class));

        mockMvc.perform(get("/game/123").session(session).param("id", "123"))
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("game", "playerSign", "opponentSign"));
    }

    @Test
    void makeMove_CoordinateIs21_DelegatesToGameObjectWithCorrectValues() throws Exception {
        when(gameService.makeMove(anyString(), anyString(), anyString())).thenReturn(mock(Game.class));
        MockHttpSession session = new MockHttpSession(null, "456");
        when(gameService.getCurrentGameById(anyString())).thenReturn(mock(Game.class));

        mockMvc.perform(post("/game/123")
                .param("coordinate", "12")
                .param("id", "213")
                .session(session))
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("game", "playerSign", "opponentSign"));

        verify(gameService, times(1)).makeMove("12", "123", "456");
    }

    @Test
    void makeMove_IsLastMove_showResults() throws Exception {
        MockHttpSession session = new MockHttpSession(null, "456");
        Game mock = mock(Game.class);
        when(gameService.makeMove(anyString(), anyString(), anyString())).thenReturn(mock);
        when(gameService.getCurrentGameById(anyString())).thenReturn(mock);
        when(mock.getResult()).thenReturn(Result.OWINS);
        when(mock.getStartPos()).thenReturn(0);
        when(mock.getEndPos()).thenReturn(20);
        when(mock.getoPlayer()).thenReturn("456");

        mockMvc.perform(post("/game/123").session(session)
                .param("coordinate", "12").param("id", "123"))
                .andExpect(view().name("game"))
                .andExpect(model().attribute("winner", true))
                .andExpect(model().attribute("startPos", 0))
                .andExpect(model().attribute("endPos", 20));
    }
}