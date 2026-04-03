package com.footballmanager.footballmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RequestSender {

    public static synchronized void sendRequest(Player player){

        File file = new File("src/main/resources/Squads/TransferReq.txt");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(file,true);
            fileWriter.write(player.name + "," + player.pos + "," + player.rating + "," + player.salary + "," + player.nation + "," + player.cardPath + "," + player.seller + "," + player.buyer + "," + player.fee + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
