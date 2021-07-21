package clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner userInput;
    private BufferedReader messageFromServer;
    private PrintStream messageToServer;
    private String input;
    private String result;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 4444);
        this.userInput = new Scanner(System.in);
        this.messageFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.messageToServer = new PrintStream(socket.getOutputStream(),true);
    }

    public void initialize() {
        try {
            while (true) {

                do {
                    result=messageFromServer.readLine();
                    if (!result.equals("{close}"))
                        System.out.println(result);
                    else {
                        System.out.println("[+] Terminating connection...");
                        socket.close();
                        userInput.close();
                        messageFromServer.close();
                        if (socket.isClosed()) {
                            System.out.println("[+] Connection closed successfully");
                            System.exit(0);
                        }
                    }

                } while (!result.contains(">>"));
                input=userInput.nextLine();
                messageToServer.println(input);
            }
        } catch (IOException e) {
            System.out.println("[-] Unexpected error occurred!");
        }
    }
}
