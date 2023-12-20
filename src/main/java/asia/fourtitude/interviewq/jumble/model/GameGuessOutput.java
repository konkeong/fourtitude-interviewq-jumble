package asia.fourtitude.interviewq.jumble.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@JsonInclude(Include.NON_NULL)
public class GameGuessOutput {

    @Schema(
            title = "Result",
            description = "Result message.",
            example = "AnyOf[\"Guessed correctly\", \"Guessed incorrectly\", \"Guessed already\"]",
            requiredMode = RequiredMode.AUTO)
    private String result;

    @Schema(
            title = "ID",
            description = "Unique identifier of the game state.",
            example = "4579256c-326f-4169-9b56-6d1d1a2c11f0",
            requiredMode = RequiredMode.AUTO)
    private String id;

    @Schema(
            description = "Original word in game.",
            example = "tomato",
            minLength = 3,
            maxLength = 30,
            requiredMode = RequiredMode.AUTO)
    @JsonProperty(value = "original_word")
    @Size(min = 3, max = 30)
    private String originalWord;

    @Schema(
            description = "Scramble letters of the word in game.",
            example = "amotto",
            minLength = 3,
            maxLength = 30,
            requiredMode = RequiredMode.AUTO)
    @JsonProperty(value = "scramble_word")
    @Size(min = 3, max = 30)
    private String scrambleWord;

    @Schema(
            description = "The word used in guessing play, if available.",
            example = "motto",
            minLength = 3,
            maxLength = 30,
            requiredMode = RequiredMode.AUTO)
    @JsonProperty(value = "guess_word")
    @Size(min = 3, max = 30)
    private String guessWord;

    @Schema(
            description = "The numbers of smaller/sub words, constructed using the letters from `original_word`.",
            example = "31",
            defaultValue = "0",
            requiredMode = RequiredMode.AUTO)
    @JsonProperty(value = "total_words")
    private int totalWords;

    @Schema(
            description = "The numbers of remaining smaller/sub words to guess.",
            example = "23",
            defaultValue = "0",
            requiredMode = RequiredMode.AUTO)
    @JsonProperty(value = "remaining_words")
    private int remainingWords;

    @Schema(
            description = "The list of words guessed correctly.",
            example = "EMPTY_LIST",
            requiredMode = RequiredMode.AUTO)
    @JsonProperty(value = "guessed_words")
    private List<String> guessedWords;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    public String getScrambleWord() {
        return scrambleWord;
    }

    public void setScrambleWord(String scrambleWord) {
        this.scrambleWord = scrambleWord;
    }

    public String getGuessWord() {
        return guessWord;
    }

    public void setGuessWord(String guessWord) {
        this.guessWord = guessWord;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getRemainingWords() {
        return remainingWords;
    }

    public void setRemainingWords(int remainingWords) {
        this.remainingWords = remainingWords;
    }

    public List<String> getGuessedWords() {
        if (guessedWords == null) {
            guessedWords = new ArrayList<>();
        }
        return guessedWords;
    }

    public void setGuessedWords(List<String> guessedWords) {
        this.guessedWords = guessedWords;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("id=[").append(id).append(']');
        }
        if (result != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("result=[").append(result).append(']');
        }
        if (originalWord != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("originalWord=[").append(originalWord).append(']');
        }
        if (scrambleWord != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("scrambleWord=[").append(scrambleWord).append(']');
        }
        if (guessWord != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("guessWord=[").append(guessWord).append(']');
        }
        sb.append(sb.length() == 0 ? "" : ", ").append("totalWords=[").append(totalWords).append(']');
        sb.append(sb.length() == 0 ? "" : ", ").append("remainingWords=[").append(remainingWords).append(']');
        if (guessedWords != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("guessedWords.size=[").append(guessedWords.size()).append(']');
        }
        return sb.toString();
    }

}
