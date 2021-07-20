package clientserver;

import entities.Priority;
import entities.Task;
import logic.BasicTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerApp {
    public static void main(String[] args) {
        String input;
        try {
            ServerSocket server = new ServerSocket(4444);

            while (true) {
                Socket socket = server.accept();
                System.out.println("[+] New client connected >> " + socket);
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
                    for (Task task : tasks) {
                        messageToClient.println(task);
                    }
                    messageToClient.println(">>");
                    input = userInputReader.readLine();
                    if (input.equals("exit")) {
                        messageToClient.println("{close}");
                    }
                    else {
                        messageToClient.println("not closing");
                    }
                }

            }

        } catch (IOException e) {
            System.out.println("[-] Error starting server!");
            e.printStackTrace();
        }

    }
}
