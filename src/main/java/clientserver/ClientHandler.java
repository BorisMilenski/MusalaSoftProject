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

                    isRunning = menu.mainMenu(taskDAO, allTasks);

                } catch (Exception e) {
                    messageToClient.println(e.getMessage());
                }
            }
            socket.close();
            System.out.println("[+] Client disconnected > " + socket);
        } catch (IOException e) {
            System.out.println("[-] Error");
            e.printStackTrace();
        }
    }
}
