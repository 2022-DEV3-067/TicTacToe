package kata.tictactoe.ws;

import java.io.IOException;

public interface WebSocketHandler {

    void notifyPlayer(String playerId) throws IOException;
}
