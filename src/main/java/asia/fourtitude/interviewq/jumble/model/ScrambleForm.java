package asia.fourtitude.interviewq.jumble.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ScrambleForm {

    @NotBlank
    @Size(min = 3, max = 30)
    private String word;

    private String scramble;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (word != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("word=[").append(word).append(']');
        }
        if (scramble != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("scramble=[").append(scramble).append(']');
        }
        return sb.toString();
    }

}
