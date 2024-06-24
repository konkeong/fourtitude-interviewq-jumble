package asia.fourtitude.interviewq.jumble.model;

public class ExistsForm {

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
