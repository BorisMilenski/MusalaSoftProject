package clientserver;

import entities.Priority;
import entities.Task;
import logic.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class Menu {
    private PrintStream messageToClient;
    private BufferedReader userInputReader;

    public Menu (PrintStream messageToClient, BufferedReader userInputReader) {
        this.messageToClient = messageToClient;
        this.userInputReader = userInputReader;
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

    public Task addTaskPrompt() throws IOException {
        String description;
        String priority;
        Priority priorityEnum = null;
        boolean priorityIsSet = false;
        messageToClient.println("Set task description:");
        messageToClient.println(">>");
        description = userInputReader.readLine();
        while (true) {
            messageToClient.println("Set task priority (low/medium/high):");
            messageToClient.println(">>");
            priority = userInputReader.readLine();
            switch (priority) {
                case "low":
                    priorityEnum = Priority.low;
                    priorityIsSet = true;
                    break;
                case "medium":
                    priorityEnum = Priority.medium;
                    priorityIsSet = true;
                    break;
                case "high":
                    priorityEnum = Priority.high;
                    priorityIsSet = true;
                    break;
                default:
                    messageToClient.println("[-] Invalid priority value!");
                    break;
            }
            if (priorityIsSet) break;
        }
        return new BasicTask(description,priorityEnum);
    }
}
