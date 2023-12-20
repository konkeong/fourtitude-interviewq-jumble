package asia.fourtitude.interviewq.jumble.model;

import asia.fourtitude.interviewq.jumble.core.GameState;

public class GameBoard {

    private GameState state;

    private String word;

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (state != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("state=[").append(state).append(']');
        }
        if (word != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("word=[").append(word).append(']');
        }
        return sb.toString();
    }

}
