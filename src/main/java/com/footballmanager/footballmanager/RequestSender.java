package com.footballmanager.footballmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RequestSender {

    public static synchronized void sendRequest(String playerName, int reqfrom, int reqto, double fee){

        File file = new File("src/main/resources/Squads/TransferReq.txt");
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(file,true);
            fileWriter.write(playerName + "," + reqfrom + "," + reqto + "," + fee + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
