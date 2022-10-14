package kata.tictactoe.controllers;

import kata.tictactoe.model.Game;
import kata.tictactoe.model.GameResult;
import kata.tictactoe.model.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Random;

@Controller
@SessionScope
public class GameController {

    private int gamesCount;
    private char sign = 0;
    private Game currentGame;
    private Random random = new Random();
    private GameResult result;

    @GetMapping({"", "/"})
    public String goToNewGame() {
        sign = gamesCount % 2 == 0 ? 'x' : 'o';
        gamesCount++;
        currentGame = new Game();
        result = null;
        if (sign == 'o') {
            currentGame.makeMove('x', random.nextInt(2), random.nextInt(2));
        }
        return "redirect:/game/" + currentGame.getId();
    }

    @GetMapping("/game/{id}")
    public String getNewGame(Model model) {
        return "game";
    }

    @PostMapping("/game/{id}")
    public String makeMove(@ModelAttribute("coordinate") String coordinates, Model model) {
        result = currentGame.makeMove(sign,
                Integer.valueOf(coordinates.substring(0, 1)),
                Integer.valueOf(coordinates.substring(1, 2)));
        if(result.getResult() == Result.INPROGRESS){
            result = computerMove();
        }
        if(result.getResult() == Result.OWINS){
            model.addAttribute("winner", sign == 'o');
        }else if(result.getResult() == Result.XWINS){
            model.addAttribute("winner", sign == 'x');
        }else if(result.getResult() == Result.DRAW){
            model.addAttribute("draw", true);
        }
        model.addAttribute("startPos", result.getStartPos());
        model.addAttribute("endPos", result.getEndPos());
        model.addAttribute("gameOver", result.getResult() != Result.INPROGRESS);
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

    @ModelAttribute("game")
    public Game gameGrid() {
        return currentGame;
    }

    private GameResult computerMove() {
        char[][] state = currentGame.getState();
        char o = sign == 'o' ? 'x' : 'o';
        for (int l = 0; l < 3; l++) {
            for (int c = 0; c < 3; c++) {
                if (state[l][c] == 0) {
                    return currentGame.makeMove(o, l, c);
                }
            }
        }
        return null;
    }
}
