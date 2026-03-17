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
                Integer from = (Integer) ois.readObject();
                Integer to = (Integer) ois.readObject();
                if(receivedCommand.equals("S")){
                    TransferManager.sell(playerName, from);
                }
                else if(receivedCommand.equals("B")){
                    TransferManager.buy(playerName, from);
                }
                else if(receivedCommand.equals("R")){
                    RequestSender.sendRequest(playerName,from,to);
                }
                else if(receivedCommand.equals("RS")){
                    TransferManager.reqSell(playerName, from, to);
                }
                else if(receivedCommand.equals("RR")){
                    TransferManager.reqReject(playerName, from, to);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
