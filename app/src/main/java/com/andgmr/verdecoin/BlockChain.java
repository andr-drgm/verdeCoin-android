package com.andgmr.verdecoin;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockChain {

    private ArrayList<Block> blockchain = new ArrayList<>();
    private ArrayList<Transaction> pendingTransactions = new ArrayList<>();

    private int difficulty = 2;
    private int miningReward = 100;

    /**
     * The first block(should have no  transactions)
     */
    public BlockChain() {
        this.blockchain.add(new Block(pendingTransactions, "0"));
    }

    public Block getLatestBlock(){
        return blockchain.get(blockchain.size() - 1);
    }

    public void minePendingTransactions(String miningRewardAdress){
        Block block = new Block(pendingTransactions, getLatestBlock().hash);
        block.mineBlock(difficulty);
        System.out.println("Block successfully mined!");
        this.blockchain.add(block);

        this.pendingTransactions.clear();
        this.pendingTransactions.add(new Transaction(null, miningRewardAdress, miningReward));

    }

    public void createTransaction(Transaction transaction){
        pendingTransactions.add(transaction);
    }

    public int getBalanceOfAddress(String address){
        int balance = 0;
        for(Block block : blockchain){
            for (trans transaction : block.getTransactions()){
                if(transaction.getFromAdress() != null)
                if(transaction.getFromAdress().equals(address)){
                    balance -= transaction.getAmount();
                }

                if(transaction.getToAdress() != null)
                if(!transaction.getToAdress().isEmpty())
                if(transaction.getToAdress().equals(address)){
                    balance = balance + transaction.getAmount();
                }
            }
        }
        return balance;
    }

    public Boolean isChainValid() {
        //It works when there are no Transactions in calculateHash()
        for(int i = 1; i < blockchain.size(); i++){
            Block currentBlock = blockchain.get(i);
            Block previousBlock  = blockchain.get(i - 1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                    System.out.println("Current Hashes not equal");
                  return false;
             }
             if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
        }

}
