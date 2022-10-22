package kata.tictactoe.ws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketHandlerImplTest {

    @Mock
    private Map<String, WebSocketSession> sessions;
    @InjectMocks
    private WebSocketHandlerImpl webSocketHandler;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(WebSocketHandlerImpl.class, "sessions", sessions);
    }

    @Test
    void afterConnectionEstablished_storesSession() throws Exception {
        WebSocketSession webSocketSession = mock(WebSocketSession.class);
        when(webSocketSession.getUri()).thenReturn(mock(URI.class));
        when(webSocketSession.getUri().getQuery()).thenReturn("playerId=465");

        webSocketHandler.afterConnectionEstablished(webSocketSession);

        verify(sessions, times(1)).put("465", webSocketSession);
    }

    @Test
    void notifyPlayer_sendsMessageOnSession() throws IOException {
        WebSocketSession webSocketSession = mock(WebSocketSession.class);
        when(sessions.get("456")).thenReturn(webSocketSession);

        webSocketHandler.notifyPlayer("456");

        verify(webSocketSession, times(1)).sendMessage(any(TextMessage.class));
    }
}