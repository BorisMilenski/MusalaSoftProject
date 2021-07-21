package clientserver;

import database.TaskDAO;
import entities.Priority;
import entities.Task;
import logic.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {
    final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String input;
        TaskDAO taskDAO = TaskDAO.getInstance();

        try {
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream messageToClient = new PrintStream(socket.getOutputStream(),true);
            Menu menu = new Menu(messageToClient, userInputReader);
            while (true) {
                try {
                    boolean exitFlag = false;
                    List<Task> tasks = taskDAO.getTasks();
                    ArrayList<Task> completed = new ArrayList<Task>();
                    ArrayList<Task> notCompleted = new ArrayList<Task>();
                    int taskCounter = 1;
                    for (Task task : tasks) {
                        if (task.isCompleted()) {
                            completed.add(task);
                        } else {
                            notCompleted.add(task);
                        }
                    }
                    tasks.clear();
                    tasks.addAll(completed);
                    tasks.addAll(notCompleted);
                    //ordering tasks to match indexes in array
                    messageToClient.println("[+] Completed tasks:");
                    if (completed.isEmpty()) {
                        messageToClient.println("None");
                    } else {
                        for (Task task : completed) {
                            messageToClient.println(taskCounter + ". " + task);
                            taskCounter++;
                        }
                    }
                    messageToClient.println("[*] Remaining tasks:");
                    if (notCompleted.isEmpty()) {
                        messageToClient.println("None");
                    } else {
                        for (Task task : notCompleted) {
                            messageToClient.println(taskCounter + ". " + task);
                            taskCounter++;
                        }
                    }
                    menu.printMainMenu();
                    input = userInputReader.readLine();

                    switch (input) {
                        case "1":
                            Task task = menu.addTaskPrompt();
                            taskDAO.addTask(task);
                            messageToClient.println("[+] Task added successfully!");
                            break;
                        case "2":
                            try {
                                String taskDisplayID = menu.taskIDPrompt();
                                int taskID = getRealTaskID(tasks,taskDisplayID);
                                taskDAO.removeTask(taskID);
                                messageToClient.println("[+] Task removed successfully!");
                            } catch (IndexOutOfBoundsException e) {
                                messageToClient.println("[-] Invalid task ID!");
                            } catch (NumberFormatException e) {
                                messageToClient.println("[-] Enter a number!");
                            }
                            break;
                        case "3":
                            try {
                                String taskDisplayID = menu.taskIDPrompt();
                                int taskID = getRealTaskID(tasks,taskDisplayID);
                                taskDAO.markTaskasCompleted(taskID, LocalDateTime.now());
                                messageToClient.println("[+] Task marked as completed!");
                            } catch (IndexOutOfBoundsException e) {
                                messageToClient.println("[-] Invalid task ID!");
                            } catch (NumberFormatException e) {
                                messageToClient.println("[-] Enter a number!");
                            }
                            break;
                        case "4":
                            try {
                                String taskDisplayID = menu.taskIDPrompt();
                                int taskID=getRealTaskID(tasks,taskDisplayID);
                                Task editedTask = menu.editTaskPrompt(tasks.get(Integer.parseInt(taskDisplayID)-1));
                                messageToClient.println("editedTask id is: " + editedTask.getId());
                                taskDAO.editTask(taskID,editedTask);
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
                    messageToClient.println("[-] Error connecting to database!");
                } catch (IllegalArgumentException e) {
                    messageToClient.println("[-] Invalid task ID!");
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
