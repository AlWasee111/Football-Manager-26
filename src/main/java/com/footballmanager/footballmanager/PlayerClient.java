package com.footballmanager.footballmanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerClient {
    private static Socket clientSocket;
    private static ObjectOutputStream oos;

    public static void connect(){
        try {
            clientSocket = new Socket("127.0.0.1",2222);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendCommand(String command, Player player){
        try {
            oos.writeObject(command);
            oos.writeObject(player);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
