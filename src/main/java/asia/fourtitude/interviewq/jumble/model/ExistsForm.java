package asia.fourtitude.interviewq.jumble.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ExistsForm {

    @NotBlank
    @Size(min = 1, max = 30)
    private String word;

    private Boolean exists;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (word != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("word=[").append(word).append(']');
        }
        if (exists != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("exists=[").append(exists).append(']');
        }
        return sb.toString();
    }

}
