package asia.fourtitude.interviewq.jumble.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JumbleEngineTest {

    @Autowired
    JumbleEngine engine;

    @Test
    void accessResource() {
        assertNotNull(this.getClass().getClassLoader().getResourceAsStream("words.txt"), "accessWordList");
    }

    @Test
    void scramble() {
        String word = "elephant";
        String actual = engine.scramble(word);
        assertNotEquals(actual, word);

        word = "egg";
        for (int ix = 0; ix < 100; ix += 1) {
            String scrambled = engine.scramble(word);
            assertNotEquals(scrambled, word);
        }
    }

    @Test
    void palindrome() {
        assertEquals(61, engine.retrievePalindromeWords().size(), "retrievePalindromeWords.size()");
    }

    @Test
    void randomWord() {
        assertNotNull(engine.pickOneRandomWord(null), "length=null");
        assertEquals(3, engine.pickOneRandomWord(3).length(), "length=3");
        assertEquals(4, engine.pickOneRandomWord(4).length(), "length=4");
        assertEquals(5, engine.pickOneRandomWord(5).length(), "length=5");
        assertEquals(6, engine.pickOneRandomWord(6).length(), "length=6");
        assertEquals(7, engine.pickOneRandomWord(7).length(), "length=7");
        assertEquals(8, engine.pickOneRandomWord(8).length(), "length=8");
        assertNull(engine.pickOneRandomWord(99), "length=99");
    }

    @Test
    void givenEmptyInvalidWord_thenExpectNotExists() {
        assertFalse(engine.exists(null), "word=null");
        assertFalse(engine.exists(""), "word=<EMPTY>");
        assertFalse(engine.exists(" "), "word=<BLANK>");
        assertFalse(engine.exists("not-valid"), "word=not-valid");
        assertFalse(engine.exists("fourtitude"), "word=fourtitude");
    }

    @Test
    void givenValidWord_thenExpectExists() {
        assertTrue(engine.exists("Panda"), "word=Panda");
        assertTrue(engine.exists("kangaroo"), "word=kangaroo");
        assertTrue(engine.exists("MaNGoS"), "word=MaNGoS");
    }

    @Test
    void givenInvalidPrefix_thenEmptyList() {
        assertEquals(0, engine.wordsMatchingPrefix(null).size(), "prefix=null");
        assertEquals(0, engine.wordsMatchingPrefix("").size(), "prefix=<EMPTY>");
        assertEquals(0, engine.wordsMatchingPrefix(" ").size(), "prefix=<BLANK>");
        assertEquals(0, engine.wordsMatchingPrefix("!").size(), "prefix=<PUNCT>");
    }

    @Test
    void givenValidPrefix_thenSomeWords() {
        assertEquals(5234, engine.wordsMatchingPrefix("p").size(), "prefix=p");
        assertEquals(714, engine.wordsMatchingPrefix("pe").size(), "prefix=pe");
        assertEquals(96, engine.wordsMatchingPrefix("pen").size(), "prefix=pen");
        assertEquals(7, engine.wordsMatchingPrefix("pend").size(), "prefix=pend");
        assertEquals(3, engine.wordsMatchingPrefix("pendu").size(), "prefix=pendu");
        assertEquals(3, engine.wordsMatchingPrefix("pendul").size(), "prefix=pendul");
        assertEquals(2, engine.wordsMatchingPrefix("pendulu").size(), "prefix=pendulu");
        assertEquals(2, engine.wordsMatchingPrefix("pendulum").size(), "prefix=pendulum");
        assertEquals(1, engine.wordsMatchingPrefix("pendulums").size(), "prefix=pendulums");
        assertEquals(0, engine.wordsMatchingPrefix("pendulumss").size(), "prefix=pendulumss");
    }

    @Test
    void givenValidPrefixCaseInsensitive_thenSomeWords() {
        assertEquals(engine.wordsMatchingPrefix("PeN").size(), engine.wordsMatchingPrefix("pen").size(), "prefix=CASE_INSENSITIVE");
    }

    @Test
    void givenAllEmptyAndOrInvalidInputs_thenEmptyList() {
        assertEquals(0, engine.searchWords(null, null, null).size(), "start=null;end=null;length=null");
        assertEquals(0, engine.searchWords(' ', '$', 0).size(), "start=<SPACE>;end=<PUNCT>;length=0");
        assertEquals(0, engine.searchWords('\t', '7', -123).size(), "start=<CONTROL>;end=<NUMBER>;length=<NEGATIVE>");
    }

    @Test
    void givenValidStartCharOnly_thenSomeWords() {
        // uppercase, lowercase
        assertEquals(3478, engine.searchWords('a', null, null).size(), "start=a;end=null;length=null");
        assertEquals(3911, engine.searchWords('B', null, null).size(), "start=B;end=null;length=null");
    }

    @Test
    void givenValidEndCharOnly_thenSomeWords() {
        // uppercase, lowercase
        assertEquals(677, engine.searchWords(null, 'c', null).size(), "start=null;end=c;length=null");
        assertEquals(7654, engine.searchWords(null, 'D', null).size(), "start=null;end=D;length=null");
    }

    @Test
    void givenValidLengthOnly_thenSomeWords() {
        // length less than MAX
        assertEquals(4, engine.searchWords(null, null, 1).size(), "start=null;end=null;length=1");
        assertEquals(61, engine.searchWords(null, null, 2).size(), "start=null;end=null;length=2");
        assertEquals(649, engine.searchWords(null, null, 3).size(), "start=null;end=null;length=3");
        assertEquals(2446, engine.searchWords(null, null, 4).size(), "start=null;end=null;length=4");
        assertEquals(4680, engine.searchWords(null, null, 5).size(), "start=null;end=null;length=5");
        assertEquals(7352, engine.searchWords(null, null, 6).size(), "start=null;end=null;length=6");
        assertEquals(9878, engine.searchWords(null, null, 7).size(), "start=null;end=null;length=7");
        assertEquals(10466, engine.searchWords(null, null, 8).size(), "start=null;end=null;length=8");
        assertEquals(9412, engine.searchWords(null, null, 9).size(), "start=null;end=null;length=9");
        assertEquals(7569, engine.searchWords(null, null, 10).size(), "start=null;end=null;length=10");
        assertEquals(5250, engine.searchWords(null, null, 11).size(), "start=null;end=null;length=11");
        assertEquals(3383, engine.searchWords(null, null, 12).size(), "start=null;end=null;length=12");
        assertEquals(1885, engine.searchWords(null, null, 13).size(), "start=null;end=null;length=13");
        assertEquals(891, engine.searchWords(null, null, 14).size(), "start=null;end=null;length=14");
        assertEquals(434, engine.searchWords(null, null, 15).size(), "start=null;end=null;length=15");
        assertEquals(176, engine.searchWords(null, null, 16).size(), "start=null;end=null;length=16");
        assertEquals(84, engine.searchWords(null, null, 17).size(), "start=null;end=null;length=17");
        assertEquals(21, engine.searchWords(null, null, 18).size(), "start=null;end=null;length=18");
        assertEquals(10, engine.searchWords(null, null, 19).size(), "start=null;end=null;length=19");
        assertEquals(7, engine.searchWords(null, null, 20).size(), "start=null;end=null;length=20");
        assertEquals(2, engine.searchWords(null, null, 21).size(), "start=null;end=null;length=21");
        assertEquals(2, engine.searchWords(null, null, 22).size(), "start=null;end=null;length=22");
    }

    @Test
    void givenValidStartCharAndValidEndCharAndValidLength_thenSomeWords() {
        assertEquals(307, engine.searchWords('F', 'G', null).size(), "start=F;end=G;length=null");
        assertEquals(377, engine.searchWords('h', null, 7).size(), "start=h;end=null;length=7");
        assertEquals(30, engine.searchWords(null, 'i', 8).size(), "start=null;end=i;length=8");
        assertEquals(17, engine.searchWords('M', 'N', 9).size(), "start=M;end=N;length=9");

        Collection<String> words = engine.searchWords('f', 'r', 6);
        assertTrue(words.contains("flower"), "start=f;end=r;length=6;word=flower");
    }

    @Test
    void givenValidStartCharAndValidEndCharAndValidLength_thenEmptyList() {
        // length too large
        // wrong combo of startChar and endChar
        assertEquals(0, engine.searchWords('m', 'n', 19).size(), "start=m;end=n;length=19");
        assertEquals(0, engine.searchWords('K', 'q', null).size(), "start=K;end=q;length=null");
    }

    @Test
    void whenInvalidWord_thenEmptyList() {
        assertEquals(0, engine.generateSubWords(null, null).size(), "word=null;len=null");
        assertEquals(0, engine.generateSubWords("", null).size(), "word=<EMPTY>;len=null");
        assertEquals(0, engine.generateSubWords(" ", null).size(), "word=<BLANK>;len=null");
        assertEquals(0, engine.generateSubWords("@", null).size(), "word=<PUNCT>;len=null");
        assertEquals(0, engine.generateSubWords("fusion", 6).size(), "word=fusion;len=6");
        assertEquals(0, engine.generateSubWords("fusion", 5).size(), "word=fusion;len=5");
    }

    @Test
    void whenValidWord_thenSomeWord() {
        assertEquals(16, engine.generateSubWords("fusion", null).size(), "word=fusion;len=null");
        assertEquals(0, engine.generateSubWords("fusion", 0).size(), "word=fusion;len=0");
        assertEquals(27, engine.generateSubWords("fusion", 1).size(), "word=fusion;len=1");
        assertEquals(25, engine.generateSubWords("fusion", 2).size(), "word=fusion;len=2");
        assertEquals(16, engine.generateSubWords("fusion", 3).size(), "word=fusion;len=3");
        assertEquals(6, engine.generateSubWords("fusion", 4).size(), "word=fusion;len=4");
        assertEquals(0, engine.generateSubWords("fusion", 5).size(), "word=fusion;len=5");
    }

    @Test
    void givenValidInput_whenCreateGameState_thenExpectSuccess() {
        assertNotNull(engine.createGameState(3, null), "length=3;minLength=null");
        assertNotNull(engine.createGameState(4, null), "length=4;minLength=null");
        assertNotNull(engine.createGameState(4, 3), "length=4;minLength=3");
        assertNotNull(engine.createGameState(5, 3), "length=5;minLength=3");
        assertNotNull(engine.createGameState(6, 5), "length=6;minLength=5");
        assertNotNull(engine.createGameState(6, 6), "length=6;minLength=6");
    }

    @Test
    void givenInvalidInput_whenCreateGameState_thenExpectException() {
        assertThrows(NullPointerException.class, () -> { engine.createGameState(null, null); }, "length=null;minLength=null");
        assertThrows(IllegalArgumentException.class, () -> { engine.createGameState(1, null); }, "length=1;minLength=null");
        assertThrows(IllegalArgumentException.class, () -> { engine.createGameState(2, null); }, "length=2;minLength=null");
        assertThrows(IllegalArgumentException.class, () -> { engine.createGameState(4, 5); }, "length=4;minLength=5");
    }

}
