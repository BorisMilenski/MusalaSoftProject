package clientserver;

import entities.Priority;
import entities.Task;
import entities.User;
import logic.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
                    switch (input) {
                        case "low":
                            task.setPriority(Priority.low);
                            break;
                        case "medium":
                            task.setPriority(Priority.medium);
                            break;
                        case "high":
                            task.setPriority(Priority.high);
                            break;
                        default:
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
            switch (input) {
                case "1":
                    return login();
                case "2":
                    return register();
                default:
                    messageToClient.println("[-] Invalid option!");
            }
        }
        //ArrayList<String> userpass = new ArrayList<>();

    }

    private User login() throws IOException {
        String username;
        String password;
        //ArrayList<String> userpass = new ArrayList<>();
        messageToClient.println("Username:");
        messageToClient.println(">>");
        username = userInputReader.readLine();
        messageToClient.println("Password:");
        messageToClient.println(">>");
        password = userInputReader.readLine();
        //userpass.add(username);
        //userpass.add(password);
        return new User(username,password);
    }

    private User register() throws IOException {
        String username;
        String password;
        String email;
        messageToClient.println("Username:");
        messageToClient.println(">>");
        username = userInputReader.readLine();
        messageToClient.println("Password:");
        messageToClient.println(">>");
        password = userInputReader.readLine();
        messageToClient.println("Email:");
        messageToClient.println(">>");
        email = userInputReader.readLine();
        return new User(username, password, email);
    }

}
