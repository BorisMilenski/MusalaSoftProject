package clientserver;

import entities.Priority;
import entities.Task;
import logic.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String input;


        try {
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream messageToClient = new PrintStream(socket.getOutputStream(),true);
            while (true) {
                BasicTask task1 = new BasicTask(1,"Get a cat", Priority.high);
                BasicTask task2 = new BasicTask(2,"Get another cat", Priority.high);
                BasicTask task3 = new BasicTask(3,"Get just one more cat", Priority.high);
                ArrayList<Task> tasks = new ArrayList<Task>();
                tasks.add(task1);
                tasks.add(task2);
                tasks.add(task3);
                ArrayList<Task> completed = new ArrayList<Task>();
                ArrayList<Task> notCompleted = new ArrayList<Task>();
                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completed.add(task);
                    } else {
                        notCompleted.add(task);
                    }
                }
                messageToClient.println("[+] Completed tasks:");
                if (completed.isEmpty()) {
                    messageToClient.println("None");
                } else {
                    for (Task task : completed) {
                        messageToClient.println(task);
                    }
                }
                messageToClient.println("[*] Remaining tasks:");
                if (notCompleted.isEmpty()) {
                    messageToClient.println("None");
                } else {
                    for (Task task : notCompleted) {
                        messageToClient.println(task);
                    }
                }
                messageToClient.println(">>");
                input = userInputReader.readLine();
                if (input.equals("exit")) {
                    messageToClient.println("{close}");
                    socket.close();
                    System.out.println("[+] Client disconnected > " + socket);
                    break;
                }
                else {
                    messageToClient.println("not closing");
                }
            }
        } catch (IOException e) {
            System.out.println("[-] Error");
            e.printStackTrace();
        }

    }
}
