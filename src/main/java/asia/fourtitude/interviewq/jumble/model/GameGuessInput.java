package asia.fourtitude.interviewq.jumble.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@JsonInclude(Include.NON_NULL)
public class GameGuessInput {

    @Schema(
            title = "ID",
            description = "Unique identifier of the game state.",
            example = "4579256c-326f-4169-9b56-6d1d1a2c11f0",
            nullable = false,
            requiredMode = RequiredMode.REQUIRED)
    @NotNull
    private String id;

    @Schema(
            title = "Word",
            description = "The word to guess.",
            example = "answer",
            minLength = 3,
            maxLength = 30,
            nullable = false,
            requiredMode = RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 3, max = 30)
    private String word;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (id != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("id=[").append(id).append(']');
        }
        if (word != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("word=[").append(word).append(']');
        }
        return sb.toString();
    }

}
