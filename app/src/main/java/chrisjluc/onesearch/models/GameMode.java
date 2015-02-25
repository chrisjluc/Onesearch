package chrisjluc.onesearch.models;


public class GameMode {

    private String type;
    private String difficulty;
    private long time;

    public GameMode(String type, String difficulty, long time) {
        this.type = type;
        this.difficulty = difficulty;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public long getTime() {
        return time;
    }
}
