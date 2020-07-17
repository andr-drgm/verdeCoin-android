package com.andgmr.verdecoin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    public String hash;
    public String previousHash;

    public ArrayList<trans> getTransactions() {
        return transactions2;
    }

    private ArrayList<trans> transactions2 = new ArrayList<>();
    private long timeStamp; //as number of milliseconds since 1/1/1970.
    private int nonce;


    //Block Constructor.
    public Block(ArrayList<Transaction> transactions, String previousHash ) {
        for(Transaction tr : transactions){
            this.transactions2.add(new trans(tr.getFromAdress(), tr.getToAdress(), tr.getAmount()));
        }
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        transactions2
        );
        return calculatedhash;
    }

    public void mineBlock(final int difficulty) {

        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);

    }
}

class trans {

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

    public trans(String fromAdress, String toAdress, int amount) {
        this.fromAdress = fromAdress;
        this.toAdress = toAdress;
        this.amount = amount;
    }
}
