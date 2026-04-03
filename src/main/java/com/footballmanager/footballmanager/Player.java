package com.footballmanager.footballmanager;

import java.io.Serializable;

public class Player implements Serializable {
    String name;
    String pos;
    int rating;
    double salary;
    String nation;
    String cardPath;
    int seller;
    int buyer;
    double fee;

    Player(String name, String pos, int rating, double salary, String nation, String cardPath){
        this.name = name;
        this.pos = pos;
        this.rating = rating;
        this.salary = salary;
        this.nation = nation;
        this.cardPath = cardPath;
    }

    public void setSeller(int seller){
        this.seller = seller;
    }
    public void setBuyer(int buyer){
        this.buyer = buyer;
    }
    public void setFee(double fee){
        this.fee = fee;
    }
    //Kai Havertz,ST,82,220000,GER,Havertz
}
