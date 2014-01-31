package com.roma.simplebj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main( String[] args ) throws IOException {
        BjGameController controller = new BjGameController();
        new Thread(new GameScheduler(controller)).start();
        ServerSocket serverSocket = new ServerSocket(1488);
        while (true) {
            Socket client = serverSocket.accept();
            boolean connected = controller.addClient(client);
            System.err.println("new client " + (connected ? "connected" : "rejected"));
        }
    }
}
