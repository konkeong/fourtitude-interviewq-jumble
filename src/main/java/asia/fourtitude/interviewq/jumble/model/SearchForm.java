package asia.fourtitude.interviewq.jumble.model;

import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SearchForm {

    @Size(min = 0, max = 1, message = "size must be between 0 and 1")
    @NotBlank(message = "Invalid startChar")
    private String startChar;
    
    @Size(min = 0, max = 1, message = "size must be between 0 and 1")
    @NotBlank(message = "Invalid endChar")
    private String endChar;

    @javax.validation.constraints.Positive
    @NotNull(message = "Invalid length")
    private Integer length;

    private Collection<String> words;

    public String getStartChar() {
        return startChar;
    }

    public void setStartChar(String startChar) {
        this.startChar = startChar;
    }

    public String getEndChar() {
        return endChar;
    }

    public void setEndChar(String endChar) {
        this.endChar = endChar;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Collection<String> getWords() {
        return words;
    }

    public void setWords(Collection<String> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (startChar != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("startChar=[").append(startChar).append(']');
        }
        if (endChar != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("endChar=[").append(endChar).append(']');
        }
        if (length != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("length=[").append(length).append(']');
        }
        if (words != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("words=[").append(words).append(']');
        }
        return sb.toString();
    }

}
