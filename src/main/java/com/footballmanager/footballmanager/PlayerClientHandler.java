package com.footballmanager.footballmanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class PlayerClientHandler implements Runnable{
    Socket socket;
    Thread t;

    PlayerClientHandler(Socket socket){
        this.socket = socket;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true){
                String receivedCommand = (String) ois.readObject();
                String playerName = (String) ois.readObject();
                Integer idx = (Integer) ois.readObject();
                if(receivedCommand.equals("S")){
                    TransferManager.sell(playerName, idx);
                }
                else if(receivedCommand.equals("B")){
                    TransferManager.buy(playerName, idx);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
