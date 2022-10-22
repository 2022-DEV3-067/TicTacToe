package kata.tictactoe.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandlerImpl extends TextWebSocketHandler implements WebSocketHandler {

    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri != null && uri.getQuery() != null) {
            String query = uri.getQuery();
            String playerId = query.substring(query.indexOf('=') + 1);
            sessions.put(playerId, session);
        }
    }

    @Override
    public void notifyPlayer(final String playerId) throws IOException {
        sessions.get(playerId).sendMessage(new TextMessage(""));
    }
}
