package clientserver;

import database.TaskDAO;
import database.UserDAO;
import entities.Task;
import entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ClientHandler extends Thread {
    private final Socket socket;
    private User currentUser = null;
    private boolean isRunning = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String input;

        try {
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream messageToClient = new PrintStream(socket.getOutputStream(),true);
            Menu menu = new Menu(messageToClient, userInputReader);
            while (isRunning) {
                UserDAO userDAO = new UserDAO();
                try {
                    if (currentUser == null) {
                        while (true) {
                            currentUser = menu.loginPrompt();
                            if (menu.isNewAccount()) {
                                userDAO.add(currentUser);
                            }
                            userDAO.initialize(currentUser.getUsername(), currentUser.getPassword());
                            try {
                                currentUser = userDAO.getUser();
                                break;
                            } catch (SQLException e) {
                                messageToClient.println(e.getMessage());
                            }
                        }
                    }
                    TaskDAO taskDAO = new TaskDAO(currentUser);

                    HashMap<Integer, Task> allTasks = new HashMap<>();

                    menu.splitAndPrintTasks(taskDAO, allTasks);
                    menu.printMainMenu();
                    input = userInputReader.readLine();

                    switch (input) {
                        case "1":
                            Task task = menu.addTaskPrompt();
                            taskDAO.add(task);
                            messageToClient.println("[+] Task added successfully!");
                            break;
                        case "2":
                            try {
                                int displayID = Integer.parseInt(menu.taskIDPrompt());
                                int taskID = allTasks.get(displayID).getId();
                                taskDAO.remove(taskID);
                                messageToClient.println("[+] Task removed successfully!");
                            } catch (NullPointerException e) {
                                messageToClient.println("[-] Invalid task ID!");
                            } catch (NumberFormatException e) {
                                messageToClient.println("[-] Enter a number!");
                            }
                            break;
                        case "3":
                            try {
                                int displayID = Integer.parseInt(menu.taskIDPrompt());
                                int taskID = allTasks.get(displayID).getId();
                                taskDAO.markTaskAsCompleted(taskID, LocalDateTime.now());
                                messageToClient.println("[+] Task marked as completed!");
                            } catch (IndexOutOfBoundsException e) {
                                messageToClient.println("[-] Invalid task ID!");
                            } catch (NumberFormatException e) {
                                messageToClient.println("[-] Enter a number!");
                            }
                            break;
                        case "4":
                            try {
                                int displayID = Integer.parseInt(menu.taskIDPrompt());
                                Task taskToEdit = allTasks.get(displayID);
                                Task editedTask = menu.editTaskPrompt(taskToEdit);
                                messageToClient.println("editedTask id is: " + editedTask.getId());
                                taskDAO.edit(editedTask.getId(),editedTask);
                            } catch (IndexOutOfBoundsException e) {
                                messageToClient.println("[-] Invalid task ID!");
                            } catch (NumberFormatException e) {
                                messageToClient.println("[-] Enter a number!");
                            }
                            break;
                        case "exit":
                            isRunning = false;
                            messageToClient.println("{close}");
                            socket.close();
                            System.out.println("[+] Client disconnected > " + socket);
                            break;
                        default:
                            messageToClient.println("[-] Invalid option!");
                    }
                } catch (Exception e) {
                    messageToClient.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[-] Error");
            e.printStackTrace();
        }
    }
}
