package asia.fourtitude.interviewq.jumble.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import asia.fourtitude.interviewq.jumble.TestConfig;
import asia.fourtitude.interviewq.jumble.core.GameState;
import asia.fourtitude.interviewq.jumble.core.JumbleEngine;
import asia.fourtitude.interviewq.jumble.model.GameGuessInput;
import asia.fourtitude.interviewq.jumble.model.GameGuessModel;
import asia.fourtitude.interviewq.jumble.model.GameGuessOutput;

@WebMvcTest(GameApiController.class)
@Import(TestConfig.class)
class GameApiControllerTest {

        static final ObjectMapper OM = new ObjectMapper();

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        JumbleEngine jumbleEngine;

        private GameGuessOutput output;

        private GameGuessInput gameGuessInput;

        private GameGuessModel gameGuessModel;

        private GameState gameState;

        private Map<String, GameGuessModel> gameBoards;

        /*
         * NOTE: Refer to "RootControllerTest.java", "GameWebControllerTest.java"
         * as reference. Search internet for resource/tutorial/help in implementing
         * the unit tests.
         *
         * Refer to "http://localhost:8080/swagger-ui/index.html" for REST API
         * documentation and perform testing.
         *
         * Refer to Postman collection ("interviewq-jumble.postman_collection.json")
         * for REST API documentation and perform testing.
         */

        @BeforeEach
        void setup() throws IOException {
                gameState = this.jumbleEngine.createGameState(6, 3);

                // initialize the gameBoards
                this.gameBoards = new ConcurrentHashMap<>();

                output = new GameGuessOutput();
                gameGuessModel = new GameGuessModel();

                // initialize the output
                output.setResult("Created new game.");
                output.setId(UUID.randomUUID().toString());
                output.setOriginalWord(gameState.getOriginal());
                output.setScrambleWord(gameState.getScramble());
                output.setTotalWords(gameState.getSubWords().size());
                output.setRemainingWords(output.getTotalWords());
                output.setGuessedWords(gameState.getGuessedWords());

                // initialize game guess model
                gameGuessModel.setCreatedAt(new Date());
                gameGuessModel.setGameState(gameState);
                gameGuessModel.setId(output.getId());

                // store game guess model into hashmap with corresponding id
                gameBoards.put(output.getId(), gameGuessModel);
        }

        @Test
        void whenCreateNewGame_thenSuccess() throws Exception {
                /*
                 * Doing HTTP GET "/api/game/new"
                 *
                 * Input: None
                 *
                 * Expect: Assert these
                 * a) HTTP status == 200
                 * b) `result` equals "Created new game."
                 * c) `id` is not null
                 * d) `originalWord` is not null
                 * e) `scrambleWord` is not null
                 * f) `totalWords` > 0
                 * g) `remainingWords` > 0 and same as `totalWords`
                 * h) `guessedWords` is empty list
                 */
                MvcResult result = this.mockMvc.perform(get("/api/game/new"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value("Created new game."))
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.original_word").isNotEmpty())
                                .andExpect(jsonPath("$.scramble_word").isNotEmpty())
                                .andExpect(jsonPath("$.total_words").isNumber())
                                .andExpect(jsonPath("$.remaining_words").isNumber())
                                .andExpect(jsonPath("$.guessed_words").isArray())
                                .andExpect(jsonPath("$.guessed_words").isEmpty())
                                .andReturn();

                String contentAsString = result.getResponse().getContentAsString();

                int remainingWords = JsonPath.read(contentAsString, "$.remaining_words");
                int totalWords = JsonPath.read(contentAsString, "$.total_words");

                assertEquals(remainingWords, totalWords);
        }

        @Test
        void givenMissingId_whenPlayGame_thenInvalidId() throws Exception {
                /*
                 * Doing HTTP POST "/api/game/guess"
                 *
                 * Input: JSON request body
                 * a) `id` is null or missing
                 * b) `word` is null/anything or missing
                 *
                 * Expect: Assert these
                 * a) HTTP status == 404
                 * b) `result` equals "Invalid Game ID."
                 */
                String jsonString = "{ \"id\": null, \"word\": null }";

                RequestBuilder request = MockMvcRequestBuilders.post("/api/game/guess")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString);

                mockMvc.perform(request)
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.result").value("Invalid Game ID."));
        }

