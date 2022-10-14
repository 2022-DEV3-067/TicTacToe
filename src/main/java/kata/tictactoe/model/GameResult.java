package kata.tictactoe.model;

public class GameResult {
    private Result result;
    private Integer startPos;
    private Integer endPos;

    public GameResult(Result result, int startPos, int endPos) {
        this.result = result;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public GameResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public Integer getEndPos() {
        return endPos;
    }
}
