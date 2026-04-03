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
                Player player = (Player) ois.readObject();
                if(receivedCommand.equals("S")){
                    TransferManager.sell(player);
                }
                else if(receivedCommand.equals("B")){
                    TransferManager.buy(player);
                }
                else if(receivedCommand.equals("R")){
                    RequestSender.sendRequest(player);
                }
                else if(receivedCommand.equals("RS")){
                    TransferManager.reqSell(player);
                }
                else if(receivedCommand.equals("RR")){
                    TransferManager.reqReject(player);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
