package asia.fourtitude.interviewq.jumble.console;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class AConsole {

    protected final Scanner cin;

    protected final PrintStream cout;

    protected AConsole(Scanner cin, PrintStream cout) {
        this.cin = cin;
        this.cout = cout;
    }

    protected String askInput(String prompt) {
        if (prompt == null) {
            prompt = "Option: ";
        }
        cout.print(prompt);
        try {
            return cin.nextLine();
        } catch (Exception ignore) {
            return "";
        }
    }

}
