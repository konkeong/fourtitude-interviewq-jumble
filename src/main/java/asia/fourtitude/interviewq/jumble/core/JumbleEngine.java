package asia.fourtitude.interviewq.jumble.core;

import java.io.*;
import java.util.*;

import asia.fourtitude.interviewq.jumble.service.DictionaryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JumbleEngine {

    private DictionaryService dictionaryService;

    public JumbleEngine() throws IOException {
        this.dictionaryService = new DictionaryService();
    }

    /**
     * From the input `word`, produces/generates a copy which has the same
     * letters, but in different ordering.
     *
     * Example: from "elephant" to "aeehlnpt".
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#scramble()
     * b) scrambled letters/output must not be the same as input
     *
     * @param word The input word to scramble the letters.
     * @return The scrambled output/letters.
     */
    public String scramble(String word) {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */
        List<Character> characters = new ArrayList<>();

        for (char c : word.toCharArray()) {
            characters.add(c);
        }

        Collections.shuffle(characters);

        String result = "";
        for (Character c : characters) {
            result += c;
        }

        return result;
    }

    /**
     * Retrieves the palindrome words from the internal
     * word list/dictionary ("src/main/resources/words.txt").
     *
     * Word of single letter is not considered as valid palindrome word.
     *
     * Examples: "eye", "deed", "level".
     *
     * Evaluation/Grading:
     * a) able to access/use resource from classpath
     * b) using inbuilt Collections
     * c) using "try-with-resources" functionality/statement
     * d) pass unit test: JumbleEngineTest#palindrome()
     *
     * @return The list of palindrome words found in system/engine.
     * @throws IOException
     * @see https://www.google.com/search?q=palindrome+meaning
     */
    public Collection<String> retrievePalindromeWords() throws IOException {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */

        List<String> palindromeWords = new ArrayList<>();
        for (String s : dictionaryService.getWordList()) {
            if (s.length() == 1)
                continue;
            if (isPalindrome(s)) {
                palindromeWords.add(s);
            }
        }

        return palindromeWords;
    }

    static boolean isPalindrome(String word) {
        List<Character> charList = new ArrayList<>();

        for (char w : word.toCharArray()) {
            charList.add(w);
        }

        Collections.reverse(charList);

        String temp = "";

        for (Character c : charList) {
            temp += c;
        }

        return word.equals(temp);
    }

    /**
     * Picks one word randomly from internal word list.
     *
     * Evaluation/Grading:
     * a) pass unit test: JumbleEngineTest#randomWord()
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param length The word picked, must of length.
     * @return One of the word (randomly) from word list.
     *         Or null if none matching.
     * @throws IOException
     */
    public String pickOneRandomWord(Integer length) throws IOException {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */

        List<String> matchingWords = new ArrayList<>();

        if (length == null) {
            return getRandomWordFromDictionary(dictionaryService.getWordList());
        }

        for (Map.Entry<String, Integer> map : dictionaryService.getWordsMap().entrySet()) {
            if (map.getValue() == length) {
                matchingWords.add(map.getKey());
            }
        }

        if (matchingWords.size() == 0) {
            return null;
        }

        Random random = new Random();

        int randomIndex = random.nextInt(matchingWords.size());

        return matchingWords.get(randomIndex);

    }

    static String getRandomWordFromDictionary(List<String> list) {
        Random random = new Random();

        int randomIndex = random.nextInt(list.size());

        return list.get(randomIndex);
    }

    /**
     * Checks if the `word` exists in internal word list.
     * Matching is case insensitive.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word The input word to check.
     * @return true if `word` exists in internal word list.
     * @throws IOException
     */
    public boolean exists(String word) throws IOException {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */

        if (word == null)
            return false;
        if (word.isEmpty())
            return false;

        return dictionaryService.getWordSets().contains(word.toLowerCase().trim());
    }

    /**
     * Finds all the words from internal word list which begins with the
     * input `prefix`.
     * Matching is case insensitive.
     *
     * Invalid `prefix` (null, empty string, blank string, non letter) will
     * return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param prefix The prefix to match.
     * @return The list of words matching the prefix.
     * @throws IOException
     */
    public Collection<String> wordsMatchingPrefix(String prefix) throws IOException {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */

        if (prefix == null || prefix.isEmpty() || prefix.equals(" "))
            return new ArrayList<String>();
        if (dictionaryService.getInvalidCharacters().contains(prefix.charAt(0)))
            return new ArrayList<String>();

        List<String> matchingWords = new ArrayList<>();
        for (String w : dictionaryService.getWordList()) {
            if (w.trim().toLowerCase().startsWith(prefix.toLowerCase().trim())) {
                matchingWords.add(w.trim());
            }
        }

        return matchingWords;
    }

    /**
     * Finds all the words from internal word list that is matching
     * the searching criteria.
     *
     * `startChar` and `endChar` must be 'a' to 'z' only. And case insensitive.
     * `length`, if have value, must be positive integer (>= 1).
     *
     * Words are filtered using `startChar` and `endChar` first.
     * Then apply `length` on the result, to produce the final output.
     *
     * Must have at least one valid value out of 3 inputs
     * (`startChar`, `endChar`, `length`) to proceed with searching.
     * Otherwise, return empty list.
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param startChar The first character of the word to search for.
     * @param endChar   The last character of the word to match with.
     * @param length    The length of the word to match.
     * @return The list of words matching the searching criteria.
     * @throws IOException
     */
    public Collection<String> searchWords(Character startChar, Character endChar, Integer length) throws IOException {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */
        List<String> matchingWords = new ArrayList<>();

        if (dictionaryService.getInvalidCharacters().contains(startChar)
                || dictionaryService.getInvalidCharacters().contains(startChar))
            return new ArrayList<>();
        if (startChar == null && endChar == null && length == null)
            return new ArrayList<>();

        if (startChar != null)
            startChar = Character.toLowerCase(startChar);
        if (endChar != null)
            endChar = Character.toLowerCase(endChar);

        for (String w : dictionaryService.getWordList()) {
            // all param given
            if (startChar != null && endChar != null && length != null) {
                if (startChar == w.charAt(0) && endChar == w.charAt(w.length() - 1) && length == w.length()) {
                    matchingWords.add(w);
                }
            } else if (startChar != null && (endChar == null || endChar == ' ') && length == null) {
                // only start char given
                if (startChar == w.charAt(0)) {
                    matchingWords.add(w);
                }
            } else if (endChar != null && (startChar == null || startChar == ' ') && length == null) {
                // only end char given
                if (endChar == w.charAt(w.length() - 1)) {
                    matchingWords.add(w);
                }
            } else if (startChar == null && endChar == null && length != null) {
                // only length is given
                if (w.length() == length) {
                    matchingWords.add(w);
                }
            } else if (startChar != null && endChar != null && length == null) {
                // start char and end char is given
                if (startChar == w.charAt(0) && endChar == w.charAt(w.length() - 1)) {
                    matchingWords.add(w);
                }
            } else if (startChar == null && endChar != null && length != null) {
                // only start char is null
                if (endChar == w.charAt(w.length() - 1) && length == w.length()) {
                    matchingWords.add(w);
                }
            } else if (startChar != null && endChar == null && length != null) {
                // only end char is null
                if (startChar == w.charAt(0) && length == w.length()) {
                    matchingWords.add(w);
                }
            }
        }

        return matchingWords;
    }

    /**
     * Generates all possible combinations of smaller/sub words using the
     * letters from input word.
     *
     * The `minLength` set the minimum length of sub word that is considered
     * as acceptable word.
     *
     * If length of input `word` is less than `minLength`, then return empty list.
     *
     * Example: From "yellow" and `minLength` = 3, the output sub words:
     * low, lowly, lye, ole, owe, owl, well, welly, woe, yell, yeow, yew, yowl
     *
     * Evaluation/Grading:
     * a) pass related unit tests in "JumbleEngineTest"
     * b) provide a good enough implementation, if not able to provide a fast lookup
     * c) bonus points, if able to implement a fast lookup/scheme
     *
     * @param word      The input word to use as base/seed.
     * @param minLength The minimum length (inclusive) of sub words.
     *                  Expects positive integer.
     *                  Default is 3.
     * @return The list of sub words constructed from input `word`.
     * @throws IOException
     */
    public Collection<String> generateSubWords(String word, Integer minLength) throws IOException {
        /*
         * Refer to the method's Javadoc (above) and implement accordingly.
         * Must pass the corresponding unit tests.
         */
        Set<String> subWords = new HashSet<>();

        if (word == null || (minLength!= null && minLength == 0)) {
            return subWords;
        }

        generateSubWordsHelper(word.trim(), minLength, "", subWords);

        if (subWords.size() > 0) {
            subWords.removeAll(Collections.singleton(word.trim()));
        }

        return subWords;

    }

    static void printer(List<String> words) {
        for (String s : words) {
            System.out.println(s);
        }
    }

    private void generateSubWordsHelper(String remaining, Integer minLength, String current, Set<String> result) {
        if (minLength != null) {
            if (current.length() != 0 && current.length() >= minLength && dictionaryService.isValidWord(current)
                    && !current.equalsIgnoreCase(remaining)) {
                result.add(current);
            }
        } else {
            if (current.length() != 0 && current.length() >= 3 && dictionaryService.isValidWord(current)
                    && !current.equalsIgnoreCase(remaining)) {
                result.add(current);
            }
        }

        // the conditions

        for (int i = 0; i < remaining.length(); i++) {
            char ch = remaining.charAt(i);
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            generateSubWordsHelper(newRemaining, minLength, current + ch, result);
        }
    }

    /**
     * Creates a game state with word to guess, scrambled letters, and
     * possible combinations of words.
     *
     * Word is of length 6 characters.
     * The minimum length of sub words is of length 3 characters.
     *
     * @param length    The length of selected word.
     *                  Expects >= 3.
     * @param minLength The minimum length (inclusive) of sub words.
     *                  Expects positive integer.
     *                  Default is 3.
     * @return The game state.
     * @throws IOException
     */
    public GameState createGameState(Integer length, Integer minLength) throws IOException {
        Objects.requireNonNull(length, "length must not be null");
        if (minLength == null) {
            minLength = 3;
        } else if (minLength <= 0) {
            throw new IllegalArgumentException("Invalid minLength=[" + minLength + "], expect positive integer");
        }
        if (length < 3) {
            throw new IllegalArgumentException("Invalid length=[" + length + "], expect greater than or equals 3");
        }
        if (minLength > length) {
            throw new IllegalArgumentException(
                    "Expect minLength=[" + minLength + "] greater than length=[" + length + "]");
        }
        String original = this.pickOneRandomWord(length);
        if (original == null) {
            throw new IllegalArgumentException("Cannot find valid word to create game state");
        }
        String scramble = this.scramble(original);
        Map<String, Boolean> subWords = new TreeMap<>();
        for (String subWord : this.generateSubWords(original, minLength)) {
            subWords.put(subWord, Boolean.FALSE);
        }
        return new GameState(original, scramble, subWords);
    }

}
