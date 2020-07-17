package com.andgmr.verdecoin;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BlockChain VerdeCoin = new BlockChain();
    private EditText usernameEditText, receiverEditText, valueToSendEditText;
    private TextView balanceTextView;
    private Button loginButton, sendButton, mineButton;
    private SharedPreferences usernamePrefs, VerdeCoinJson;
    private DatabaseReference mDatabaseReference;
    private String username;
    String blockchainJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setViews();
        setUsername();
        update();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamePrefs = getSharedPreferences("USERNAME", 0);
                usernamePrefs.edit().putString("username", usernameEditText.getText().toString()).apply();
                username = usernamePrefs.getString("username", "not found");
                update();


            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDatabaseReference.child("Users").child(usernameEditText.getText().toString()).push().setValue(Integer.parseInt(valueToSendEditText.getText().toString()));
                if(VerdeCoin.getBalanceOfAddress(username) > Integer.valueOf(valueToSendEditText.getText().toString()))
                VerdeCoin.createTransaction(new Transaction(usernameEditText.getText().toString(), receiverEditText.getText().toString(), Integer.valueOf(valueToSendEditText.getText().toString())));
                else Toast.makeText(getApplicationContext(), "Not enought VerdeCoin", Toast.LENGTH_SHORT).show();
                update();
            }
        });

        mineButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                mineButton.setText("Stop mining");
                mineButton.setBackgroundColor(R.color.redcolor);
                /**
                 * Schimba culoarea la buton in verde de fiecare data(trebuie reparat)
                 */
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VerdeCoin.minePendingTransactions(usernameEditText.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mineButton.setBackgroundColor(R.color.colorPrimaryDark);
                                mineButton.setText("Mine VerdeCoin");
                                update();
                            }
                        });
                    }
                });

                thread.start();

            }
        });

    }

    private void setViews(){
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        receiverEditText = (EditText) findViewById(R.id.sendToEditText);
        sendButton = (Button) findViewById(R.id.sendButton);
        valueToSendEditText = (EditText) findViewById(R.id.valueToSendEditText);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        mineButton = (Button) findViewById(R.id.mineButton);
    }

    /** UserName */
    private void setUsername(){

        usernamePrefs = getSharedPreferences("USERNAME", 0);
        if(usernamePrefs.contains("username")){
            usernameEditText.setText("" + usernamePrefs.getString("username", "not found"));
            username = usernamePrefs.getString("username", "not found");
        }
    }



    @SuppressLint("SetTextI18n")
    private void update(){
        String username = usernameEditText.getText().toString();
        balanceTextView.setText("You have " + VerdeCoin.getBalanceOfAddress(username) + " VerdeCoin");
        blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(VerdeCoin);
    }
}
