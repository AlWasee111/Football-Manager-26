package com.footballmanager.footballmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);

        while (true){
            Socket socket = serverSocket.accept();
            new PlayerClientHandler(socket);
        }
    }
}
