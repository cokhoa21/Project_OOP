package com.example.datacollection.twitter;

import java.io.*;

public class AccountManager {
    private BufferedReader reader;

    public AccountManager(String filePath) {
        try {
            this.reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] changeAccount() {
        String[] accountDetails = new String[2];
        try {
            accountDetails[0] = reader.readLine(); // username
            accountDetails[1] = reader.readLine(); // password
            if (accountDetails[0] == null || accountDetails[1] == null) {
                resetReader();
                accountDetails[0] = reader.readLine(); // username
                accountDetails[1] = reader.readLine(); // password
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountDetails;
    }

    private void resetReader() {
        try {
            reader.close();
            reader = new BufferedReader(new FileReader("src/sampleapp/account.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}