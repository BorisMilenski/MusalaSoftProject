package clientserver;

import java.io.PrintStream;

public class Menu {
    private PrintStream messageToClient;

    public Menu (PrintStream messageToClient) {
        this.messageToClient = messageToClient;
    }

    public void printMainMenu() {
        messageToClient.println("Select an option:\n" +
                "{1} Add task\n" +
                "{2} Remove task\n" +
                "{3} Mark task as completed\n" +
                "{4} Edit task\n" +
                "{exit} Quit");
        messageToClient.println(">>");
    }
}
