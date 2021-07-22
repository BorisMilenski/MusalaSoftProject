package clientserver;

import database.TaskDAO;
import entities.Priority;
import entities.Task;
import entities.User;
import entities.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TreeMap;

public class Menu {
    private PrintStream messageToClient;
    private BufferedReader userInputReader;
    private boolean newAccount = false;

    public Menu (PrintStream messageToClient, BufferedReader userInputReader) {
        this.messageToClient = messageToClient;
        this.userInputReader = userInputReader;
    }

    public boolean mainMenu(TaskDAO taskDAO, HashMap<Integer, Task> allTasks) throws IOException, SQLException {
       String input;
        try {
            input = getInputWithPrompt("Select an option:\n" +
                    "{1} Add task\n" +
                    "{2} Remove task\n" +
                    "{3} Mark task as completed\n" +
                    "{4} Edit task\n" +
                    "{exit} Quit");
        }catch (IOException e){
            System.out.println("[-] " + e.getMessage());
            return false;
        }

        switch (input) {
            case "1":
                Task task = addTaskPrompt();
                taskDAO.add(task);
                messageToClient.println("[+] Task added successfully!");
                return true;
            case "2":
                try {
                    int displayID = Integer.parseInt(taskIDPrompt());
                    int taskID = allTasks.get(displayID).getId();
                    taskDAO.remove(taskID);
                    messageToClient.println("[+] Task removed successfully!");
                } catch (NullPointerException e) {
                    messageToClient.println("[-] Invalid task ID!");
                } catch (NumberFormatException e) {
                    messageToClient.println("[-] Enter a number!");
                }
                return true;
            case "3":
                try {
                    int displayID = Integer.parseInt(taskIDPrompt());
                    int taskID = allTasks.get(displayID).getId();
                    taskDAO.markTaskAsCompleted(taskID, LocalDateTime.now());
                    messageToClient.println("[+] Task marked as completed!");
                } catch (IndexOutOfBoundsException e) {
                    messageToClient.println("[-] Invalid task ID!");
                } catch (NumberFormatException e) {
                    messageToClient.println("[-] Enter a number!");
                }
                return true;
            case "4":
                try {
                    int displayID = Integer.parseInt(taskIDPrompt());
                    Task taskToEdit = allTasks.get(displayID);
                    Task editedTask = editTaskPrompt(taskToEdit);
                    taskDAO.edit(editedTask.getId(),editedTask);
                } catch (IndexOutOfBoundsException e) {
                    messageToClient.println("[-] Invalid task ID!");
                } catch (NumberFormatException e) {
                    messageToClient.println("[-] Enter a number!");
                }
                return true;
            case "exit":
                messageToClient.println("{close}");
                return false;
            default:
                messageToClient.println("[-] Invalid option!");
                return true;
        }
    }

    private Task addTaskPrompt() throws IOException {
        String description;
        String priority;
        Priority actualPriority = null;
        description = getInputWithPrompt("Set task description:");
        while (actualPriority == null) {
            priority = getInputWithPrompt("Set task priority (low/medium/high):");
            try {
                actualPriority = Priority.valueOf(priority.toLowerCase());
            }catch (IllegalArgumentException i){
                messageToClient.println("[-] Invalid priority!");
            }
        }
        return new BasicTask(description, actualPriority);
    }

    private String taskIDPrompt() throws IOException {
        return getInputWithPrompt("Enter task ID:");
    }

    private Task editTaskPrompt(Task task) throws IOException {
        while (true) {
            String input = getInputWithPrompt("Edit:\n" +
                    "{1} Description\n" +
                    "{2} Priority\n" +
                    "{3} Save");
            switch (input) {
                case "1":
                    input = getInputWithPrompt("Enter new description:");
                    task.setDescription(input);
                    break;
                case "2":
                    input = getInputWithPrompt("Enter new priority:");
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
        String username = null;
        String password = null;
        String email = null;
        boolean isRunning = true;
        while (isRunning) {
            newAccount = false;
            String input = getInputWithPrompt("Welcome! Login or register to use the app!\n" +
                    "{1} Login\n" +
                    "{2} Register");

            switch (input) {
                case "1":
                    username = getInputWithPrompt("Username:");
                    password = getInputWithPrompt("Password:");
                    isRunning = false;
                    break;
                case "2":
                    newAccount = true;
                    username = getInputWithPrompt("Username:");
                    password = getInputWithPrompt("Password:");
                    email = getInputWithPrompt("Email:");
                    isRunning = false;
                    break;
                default:
                    messageToClient.println("[-] Invalid option!");
                    break;
            }
        }
        return new User.UserBuilder(username,password).withEmail(email).build();
    }

    public void splitAndPrintTasks(TaskDAO taskDAO, HashMap<Integer, Task> allTasks) throws SQLException {
        TreeMap<Integer,Task> completed = new TreeMap<>();
        TreeMap<Integer,Task> notCompleted = new TreeMap<>();

        int taskCounter = 1;
        for (Task task : taskDAO.get()) {
            if (task.isCompleted()) {
                completed.put(taskCounter, task);
            } else {
                notCompleted.put(taskCounter, task);
            }
            allTasks.put(taskCounter, task);
            taskCounter++;
        }
        messageToClient.println("[+] Completed tasks:");
        if (completed.isEmpty()) {
            messageToClient.println("None");
        } else {
            completed.forEach((index, task)-> messageToClient.println(index + ". " + task));
        }
        messageToClient.println("[*] Remaining tasks:");
        if (notCompleted.isEmpty()) {
            messageToClient.println("None");
        } else {
            notCompleted.forEach((index, task)-> messageToClient.println(index + ". " + task));
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
