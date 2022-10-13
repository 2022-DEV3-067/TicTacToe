package kata.tictactoe.controllers;

import kata.tictactoe.model.Game;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Random;

@Controller
@SessionScope
public class GameController {

    private int gamesCount;
    private char sign = 0;
    private Game currentGame;
    private Random random = new Random();

    @GetMapping({"", "/"})
    public String goToNewGame() {
        sign = gamesCount % 2 == 0 ? 'x' : 'o';
        gamesCount++;
        currentGame = new Game();
        return "redirect:/game/" + currentGame.getId();
    }

    @GetMapping("/game/{id}")
    public String getNewGame(Model model) {
        if (sign == 'o') {
            currentGame.makeXMove(random.nextInt(2), random.nextInt(2));
        }
        char[][] state = currentGame.getState();
        model.addAttribute("grid", state);
        return "game";
    }

    @ModelAttribute("playerSign")
    public char playerSign() {
        return sign;
    }

    @ModelAttribute("opponentSign")
    public char opponentSign() {
        return sign == 'o' ? 'x' : 'o';
    }
}
