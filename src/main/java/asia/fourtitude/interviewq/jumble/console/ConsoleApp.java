package asia.fourtitude.interviewq.jumble.console;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

import asia.fourtitude.interviewq.jumble.core.JumbleEngine;

public class ConsoleApp extends AConsole {

    private final JumbleEngine engine;

    public ConsoleApp(Scanner cin, PrintStream cout) {
        super(cin, cout);
        this.engine = new JumbleEngine();
    }

    private void scramble() {
        cout.println();
        cout.println("Scramble Word");
        cout.println("-------------");
        cout.print("Enter word: ");
        String word = cin.nextLine().trim();

        if (! word.isEmpty()) {
            String drow = engine.scramble(word);
            cout.print("Scrambled : ");
            cout.println(drow);
            if (word.equals(drow)) {
                throw new RuntimeException("Scrambled word is same as input=[" + word + "]");
            }
        }
    }

    private void palindrome() {
        cout.println();
        cout.println("Palindrome Words");
        cout.println("----------------");

        Collection<String> words = engine.retrievePalindromeWords();
        if (words.isEmpty()) {
            cout.println("No palindrome words can be found");
        } else {
            cout.println("There are " + words.size() + " palindrome words");
            int pos = 0;
            for (String word : words) {
                pos += 1;
                cout.printf("%3d. %s%n", pos, word);
            }
        }
    }

    private void wordExists() {
        cout.println();
        cout.println("Word Exists");
        cout.println("-----------");
        cout.print("Enter word: ");
        String word = cin.nextLine().trim();

        if (engine.exists(word)) {
            cout.printf("[%s] exists%n", word);
        } else {
            cout.printf("[%s] not exists%n", word);
        }
    }

    private void wordsMatchingPrefix() {
        cout.println();
        cout.println("Word Matching Prefix");
        cout.println("--------------------");
        cout.print("Enter prefix: ");
        String prefix = cin.nextLine().trim();

        Collection<String> words = engine.wordsMatchingPrefix(prefix);
        if (words.isEmpty()) {
            cout.printf("No words matching prefix=[%s] can be found%n", prefix);
        } else {
            if (words.size() == 1) {
                cout.printf("There is only 1 word matching prefix=[%s]%n", prefix);
            } else {
                cout.printf("There are %d words matching prefix=[%s]%n", words.size(), prefix);
            }
            int pos = 0;
            for (String word : words) {
                pos += 1;
                cout.printf("%3d. %s%n", pos, word);
            }
        }
    }

    private void searchWords() {
        Character startChar = null;
        Character endChar = null;
        Integer length = null;
        StringBuilder sbCond = new StringBuilder();

        cout.println();
        cout.println("Searching for Words");
        cout.println("-------------------");
        cout.print("Starting character (ENTER to ignore): ");
        String input = cin.nextLine().trim();
        if (! input.isEmpty()) {
            startChar = input.charAt(0);
            sbCond.append(" startChar=[").append(startChar).append(']');
        }
        cout.print("Ending character (ENTER to ignore): ");
        input = cin.nextLine().trim();
        if (! input.isEmpty()) {
            endChar = input.charAt(0);
            sbCond.append(" endChar=[").append(endChar).append(']');
        }
        cout.print("Word Length (ENTER to ignore): ");
        input = cin.nextLine().trim();
        if (! input.isEmpty()) {
            try {
                length = Integer.parseInt(input);
                sbCond.append(" length=[").append(length).append(']');
            } catch (Exception ignore) {
                // ignore
            }
        }

        String conditions = sbCond.toString().trim();
        Collection<String> words = engine.searchWords(startChar, endChar, length);
        if (words.isEmpty()) {
            cout.printf("No words found: %s%n", conditions);
        } else {
            if (words.size() == 1) {
                cout.printf("There is only 1 word found: %s%n", conditions);
            } else {
                cout.printf("There are %d words found: %s%n", words.size(), conditions);
            }
            int pos = 0;
            for (String word : words) {
                pos += 1;
                cout.printf("%3d. %s%n", pos, word);
            }
        }
    }

    private void generateSubWords() {
        cout.println();
        cout.println("Generate Sub Words");
        cout.println("------------------");
        cout.print("Enter base word: ");
        String baseWord = cin.nextLine().trim();
        cout.print("Minimum Length (ENTER to ignore): ");
        Integer minLength = null;
        String input = cin.nextLine().trim();
        if (! input.isEmpty()) {
            try {
                minLength = Integer.parseInt(input);
            } catch (Exception ignore) {
                // ignore
            }
        }
        String conditions = "baseWord=[" + baseWord + "]";
        if (minLength != null) {
            conditions = conditions + " minLength=[" + minLength + "]";
        }
        long tStart = System.currentTimeMillis();
        Collection<String> words = engine.generateSubWords(baseWord, minLength);
        long tStop = System.currentTimeMillis();
        if (words.isEmpty()) {
            cout.printf("No words found: %s%n", conditions);
        } else {
            if (words.size() == 1) {
                cout.printf("There is only 1 word found: %s%n", conditions);
            } else {
                cout.printf("There are %d words found: %s%n", words.size(), conditions);
            }
            int pos = 0;
            for (String word : words) {
                pos += 1;
                cout.printf("%3d. %s%n", pos, word);
            }
        }
        cout.println((tStop - tStart) + " msec");
    }

    public void run() {
        boolean exit = false;
        boolean finish = false;
        do {
            cout.println("Jumble Main Menu");
            cout.println("----------------");
            cout.println("   1. scramble word");
            cout.println("   2. palindrome words");
            cout.println("   3. word exists");
            cout.println("   4. words matching prefix");
            cout.println("   5. search words");
            cout.println("   6. generate sub words");
            cout.println("  11. play game");
            cout.println("   q. quit");

            String option = askInput(null);
            switch (option) {
            case "1":
                scramble();
                break;
            case "2":
                palindrome();
                break;
            case "3":
                wordExists();
                break;
            case "4":
                wordsMatchingPrefix();
                break;
            case "5":
                searchWords();
                break;
            case "6":
                generateSubWords();
                break;
            case "11":
                exit = new GuessWord(cin, cout, engine).exec();
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

            if (! finish && ! exit) {
                cout.println();
            }
        } while (! finish && ! exit);
    }

    public static void main(String[] args) {
        new ConsoleApp(new Scanner(System.in), new PrintStream(System.out)).run();
    }

}
