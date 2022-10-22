package kata.tictactoe.controllers;

import kata.tictactoe.model.Game;
import kata.tictactoe.model.Result;
import kata.tictactoe.services.GameService;
import kata.tictactoe.ws.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class GameController {

    private GameService gameService;
    private WebSocketHandler webSocketHandler;

    @Autowired
    public GameController(final GameService gameService, WebSocketHandler webSocketHandler) {
        this.gameService = gameService;
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping({"", "/"})
    public String goToNewGame(HttpSession session) {
        Game currentGame = gameService.startNewGame(session.getId());
        return "redirect:/game/" + currentGame.getId();
    }

    @GetMapping("/game/{id}")
    public String getNewGame(@PathVariable String id, Model model, HttpSession session) {
        setGameAttributes(model, session, id);
        return "game";
    }

    @GetMapping("/game/{id}/join")
    public String joinGame(@PathVariable String id, HttpSession session) {
        Game currentGame = gameService.joinGame(id, session.getId());
        return "redirect:/game/" + currentGame.getId();
    }

    @PostMapping("/game/{id}")
    public String makeMove(@PathVariable String id, @ModelAttribute("coordinate") String coordinates, Model model, HttpSession session) throws IOException {
        Game game = gameService.makeMove(coordinates, id, session.getId());
        setGameAttributes(model, session, id);
        webSocketHandler.notifyPlayer(game.getOpponent(session.getId()));
        return "game :: game-grid";
    }

    @PostMapping("/game/{id}/refresh")
    public String refreshGrid(@PathVariable String id, Model model, HttpSession session) {
        setGameAttributes(model, session, id);
        return "game :: game-grid";
    }

    private void setGameAttributes(Model model, HttpSession session, String id) {
        Game game = gameService.getCurrentGameById(id);
        model.addAttribute("game", game);
        char sign = session.getId().equals(game.getxPlayer()) ? 'x' : 'o';
        char opponentSign = sign == 'o' ? 'x' : 'o';
        model.addAttribute("playerSign", sign);
        model.addAttribute("opponentSign", opponentSign);
        if (game.getResult() == Result.OWINS) {
            model.addAttribute("winner", session.getId().equals(game.getoPlayer()));
        } else if (game.getResult() == Result.XWINS) {
            model.addAttribute("winner", session.getId().equals(game.getxPlayer()));
        } else if (game.getResult() == Result.DRAW) {
            model.addAttribute("draw", true);
        }
        model.addAttribute("startPos", game.getStartPos());
        model.addAttribute("endPos", game.getEndPos());
        boolean blockGrid = game.getResult() != Result.INPROGRESS || !game.isYourTurn(session.getId());
        model.addAttribute("blockGrid", blockGrid);
        model.addAttribute("playerId", session.getId());
    }
}
