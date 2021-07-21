package clientserver;

import entities.Priority;
import entities.Task;
import entities.User;
import logic.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class Menu {
    private PrintStream messageToClient;
    private BufferedReader userInputReader;
    private boolean newAccount = false;

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
        Priority actualPriority = null;
        messageToClient.println("Set task description:");
        messageToClient.println(">>");
        description = userInputReader.readLine();
        while (true) {
            messageToClient.println("Set task priority (low/medium/high):");
            messageToClient.println(">>");
            priority = userInputReader.readLine();
            try {
                actualPriority = Priority.valueOf(priority.toLowerCase());
            }catch (IllegalArgumentException i){
                messageToClient.println("[-] Invalid priority!");
            }
            if (actualPriority != null) break;
        }
        return new BasicTask(description, actualPriority);
    }

    public String taskIDPrompt() throws IOException {
        messageToClient.println("Enter task ID:");
        messageToClient.println(">>");
        return userInputReader.readLine();
    }

    public Task editTaskPrompt(Task task) throws IOException {
        while (true) {
            messageToClient.println("Edit:\n" +
                    "{1} Description\n" +
                    "{2} Priority\n" +
                    "{3} Save");
            messageToClient.println(">>");
            String input = userInputReader.readLine();
            switch (input) {
                case "1":
                    messageToClient.println("Enter new description:");
                    messageToClient.println(">>");
                    input = userInputReader.readLine();
                    task.setDescription(input);
                    break;
                case "2":
                    messageToClient.println("Enter new priority:");
                    messageToClient.println(">>");
                    input = userInputReader.readLine();
                    try {
                        task.setPriority(Priority.valueOf(input.toLowerCase()));
                    }catch (IllegalArgumentException i){
                        messageToClient.println("[-] Invalid priority!");
                    }
                    break;
                case "3":
                    return task;
                default:
                    messageToClient.println("[-] Invalid option!");
            }
        }


    }

    public User loginPrompt() throws IOException {
        while (true) {
            messageToClient.println("Welcome! Login or register to use the app!\n" +
                    "{1} Login\n" +
                    "{2} Register");
            messageToClient.println(">>");
            String input = userInputReader.readLine();
            String username = null;
            String password = null;
            String email = null;
            switch (input) {
                case "1":
                    username = getInputWithPrompt("Username:");
                    password = getInputWithPrompt("Password:");
                    break;
                case "2":
                    newAccount = true;
                    username = getInputWithPrompt("Username:");
                    password = getInputWithPrompt("Password:");
                    email = getInputWithPrompt("Email:");
                default:
                    messageToClient.println("[-] Invalid option!");
            }
            return new User.UserBuilder(username,password).withEmail(email).build();
        }

    }

    public boolean isNewAccount(){
        return newAccount;
    }

    private String getInputWithPrompt(String prompt) throws IOException {
        messageToClient.println(prompt);
        messageToClient.println(">>");
        return userInputReader.readLine();
    }


}
