package com.footballmanager.footballmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TransferManager {

    public static synchronized void sell(String playerName, int idx){
        String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
                "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};
        File file = new File("src/main/resources/" + squads[idx]);
        Scanner scanner;
        ArrayList<String> players = new ArrayList<>();

        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    players.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        players.remove(playerName);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(String player : players){
            printWriter.println(player);
        }
        printWriter.close();

        File file1 = new File("src/main/resources/Squads/TransferList.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file1,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileWriter.write(playerName + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        scanner.close();
    }

    public static synchronized void buy(String playerName, int idx){
        File file = new File("src/main/resources/Squads/TransferList.txt");
        Scanner scanner;

        ArrayList<String> players = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    players.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        players.remove(playerName);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String player : players) {
            printWriter.println(player);
        }
        printWriter.close();

        String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
                "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

        File file2 = new File("src/main/resources/" + squads[idx]);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file2, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileWriter.write(playerName + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
