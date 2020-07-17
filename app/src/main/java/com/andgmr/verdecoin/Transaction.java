package com.andgmr.verdecoin;

public class Transaction {

    public String getFromAdress() {
        return fromAdress;
    }

    public String getToAdress() {
        return toAdress;
    }

    public int getAmount() {
        return amount;
    }

    private String fromAdress, toAdress;
    private int amount;

    public Transaction(String fromAdress, String toAdress, int amount) {
        this.fromAdress = fromAdress;
        this.toAdress = toAdress;
        this.amount = amount;
    }


}