        @Test
        void givenMissingRecord_whenPlayGame_thenRecordNotFound() throws Exception {
                /*
                 * Doing HTTP POST "/api/game/guess"
                 *
                 * Input: JSON request body
                 * a) `id` is some valid ID (but not exists in game system)
                 * b) `word` is null/anything or missing
                 *
                 * Expect: Assert these
                 * a) HTTP status == 404
                 * b) `result` equals "Game board/state not found."
                 */
                String jsonInput = "{ \"id\": \"invalid-game-id\", \"word\": null }";

                RequestBuilder request = MockMvcRequestBuilders.post("/api/game/guess")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput);

                this.mockMvc.perform(request)
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.result").value("Game board/state not found."));

                String gameId = JsonPath.read(jsonInput, "$.id");

                assertTrue(!gameBoards.containsKey(gameId));
        }

        @Test
        void givenCreateNewGame_whenSubmiNullWord_thenGuessedIncorrectly() throws Exception {
                /*
                 * Doing HTTP POST "/api/game/guess"
                 *
                 * Given:
                 * a) has valid game ID from previously created game
                 *
                 * Input: JSON request body
                 * a) `id` of previously created game
                 * b) `word` is null or missing
                 *
                 * Expect: Assert these
                 * a) HTTP status == 200
                 * b) `result` equals "Guessed incorrectly."
                 * c) `id` equals to `id` of this game
                 * d) `originalWord` is equals to `originalWord` of this game
                 * e) `scrambleWord` is not null
                 * f) `guessWord` is equals to `input.word`
                 * g) `totalWords` is equals to `totalWords` of this game
                 * h) `remainingWords` is equals to `remainingWords` of previous game state (no
                 * change)
                 * i) `guessedWords` is empty list (because this is first attempt)
                 */

                // first need to create new game
                MvcResult newGameResult = this.mockMvc.perform(get("/api/game/new"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value("Created new game."))
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.original_word").isNotEmpty())
                                .andExpect(jsonPath("$.scramble_word").isNotEmpty())
                                .andExpect(jsonPath("$.total_words").isNumber())
                                .andExpect(jsonPath("$.remaining_words").isNumber())
                                .andExpect(jsonPath("$.guessed_words").isArray())
                                .andExpect(jsonPath("$.guessed_words").isEmpty())
                                .andReturn();

                // then get the game id based on the response
                String newGameResultResponse = newGameResult.getResponse().getContentAsString();
                String gameId = JsonPath.read(newGameResultResponse, "$.id");
                String guessWord = null;

                String jsonInput = String.format("{ \"id\": \"%s\", \"word\": %s }", gameId,
                                guessWord != null ? "\"" + guessWord + "\"" : "null");

                // given valid id and null/missing guess word, the response should be ok
                RequestBuilder request = MockMvcRequestBuilders.post("/api/game/guess")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput);

                MvcResult guessResult = this.mockMvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.result").value("Guessed Incorrectly."))
                                .andReturn();

                // then check if all content in guessResult is equal to newGameResult
                String guessResultString = guessResult.getResponse().getContentAsString();

                String newGameId = JsonPath.read(newGameResultResponse, "$.id");
                String guessId = JsonPath.read(guessResultString, "$.id");
                assertEquals(guessId, newGameId);

                String newGameOgWord = JsonPath.read(newGameResultResponse, "$.original_word");
                String guessOgWord = JsonPath.read(guessResultString, "$.original_word");
                assertEquals(newGameOgWord, guessOgWord);

                String guessWordOutput = JsonPath.read(guessResultString, "$.guess_word");
                assertEquals(guessWordOutput, "null");

                int newGameTotalWords = JsonPath.read(newGameResultResponse, "$.total_words");
                int guessOutputTotalWords = JsonPath.read(guessResultString, "$.total_words");
                assertEquals(guessOutputTotalWords, newGameTotalWords);

                int newGame_remainingWords = JsonPath.read(newGameResultResponse, "$.remaining_words");
                int guessOutput_remainingWords = JsonPath.read(guessResultString, "$.remaining_words");
                assertEquals(newGame_remainingWords, guessOutput_remainingWords);

                int guessed_words_size = JsonPath.read(guessResultString, "$.guessed_words.size()");
                assertEquals(0, guessed_words_size);
        }

        @Test
        void givenCreateNewGame_whenSubmitWrongWord_thenGuessedIncorrectly() throws Exception {
                /*
                 * Doing HTTP POST "/api/game/guess"
                 *
                 * Given:
                 * a) has valid game ID from previously created game
                 *
                 * Input: JSON request body
                 * a) `id` of previously created game
                 * b) `word` is some value (that is not correct answer)
                 *
                 * Expect: Assert these
                 * a) HTTP status == 200
                 * b) `result` equals "Guessed incorrectly."
                 * c) `id` equals to `id` of this game
                 * d) `originalWord` is equals to `originalWord` of this game
                 * e) `scrambleWord` is not null
                 * f) `guessWord` equals to input `guessWord`
                 * g) `totalWords` is equals to `totalWords` of this game
                 * h) `remainingWords` is equals to `remainingWords` of previous game state (no
                 * change)
                 * i) `guessedWords` is empty list (because this is first attempt)
                 */
                // first need to create new game
                MvcResult newGameResult = this.mockMvc.perform(get("/api/game/new"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value("Created new game."))
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.original_word").isNotEmpty())
                                .andExpect(jsonPath("$.scramble_word").isNotEmpty())
                                .andExpect(jsonPath("$.total_words").isNumber())
                                .andExpect(jsonPath("$.remaining_words").isNumber())
                                .andExpect(jsonPath("$.guessed_words").isArray())
                                .andExpect(jsonPath("$.guessed_words").isEmpty())
                                .andReturn();

                // then get the game id based on the response
                String newGameResultResponse = newGameResult.getResponse().getContentAsString();
                String gameId = JsonPath.read(newGameResultResponse, "$.id");
                String guessWord = "sambalnyet";

                String jsonInput = String.format("{ \"id\": \"%s\", \"word\": %s }", gameId,
                                guessWord != null ? "\"" + guessWord + "\"" : "null");

                // given valid id and wrong guess word, the response should be ok
                RequestBuilder request = MockMvcRequestBuilders.post("/api/game/guess")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInput);

                MvcResult guessResult = this.mockMvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.result").value("Guessed Incorrectly."))
                                .andReturn();

                // then check if all content in guessResult is equal to newGameResult
                String guessResultString = guessResult.getResponse().getContentAsString();

                String newGameId = JsonPath.read(newGameResultResponse, "$.id");
                String guessId = JsonPath.read(guessResultString, "$.id");
                assertEquals(guessId, newGameId);

                String newGameOgWord = JsonPath.read(newGameResultResponse, "$.original_word");
                String guessOgWord = JsonPath.read(guessResultString, "$.original_word");
                assertEquals(newGameOgWord, guessOgWord);

                String guessWordOutput = JsonPath.read(guessResultString, "$.guess_word");
                assertEquals(guessWord, guessWordOutput);

                int newGameTotalWords = JsonPath.read(newGameResultResponse, "$.total_words");
                int guessOutputTotalWords = JsonPath.read(guessResultString, "$.total_words");
                assertEquals(guessOutputTotalWords, newGameTotalWords);

                int newGame_remainingWords = JsonPath.read(newGameResultResponse, "$.remaining_words");
                int guessOutput_remainingWords = JsonPath.read(guessResultString, "$.remaining_words");
                assertEquals(newGame_remainingWords, guessOutput_remainingWords);

                int guessed_words_size = JsonPath.read(guessResultString, "$.guessed_words.size()");
                assertEquals(0, guessed_words_size);
        }

        @Test
        void givenCreateNewGame_whenSubmitFirstCorrectWord_thenGuessedCorrectly() throws Exception {
                /*
                 * Doing HTTP POST "/api/game/guess"
                 *
                 * Given:
                 * a) has valid game ID from previously created game
                 *
                 * Input: JSON request body
                 * a) `id` of previously created game
                 * b) `word` is of correct answer
                 *
                 * Expect: Assert these
                 * a) HTTP status == 200
                 * b) `result` equals "Guessed correctly."
                 * c) `id` equals to `id` of this game
                 * d) `originalWord` is equals to `originalWord` of this game
                 * e) `scrambleWord` is not null
                 * f) `guessWord` equals to input `guessWord`
                 * g) `totalWords` is equals to `totalWords` of this game
                 * h) `remainingWords` is equals to `remainingWords - 1` of previous game state
                 * (decrement by 1)
                 * i) `guessedWords` is not empty list
                 * j) `guessWords` contains input `guessWord`
                 */
                // first need to create new game
                MvcResult newGameResult = this.mockMvc.perform(get("/api/game/new"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value("Created new game."))
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.original_word").isNotEmpty())
                                .andExpect(jsonPath("$.scramble_word").isNotEmpty())
                                .andExpect(jsonPath("$.total_words").isNumber())
                                .andExpect(jsonPath("$.remaining_words").isNumber())
                                .andExpect(jsonPath("$.guessed_words").isArray())
                                .andExpect(jsonPath("$.guessed_words").isEmpty())
                                .andReturn();

                // then get the game id based on the response
                String newGameResultResponse = newGameResult.getResponse().getContentAsString();
                String gameId = JsonPath.read(newGameResultResponse, "$.id");

                // guessWord need to retrieve from the GameState
                // need to implement logic here to get valid subword
                String theGivenWord = JsonPath.read(newGameResultResponse, "$.original_word");

                // get the valid subword(s) from the theGivenWord
                Set<String> validSubwords = (Set<String>) jumbleEngine.generateSubWords(theGivenWord, null);

                // get word from the collection of validSubwords
                String validSubwordInput = validSubwords.iterator().next();

                // use the validSubwordInput and gameId to send a post request
                String validJsonInputString = String.format("{ \"id\": \"%s\", \"word\": \"%s\" }", gameId,
                                validSubwordInput);

                // then make a post request
                // given valid id and null/missing guess word, the response should be ok
                RequestBuilder request = MockMvcRequestBuilders.post("/api/game/guess")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJsonInputString);

                MvcResult guessResult = this.mockMvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.result").value("Guessed correctly."))
                                .andReturn();

                // then finally validate the guessResult response
                String guessResultString = guessResult.getResponse().getContentAsString();

                String newGameId = JsonPath.read(newGameResultResponse, "$.id");
                String guessId = JsonPath.read(guessResultString, "$.id");
                assertEquals(guessId, newGameId);

                String newGameOgWord = JsonPath.read(newGameResultResponse, "$.original_word");
                String guessOgWord = JsonPath.read(guessResultString, "$.original_word");
                assertEquals(newGameOgWord, guessOgWord);

                String guessWordOutput = JsonPath.read(guessResultString, "$.guess_word");
                assertEquals(validSubwordInput, guessWordOutput);

                int newGameTotalWords = JsonPath.read(newGameResultResponse, "$.total_words");
                int guessOutputTotalWords = JsonPath.read(guessResultString, "$.total_words");
                assertEquals(guessOutputTotalWords, newGameTotalWords);

                int newGame_remainingWords = JsonPath.read(newGameResultResponse, "$.remaining_words");
                int guessOutput_remainingWords = JsonPath.read(guessResultString, "$.remaining_words");

                int expectedRemainingWords = newGame_remainingWords - 1;
                assertEquals(expectedRemainingWords, guessOutput_remainingWords);

                int guessed_words_size = JsonPath.read(guessResultString, "$.guessed_words.size()");
                assertTrue(guessed_words_size > 0);

                // check if guessed_words contain the input guessWord
                List<String> guessOutputGuessedWords = JsonPath.read(guessResultString, "$.guessed_words");
                assertTrue(guessOutputGuessedWords.contains(validSubwordInput));
        }

        @Test
        void givenCreateNewGame_whenSubmitAllCorrectWord_thenAllGuessed() throws Exception {
                /*
                 * Doing HTTP POST "/api/game/guess"
                 *
                 * Given:
                 * a) has valid game ID from previously created game
                 * b) has submit all correct answers, except the last answer
                 *
                 * Input: JSON request body
                 * a) `id` of previously created game
                 * b) `word` is of the last correct answer
                 *
                 * Expect: Assert these
                 * a) HTTP status == 200
                 * b) `result` equals "All words guessed."
                 * c) `id` equals to `id` of this game
                 * d) `originalWord` is equals to `originalWord` of this game
                 * e) `scrambleWord` is not null
                 * f) `guessWord` equals to input `guessWord`
                 * g) `totalWords` is equals to `totalWords` of this game
                 * h) `remainingWords` is 0 (no more remaining, game ended)
                 * i) `guessedWords` is not empty list
                 * j) `guessWords` contains input `guessWord`
                 */
                // first need to create new game
                MvcResult newGameResult = this.mockMvc.perform(get("/api/game/new"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value("Created new game."))
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.original_word").isNotEmpty())
                                .andExpect(jsonPath("$.scramble_word").isNotEmpty())
                                .andExpect(jsonPath("$.total_words").isNumber())
                                .andExpect(jsonPath("$.remaining_words").isNumber())
                                .andExpect(jsonPath("$.guessed_words").isArray())
                                .andExpect(jsonPath("$.guessed_words").isEmpty())
                                .andReturn();

                // then get the game id based on the response
                String newGameResultResponse = newGameResult.getResponse().getContentAsString();
                String gameId = JsonPath.read(newGameResultResponse, "$.id");

                // guessWord need to retrieve from the GameState
                // need to implement logic here to get valid subword
                String theGivenWord = JsonPath.read(newGameResultResponse, "$.original_word");

                // get the valid subword(s) from the theGivenWord
                Set<String> validSubwords = (Set<String>) jumbleEngine.generateSubWords(theGivenWord, null);

                // make n api-calls for every word in validSubwords
                List<String> listOfSubWords = new ArrayList<>(validSubwords);

                RequestBuilder request;
                String validJsonInputString = "";
                for (int i = 0; i < listOfSubWords.size() - 1; i++) {
                        // use the validSubwordInput and gameId to send a post request
                        validJsonInputString = String.format("{ \"id\": \"%s\", \"word\": \"%s\" }", gameId,
                                        listOfSubWords.get(i));

                        request = MockMvcRequestBuilders.post("/api/game/guess")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(validJsonInputString);

                        this.mockMvc.perform(request)
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.result").value("Guessed correctly."));
                }

                // by now, we need to submit the last word to guess,
                validJsonInputString = String.format("{ \"id\": \"%s\", \"word\": \"%s\" }", gameId,
                                listOfSubWords.get(listOfSubWords.size() - 1));

                // make the final post request ...
                request = MockMvcRequestBuilders.post("/api/game/guess")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validJsonInputString);

                MvcResult guessResult = this.mockMvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.result").value("All words guessed."))
                                .andReturn();

                // then finally validate the guessResult response
                String guessResultString = guessResult.getResponse().getContentAsString();

                String newGameId = JsonPath.read(newGameResultResponse, "$.id");
                String guessId = JsonPath.read(guessResultString, "$.id");
                assertEquals(guessId, newGameId);

                String newGameOgWord = JsonPath.read(newGameResultResponse, "$.original_word");
                String guessOgWord = JsonPath.read(guessResultString, "$.original_word");
                assertEquals(newGameOgWord, guessOgWord);

                String guessWordOutput = JsonPath.read(guessResultString, "$.guess_word");
                assertEquals(listOfSubWords.get(listOfSubWords.size() - 1), guessWordOutput);

                int newGameTotalWords = JsonPath.read(newGameResultResponse, "$.total_words");
                int guessOutputTotalWords = JsonPath.read(guessResultString, "$.total_words");
                assertEquals(guessOutputTotalWords, newGameTotalWords);

                int guessOutput_remainingWords = JsonPath.read(guessResultString, "$.remaining_words");

                assertEquals(0, guessOutput_remainingWords);

                int guessed_words_size = JsonPath.read(guessResultString, "$.guessed_words.size()");
                assertTrue(guessed_words_size > 0);

                // `guessedWords` is not empty list
                assertTrue(guessed_words_size > 0);

                // check if `guessWords` contains input `guessWord`
                assertEquals(listOfSubWords.size(), guessed_words_size);
        }

}
