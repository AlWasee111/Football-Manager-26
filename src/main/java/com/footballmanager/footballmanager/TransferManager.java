package com.footballmanager.footballmanager;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TransferManager {

    public static synchronized void sell(Player player){
        String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
                "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};
        int idx = player.seller;

        File file = new File("src/main/resources/" + squads[idx]);
        Scanner scanner;
        ArrayList<Player> players = new ArrayList<>();

        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    String[] splitPlayer = scanner.nextLine().split(",");
                    if(!splitPlayer[0].equals(player.name)){
                        players.add(new Player(splitPlayer[0],splitPlayer[1],Integer.parseInt(splitPlayer[2]),Double.parseDouble(splitPlayer[3]),splitPlayer[4],splitPlayer[5]));
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(Player playerinfos : players){
            printWriter.println(playerinfos.name + "," + playerinfos.pos + "," + playerinfos.rating + "," + playerinfos.salary + "," + playerinfos.nation + "," + playerinfos.cardPath);
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
            fileWriter.write(player.name + "," + player.pos + "," + player.rating + "," + player.salary + "," + player.nation + "," + player.cardPath + "," + idx + "," + player.fee + "\n");
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

    public static synchronized void buy(Player player){

        String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
                "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};

        int selleridx = player.seller;
        int buyeridx = player.buyer;
        double fee = player.fee;

        File file1 = new File("src/main/resources/Squads/TransferList.txt");
        Scanner scanner;

        ArrayList<Player> players = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file1);
                while (scanner.hasNextLine()){
                    String[] splitPlayer = scanner.nextLine().split(",");
                    if(!splitPlayer[0].equals(player.name)){
                        Player playerInfo = new Player(splitPlayer[0],splitPlayer[1],Integer.parseInt(splitPlayer[2]),Double.parseDouble(splitPlayer[3]),splitPlayer[4],splitPlayer[5]);
                        playerInfo.setSeller(Integer.parseInt(splitPlayer[6]));
                        playerInfo.setFee(Double.parseDouble(splitPlayer[7]));
                        players.add(playerInfo);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Player playerInfo : players) {
            printWriter.println(playerInfo.name + "," + playerInfo.pos + "," + playerInfo.rating + "," + playerInfo.salary + "," + playerInfo.nation + "," + playerInfo.cardPath + "," + playerInfo.seller + "," + playerInfo.fee);
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
            fileWriter.write(player.name + "," + player.pos + "," + player.rating + "," + player.salary + "," + player.nation + "," + player.cardPath + "\n");
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

        File file4 = new File("src/main/resources/Squads/Notifications.txt");
        ArrayList<String> notifics = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file4);
                while (scanner.hasNextLine()){
                    notifics.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        notifics.add(player.name + "," + buyeridx + "," + selleridx + "," + fee + ",A");
        PrintWriter printWriter3 = null;
        try {
            printWriter3 = new PrintWriter(file4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String notific : notifics) {
            printWriter3.println(notific);
        }
        printWriter3.close();
    }

    public static synchronized void reqSell(Player player){

        sell(player);
        buy(player);

        File file = new File("src/main/resources/Squads/TransferReq.txt");
        Scanner scanner;

        ArrayList<Player> players = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    String[] splitPlayer = scanner.nextLine().split(",");
                    if(!splitPlayer[0].equals(player.name)){
                        Player playerInfo = new Player(splitPlayer[0],splitPlayer[1],Integer.parseInt(splitPlayer[2]),Double.parseDouble(splitPlayer[3]),splitPlayer[4],splitPlayer[5]);
                        playerInfo.setSeller(Integer.parseInt(splitPlayer[6]));
                        playerInfo.setBuyer(Integer.parseInt(splitPlayer[7]));
                        playerInfo.setFee(Double.parseDouble(splitPlayer[8]));
                        players.add(playerInfo);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Player playerInfo : players) {
            printWriter.println(playerInfo.name + "," + playerInfo.pos + "," + playerInfo.rating + "," + playerInfo.salary + "," + playerInfo.nation + "," + playerInfo.cardPath + "," + playerInfo.seller + "," + playerInfo.buyer + "," + playerInfo.fee);
        }
        printWriter.close();
    }

    public static synchronized void reqReject(Player player){

        File file = new File("src/main/resources/Squads/TransferReq.txt");
        Scanner scanner;

        ArrayList<Player> players = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    String[] splitPlayer = scanner.nextLine().split(",");
                    if(!splitPlayer[0].equals(player.name)){
                        Player playerInfo = new Player(splitPlayer[0],splitPlayer[1],Integer.parseInt(splitPlayer[2]),Double.parseDouble(splitPlayer[3]),splitPlayer[4],splitPlayer[5]);
                        playerInfo.setSeller(Integer.parseInt(splitPlayer[6]));
                        playerInfo.setBuyer(Integer.parseInt(splitPlayer[7]));
                        playerInfo.setFee(Double.parseDouble(splitPlayer[8]));
                        players.add(playerInfo);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Player playerInfo : players) {
            printWriter.println(playerInfo.name + "," + playerInfo.pos + "," + playerInfo.rating + "," + playerInfo.salary + "," + playerInfo.nation + "," + playerInfo.cardPath + "," + playerInfo.seller + "," + playerInfo.buyer + "," + playerInfo.fee);
        }
        printWriter.close();

        File file4 = new File("src/main/resources/Squads/Notifications.txt");
        ArrayList<String> notifics = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file4);
                while (scanner.hasNextLine()){
                    notifics.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        notifics.add(player.name + "," + player.buyer + "," + player.seller + "," + player.fee + ",R");
        PrintWriter printWriter3 = null;
        try {
            printWriter3 = new PrintWriter(file4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String notific : notifics) {
            printWriter3.println(notific);
        }
        printWriter3.close();
    }
}
