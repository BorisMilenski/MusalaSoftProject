package clientserver;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.initialize();
        } catch (IOException e) {
            System.out.println("[-] Error connecting to server!");
        }

    }
}
