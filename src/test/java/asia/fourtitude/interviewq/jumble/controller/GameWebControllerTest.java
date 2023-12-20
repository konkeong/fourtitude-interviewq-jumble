package asia.fourtitude.interviewq.jumble.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import asia.fourtitude.interviewq.jumble.TestConfig;
import asia.fourtitude.interviewq.jumble.model.GameBoard;

@WebMvcTest(GameWebController.class)
@Import(TestConfig.class)
class GameWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenVisitGoodbye_thenExpectSuccess() throws Exception {
        this.mockMvc.perform(get("/game/goodbye"))
                .andDo(print())
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk());
    }

    @Test
    void whenVisitHelp_thenExpectSuccess() throws Exception {
        this.mockMvc.perform(get("/game/help"))
                .andExpect(view().name("game/help"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Guess Words - Help")));
    }

    @Test
    void whenVisitNew_thenExpectSuccess() throws Exception {
        MvcResult resu = this.mockMvc.perform(get("/game/new"))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andReturn();
        GameBoard board = (GameBoard) resu.getModelAndView().getModel().get("board");
        assertNotNull(board.getState(), "board.state");
        assertNotNull(board.getState().getOriginal(), "board.state.original");
        assertNotNull(board.getState().getScramble(), "board.state.scramble");
        assertNotNull(board.getState().getSubWords(), "board.state.subWords");
        for (Map.Entry<String, Boolean> entry : board.getState().getSubWords().entrySet()) {
            assertNotNull(entry.getKey(), "board.state.subWords[].key");
            assertFalse(entry.getValue(), "board.state.subWords[].value");
        }
        assertNotNull(board.getState().getGuessedWords(), "board.state.guessedWords");
        assertTrue(board.getState().getGuessedWords().isEmpty(), "board.state.guessedWords.isEmpty");
        assertEquals("", board.getWord(), "board.word");
    }

    @Test
    void whenVisitPlayBeforeNew_thenExpectEmptyGameState() throws Exception {
        MvcResult resu = this.mockMvc.perform(get("/game/play"))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andReturn();
        GameBoard board = (GameBoard) resu.getModelAndView().getModel().get("board");
        assertNull(board.getState(), "board.state");
    }

    @Test
    void givenVisitNew_whenVisitPlay_thenExpectValidGameState() throws Exception {
        // visit "new" first
        MvcResult resu = this.mockMvc.perform(get("/game/new"))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andReturn();
        MockHttpSession session = (MockHttpSession) resu.getRequest().getSession();
        GameBoard board = (GameBoard) resu.getModelAndView().getModel().get("board");
        String oldScramble = board.getState().getScramble();

        // then visit "play" (fresh)
        resu = this.mockMvc.perform(get("/game/play")
                        .session(session))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andReturn();
        board = (GameBoard) resu.getModelAndView().getModel().get("board");
        assertNotNull(board.getState(), "board.state");
        assertNotNull(board.getState().getOriginal(), "board.state.original");
        assertNotNull(board.getState().getScramble(), "board.state.scramble");
        assertNotEquals(oldScramble, board.getState().getScramble(), "board.state.scramble");
        assertNotNull(board.getState().getSubWords(), "board.state.subWords");
        for (Map.Entry<String, Boolean> entry : board.getState().getSubWords().entrySet()) {
            assertNotNull(entry.getKey(), "board.state.subWords[].key");
            assertFalse(entry.getValue(), "board.state.subWords[].value");
        }
        assertNotNull(board.getState().getGuessedWords(), "board.state.guessedWords");
        assertTrue(board.getState().getGuessedWords().isEmpty(), "board.state.guessedWords.isEmpty");
        assertEquals("", board.getWord(), "board.word");
    }

    @Test
    void givenVisitNewVisitPlay_whenSubmitPlay_thenExpectValidGameState() throws Exception {
        // visit "new" first
        MvcResult resu = this.mockMvc.perform(get("/game/new"))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andReturn();
        MockHttpSession session = (MockHttpSession) resu.getRequest().getSession();

        // then visit "play" (fresh)
        resu = this.mockMvc.perform(get("/game/play")
                        .session(session))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andExpect(content().string(containsString("No word guessed yet.")))
                .andReturn();
        GameBoard board = (GameBoard) resu.getModelAndView().getModel().get("board");
        String oldScramble = board.getState().getScramble();
        List<String> correctWords = new ArrayList<>(board.getState().getSubWords().keySet());
        assertTrue(correctWords.size() >= 2, "correctWords.size>=2");

        // submit a wrong word to play
        resu = this.mockMvc.perform(post("/game/play")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", oldScramble))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andExpect(content().string(containsString("Guessed incorrectly")))
                .andReturn();

        // submit a correct word to play
        String correctWord = correctWords.get(0);
        resu = this.mockMvc.perform(post("/game/play")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", correctWord))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andExpect(content().string(not(containsString("Guessed incorrectly"))))
                .andExpect(content().string(containsString("Remaining words: ")))
                .andReturn();
        board = (GameBoard) resu.getModelAndView().getModel().get("board");
        assertTrue(board.getState().getSubWords().get(correctWord), "board.state.subWords[].guessed");
        assertEquals(1, board.getState().getGuessedWords().size(), "board.state.guessedWords.size==1");

        // submit all, except the last one
        int numCorrect = 0;
        for (int pos = 0; pos < correctWords.size() - 1; pos += 1) {
            correctWord = correctWords.get(pos);
            resu = this.mockMvc.perform(post("/game/play")
                            .session(session)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("word", correctWord))
                    .andExpect(view().name("game/board"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("board"))
                    .andExpect(content().string(not(containsString("Guessed incorrectly"))))
                    .andExpect(content().string(containsString("Remaining words: ")))
                    .andReturn();
            board = (GameBoard) resu.getModelAndView().getModel().get("board");
            assertTrue(board.getState().getSubWords().get(correctWord), "board.state.subWords[].guessed");
            numCorrect += 1;
            assertEquals(numCorrect, board.getState().getGuessedWords().size(), "board.state.guessedWords.size==" + numCorrect);
        }

        // submit last word
        correctWord = correctWords.get(correctWords.size() - 1);
        resu = this.mockMvc.perform(post("/game/play")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("word", correctWord))
                .andExpect(view().name("game/board"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("board"))
                .andExpect(content().string(not(containsString("Guessed incorrectly"))))
                .andExpect(content().string(not(containsString("Remaining words: "))))
                .andExpect(content().string(containsString("<p>Click <a href=\"/game/new\">here</a> to start game.</p>")))
                .andReturn();
        board = (GameBoard) resu.getModelAndView().getModel().get("board");
        assertTrue(board.getState().getSubWords().get(correctWord), "board.state.subWords[].guessed");
        numCorrect += 1;
        assertEquals(numCorrect, board.getState().getGuessedWords().size(), "board.state.guessedWords.size==" + numCorrect);
    }

}
