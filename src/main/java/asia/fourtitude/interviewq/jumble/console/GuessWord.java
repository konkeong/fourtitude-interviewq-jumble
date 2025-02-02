package asia.fourtitude.interviewq.jumble.console;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import asia.fourtitude.interviewq.jumble.core.GameState;
import asia.fourtitude.interviewq.jumble.core.JumbleEngine;

public class GuessWord extends AConsole {

    /*
     * Develop a game with mechanics of guessing all possible words
     * (available in internal word list) from the given scrambled letters.
     *
     * At start of each round, display current state:
     * a) number of all possible words (as hint)
     * b) words that are guessed/matched
     * c) how many words left to guess
     * d) the scrambled letters
     *
     * Case insensitive matching.
     */

    private final JumbleEngine engine;

    public GuessWord(Scanner cin, PrintStream cout, JumbleEngine engine) {
        super(cin, cout);
        this.engine = engine;
    }

    public boolean playGame(GameState gameState) {
        boolean exit = false;
        boolean finish = false;
        do {
            List<String> guessedWords = gameState.getGuessedWords();
            int remaining = gameState.getSubWords().size() - guessedWords.size();
            cout.println();
            cout.println("Total possible : " + gameState.getSubWords().size());
            if (guessedWords.isEmpty()) {
                cout.println("No word guessed yet.");
            } else {
                cout.println("Words guessed  : " + guessedWords.size());
                int pos = 0;
                for (String word : guessedWords) {
                    pos += 1;
                    cout.printf("%3d. %s%n", pos, word);
                }
            }
            if (remaining > 0) {
                String scramble = this.engine.scramble(gameState.getOriginal());
                gameState.setScramble(scramble);

                cout.println("Remaining words: " + remaining);
                cout.println("Original       : " + gameState.getOriginal());
                cout.println("Letters        : " + gameState.getScrambleAsDisplay());

                String option = askInput("Enter word (min 3 letters)(q to quit): ");
                switch (option) {
                case "exit":
                    exit = true;
                    /* $FALL-THROUGH$ */
                case "q":
                    /* $FALL-THROUGH$ */
                case "quit":
                    finish = true;
                    break;
                default:
                    String word = option.trim();
                    cout.println();
                    if (gameState.updateGuessWord(word)) {
                        cout.printf("[%s] guessed correctly%n", word);
                    } else {
                        cout.printf("[%s] guessed incorrectly%n", word);
                    }
                    break;
                }
            } else {
                finish = true;
            }
        } while (! finish && ! exit);
        return exit;
    }

    public boolean exec() {
        boolean exit = false;
        boolean finish = false;
        do {
            cout.println();
            cout.println("Guess Words");
            cout.println("-----------");
            cout.println("   n. new game");
            cout.println("   h. help");
            cout.println("   q. quit");

            String option = askInput(null);
            switch (option) {
            case "n":
                GameState gameState = this.engine.createGameState(6, 3);
                exit = playGame(gameState);
                break;
            case "h":
                cout.println("Given a list of scrambled letters, pick and construct");
                cout.println("all possible words, using at least 3 letters (or more).");
                break;
            case "exit":
                exit = true;
                /* $FALL-THROUGH$ */
            case "q":
                /* $FALL-THROUGH$ */
            case "quit":
                finish = true;
                break;
            default:
                break;
            }
        } while (! finish && ! exit);
        return exit;
    }

}
