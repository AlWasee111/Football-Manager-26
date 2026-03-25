package com.footballmanager.footballmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TransferManager {

    public static synchronized void sell(String playerName, int idx, double fee){
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
            fileWriter.write(playerName + "," + idx + "," + fee + "\n");
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

    public static synchronized void buy(String playerName, int buyeridx, int selleridx, double fee){

        String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
                "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};


        File file1 = new File("src/main/resources/Squads/TransferList.txt");
        Scanner scanner;

        ArrayList<String> players = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file1);
                while (scanner.hasNextLine()){
                    players.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String infoToRemove = playerName + "," + selleridx + "," + fee;

        players.remove(infoToRemove);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String player : players) {
            printWriter.println(player);
        }
        printWriter.close();

        String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
                "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

        File file2 = new File("src/main/resources/" + squads[buyeridx]);
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

        File file3 = new File("src/main/resources/Squads/SquadBudgets.txt");

        ArrayList<String> budgets = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file3);
                while (scanner.hasNextLine()){
                    budgets.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if(buyeridx != selleridx){
            double buyerBudget = Double.parseDouble(budgets.get(buyeridx));
            double sellerBudget = Double.parseDouble(budgets.get(selleridx));

            sellerBudget += fee;
            buyerBudget -= fee;

            budgets.set(buyeridx, String.format("%.1f", buyerBudget));
            budgets.set(selleridx, String.format("%.1f", sellerBudget));

            PrintWriter printWriter2 = null;
            try {
                printWriter2 = new PrintWriter(file3);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            for (String budget : budgets) {
                printWriter2.println(budget);
            }
            printWriter2.close();
        }
    }

    public static synchronized void reqSell(String offerInfo, int sellFrom, int sellTo, double fee){
        String[] splitInfo = offerInfo.split(" - ");

        sell(splitInfo[0], sellFrom, fee);
        buy(splitInfo[0], sellTo, sellFrom,fee);

        File file = new File("src/main/resources/Squads/TransferReq.txt");
        Scanner scanner;

        ArrayList<String> offers = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    offers.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String offerInfoToRemove = splitInfo[0] + "," + sellTo + "," + sellFrom + "," + fee;

        offers.remove(offerInfoToRemove);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String offer : offers) {
            printWriter.println(offer);
        }
        printWriter.close();
    }

    public static synchronized void reqReject(String offerInfo, int sellFrom, int sellTo, double fee){
        String[] splitInfo = offerInfo.split(" - ");

        File file = new File("src/main/resources/Squads/TransferReq.txt");
        Scanner scanner;

        ArrayList<String> offers = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    offers.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String offerInfoToRemove = splitInfo[0] + "," + sellTo + "," + sellFrom + "," + fee;

        offers.remove(offerInfoToRemove);

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String offer : offers) {
            printWriter.println(offer);
        }
        printWriter.close();
    }
}
