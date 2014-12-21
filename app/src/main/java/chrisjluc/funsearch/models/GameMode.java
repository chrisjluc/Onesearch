package chrisjluc.funsearch.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GameMode implements Serializable {

    private GameType type;
    private GameDifficulty difficulty;
    private long time;

    public GameMode(GameType type, GameDifficulty difficulty, long time) {
        this.type = type;
        this.difficulty = difficulty;
        this.time = time;
    }

    public GameType getType() {
        return type;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public long getTime() {
        return time;
    }
}
