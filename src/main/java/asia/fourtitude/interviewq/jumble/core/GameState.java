package asia.fourtitude.interviewq.jumble.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public /*record*/ class GameState {

    private final String original;

    private String scramble;

    private final Map<String, Boolean> subWords;

    public GameState(String original, String scramble, Map<String, Boolean> subWords) {
        this.original = original;
        this.scramble = scramble;
        this.subWords = subWords;
    }

    public String getOriginal() {
        return original;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public Map<String, Boolean> getSubWords() {
        return subWords;
    }

    public String getScrambleAsDisplay() {
        List<String> list = new ArrayList<>();
        for (char ch : this.scramble.toCharArray()) {
            list.add(Character.toString(ch));
        }
        return String.join(" ", list);
    }

    public List<String> getGuessedWords() {
        Map<Integer, Set<String>> guesseds = new TreeMap<>();
        for (Map.Entry<String, Boolean> entry : this.subWords.entrySet()) {
            if (entry.getValue() == Boolean.TRUE) {
                String word = entry.getKey();
                Integer len = word.length();
                Set<String> words = guesseds.get(len);
                if (words == null) {
                    words = new TreeSet<>();
                    guesseds.put(len, words);
                }
                words.add(word);
            }
        }
        List<String> words = new ArrayList<>();
        for (Map.Entry<Integer, Set<String>> entry : guesseds.entrySet()) {
            for (String word : entry.getValue()) {
                words.add(word);
            }
        }
        return words;
    }

    public boolean updateGuessWord(String word) {
        if (word == null) {
            return false;
        }
        if (this.subWords.containsKey(word)) {
            this.subWords.put(word, Boolean.TRUE);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (original != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("original=[").append(original).append(']');
        }
        if (scramble != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("scramble=[").append(scramble).append(']');
        }
        if (subWords != null) {
            sb.append(sb.length() == 0 ? "" : ", ").append("subWords.size=[").append(subWords.size()).append(']');
        }
        return sb.toString();
    }

}
