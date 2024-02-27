package asia.fourtitude.interviewq.jumble.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenVisitHomePage_thenExpectTimeNow() throws Exception {
        this.mockMvc.perform(get("/"))
                        .andDo(print())
                .andExpect(view().name("index"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The time now: ")));
    }

    @Test
    void givenValidInput_whenExecScramble_thenSuccess() throws Exception {
        String word = "qwerty";
        String result = "<p>Scrambled word: <span>" + word + "</span></p>";
        this.mockMvc.perform(post("/scramble")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("scramble"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString(result))));
    }

    @Test
    void givenEmptyWord_whenExecScramble_thenFailure() throws Exception {
        String word = "";
        String result = "must not be blank";
        this.mockMvc.perform(post("/scramble")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("scramble"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenWordLengthLt3_whenExecScramble_thenFailure() throws Exception {
        String word = "we";
        String result = "size must be between 3 and 30";
        this.mockMvc.perform(post("/scramble")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("scramble"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void whenVisitPalindrome_thenSuccess() throws Exception {
        this.mockMvc.perform(get("/palindrome"))
                        .andDo(print())
                .andExpect(view().name("palindrome"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<p>The number of palindrome words: <span>61</span></p>")));
    }

    @Test
    void givenExistWord_whenExecExists_thenExist() throws Exception {
        String word = "qwerty";
        String result = "<p>The word \"<span>" + word + "</span>\" exists.</p>";
        this.mockMvc.perform(post("/exists")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("exists"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenExistWordPadSpace_whenExecExists_thenExist() throws Exception {
        String word = "qwerty";
        word = new StringBuilder().append(' ').append(word).append(' ').toString();
        String result = "<p>The word \"<span>" + word + "</span>\" exists.</p>";
        this.mockMvc.perform(post("/exists")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("exists"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenNonExistWord_whenExecExists_thenNonExist() throws Exception {
        String word = "fourtitude";
        String result = "<p>The word \"<span>" + word + "</span>\" not exists.</p>";
        this.mockMvc.perform(post("/exists")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("exists"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenEmptyWord_whenExecExists_thenFailure() throws Exception {
        String word = "";
        String result = "must not be blank";
        this.mockMvc.perform(post("/exists")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("exists"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenValidPrefix_whenExecPrefix_thenSuccess() throws Exception {
        String prefix = "tomato";
        String result = "<p>The number of words: <span>2</span></p>";
        this.mockMvc.perform(post("/prefix")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("prefix", prefix))
                .andExpect(view().name("prefix"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenValidPrefixPadSpace_whenExecPrefix_thenSuccess() throws Exception {
        String prefix = "tomato";
        prefix = new StringBuilder().append(' ').append(prefix).append(' ').toString();
        String result = "<p>The number of words: <span>2</span></p>";
        this.mockMvc.perform(post("/prefix")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("prefix", prefix))
                .andExpect(view().name("prefix"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenInvalidPrefix_whenExecPrefix_thenSuccess() throws Exception {
        String prefix = "tomatos";
        String result = "<p>The number of words: <span>0</span></p>";
        this.mockMvc.perform(post("/prefix")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("prefix", prefix))
                .andExpect(view().name("prefix"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenEmptyPrefix_whenExecPrefix_thenFailure() throws Exception {
        String prefix = "";
        String result = "must not be blank";
        this.mockMvc.perform(post("/prefix")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("prefix", prefix))
                .andExpect(view().name("prefix"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void givenStartChar_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("startChar", "a"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenEndChar_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("endChar", "a"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenLength_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("length", "1"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenStartCharEndChar_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("startChar", "a")
                        .param("endChar", "a"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));

        this.mockMvc.perform(post("/search")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("startChar", "a")
                    .param("endChar", ""))
            .andExpect(view().name("search"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("The number of words: ")));

        this.mockMvc.perform(post("/search")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("startChar", "")
                    .param("endChar", "a"))
            .andExpect(view().name("search"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenStartCharLength_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("startChar", "a")
                        .param("length", "5"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenEndCharLength_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("endChar", "a")
                        .param("length", "5"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenStartCharEndCharLength_whenExecSearch_thenSuccess() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("startChar", "a")
                        .param("endChar", "a")
                        .param("length", "5"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The number of words: ")));
    }

    @Test
    void givenAllEmpty_whenExecSearch_thenFailure() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("startChar", "")
                        .param("endChar", "")
                        .param("length", ""))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Invalid startChar")))
                .andExpect(content().string(containsString("Invalid endChar")))
                .andExpect(content().string(containsString("Invalid length")));
    }

    @Test
    void givenStartCharLengthGe3_whenExecSearch_thenFailure() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("startChar", "are"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<label id=\"iptStartCharFeedback\" class=\"col-sm-4 text-danger is-invalid\">size must be between 0 and 1</label>")));
    }

    @Test
    void givenEndCharLengthGe2_whenExecSearch_thenFailure() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("endChar", "at"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<label id=\"iptEndCharFeedback\" class=\"col-sm-4 text-danger is-invalid\">size must be between 0 and 1</label>")));
    }

    @Test
    void givenInvalidLength_whenExecSearch_thenFailure() throws Exception {
        this.mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("length", "a"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<label id=\"iptLengthFeedback\" class=\"col-sm-4 text-danger is-invalid\">Failed to convert property value of type java.lang.String to required type java.lang.Integer for property length; nested exception is java.lang.NumberFormatException: For input string: &quot;a&quot;</label>")));
    }

    @Test
    void givenValidWord_whenExecSubWords_thenSuccess() throws Exception {
        String word = "tomato";
        this.mockMvc.perform(post("/subWords")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("subWords"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<p>The number of words: <span>13</span></p>")));
    }

    @Test
    void givenInvalidWord_whenExecSubWords_thenSuccess() throws Exception {
        String word = "drucke";
        this.mockMvc.perform(post("/subWords")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("subWords"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<p>The number of words: <span>22</span></p>")));
    }

    @Test
    void givenWordPadSpace_whenExecSubWords_thenSuccess() throws Exception {
        String word = " duck ";
        this.mockMvc.perform(post("/subWords")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word))
                .andExpect(view().name("subWords"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<p>The number of words: <span>1</span></p>")));
    }

    @Test
    void givenValidWordValidMinLength_whenExecSubWords_thenSuccess() throws Exception {
        String word = "tomato";
        this.mockMvc.perform(post("/subWords")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word)
                        .param("minLength", "4"))
                .andExpect(view().name("subWords"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<p>The number of words: <span>6</span></p>")));
    }

    @Test
    void givenValidWordGeMinLength_whenExecSubWords_thenSuccess() throws Exception {
        String word = "tomato";
        this.mockMvc.perform(post("/subWords")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word)
                        .param("minLength", Integer.toString(word.length())))
                .andExpect(view().name("subWords"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<p>The number of words: <span>0</span></p>")));
    }

    @Test
    void givenEmptyWord_whenExecSubWords_thenFailure() throws Exception {
        String word = "";
        this.mockMvc.perform(post("/subWords")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", word)
                        .param("minLength", "9"))
                .andExpect(view().name("subWords"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<label id=\"iptWordFeedback\" class=\"col-sm-4 text-danger is-invalid\">")));
    }

}
