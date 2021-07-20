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

        try {
            ServerSocket server = new ServerSocket(4444);

            while (true) {
                Socket socket = null;

                socket = server.accept();

                System.out.println("[+] New client connected >> " + socket);

                Thread th = new ClientHandler(socket);
                th.start();

            }
        } catch (IOException e ) {
            System.out.println("[-] Error accepting client!");
            e.printStackTrace();
        }

    }
}
