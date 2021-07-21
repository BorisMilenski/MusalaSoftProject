package clientserver;

import database.TaskDAO;
import database.UserDAO;
import entities.Task;
import entities.User;
import org.w3c.dom.UserDataHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ClientHandler extends Thread {
    final Socket socket;
    User currentUser = null;

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
            while (true) {
                UserDAO userDAO = null;
                try {
                    if (currentUser == null) {
                        User someUser = menu.loginPrompt();
                        if (someUser.getId()==-1) {
                            userDAO = new UserDAO(someUser.getUsername(), someUser.getPassword());
                            currentUser = userDAO.getUser();
                        }
                        if (someUser.getId()==-2) {
                            userDAO = new UserDAO();
                            userDAO.add(someUser);
                            currentUser = userDAO.getUser();
                        }
                    }
                    TaskDAO taskDAO = new TaskDAO(currentUser);
                    boolean exitFlag = false;
                    TreeMap<Integer,Task> completed = new TreeMap<>();
                    TreeMap<Integer,Task> notCompleted = new TreeMap<>();
                    int taskCounter = 1;
                    for (Task task : taskDAO.get()) {
                        if (task.isCompleted()) {
                            completed.put(taskCounter, task);
                        } else {
                            notCompleted.put(taskCounter, task);
                        }
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
                                int taskDisplayID = Integer.parseInt(menu.taskIDPrompt());
                                int taskID;
                                if (completed.containsKey(taskDisplayID)){
                                    taskID = completed.get(taskDisplayID).getId();
                                }else{
                                    taskID = notCompleted.get(taskDisplayID).getId();
                                }
                                taskDAO.remove(taskID);
                                messageToClient.println("[+] Task removed successfully!");
                            } catch (IndexOutOfBoundsException e) {
                                messageToClient.println("[-] Invalid task ID!");
                            } catch (NumberFormatException e) {
                                messageToClient.println("[-] Enter a number!");
                            }
                            break;
                        case "3":
                            try {
                                int taskDisplayID = Integer.parseInt(menu.taskIDPrompt());
                                int taskID;
                                if (completed.containsKey(taskDisplayID)){
                                    taskID = completed.get(taskDisplayID).getId();
                                }else{
                                    taskID = notCompleted.get(taskDisplayID).getId();
                                }
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
                                int taskDisplayID = Integer.parseInt(menu.taskIDPrompt());
                                Task taskToEdit;
                                if (completed.containsKey(taskDisplayID)){
                                    taskToEdit = completed.get(taskDisplayID);
                                }else{
                                    taskToEdit = notCompleted.get(taskDisplayID);
                                }
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
                            exitFlag = true;
                            messageToClient.println("{close}");
                            socket.close();
                            System.out.println("[+] Client disconnected > " + socket);
                            break;
                        default:
                            messageToClient.println("[-] Invalid option!");
                    }
                    if (exitFlag) break;
                } catch (SQLException s) {
                    messageToClient.println(s.getMessage());
                } catch (IllegalArgumentException e) {
                    messageToClient.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[-] Error");
            e.printStackTrace();
        }

    }

    private int getRealTaskID (List<Task> tasks, String displayID) throws IndexOutOfBoundsException, NumberFormatException {
        Task task = tasks.get(Integer.parseInt(displayID)-1);
        return task.getId();
    }
}
