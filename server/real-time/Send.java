package com.steed.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Send implements Runnable {
    private BufferedReader console;
    private DataOutputStream dos;
    private Socket client;
    private boolean isRunning;
    private String name;

    public Send(Socket client,String name) {
        this.client = client;
        console = new BufferedReader(new InputStreamReader(System.in));
        isRunning = true;
        this.name =name;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            send(name);
        } catch (IOException e) {
            System.out.printf("----client exception----");
            this.release();
        }
    }

    private String getStrFromConsole() {
        try {
            return console.readLine();
        } catch (IOException e) {
            System.out.printf("----console exception----");
            e.printStackTrace();
        }
        return "";
    }

    private void send(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            System.out.printf("----send exception----");
            release();
        }
    }

    private void release() {
        this.isRunning = false;
        Utils.close(dos, client);
    }
    @Override
    public void run() {
        while (isRunning) {
            String msg = getStrFromConsole();
            if (!msg.equals("")) {
                send(msg);
            }
        }
    }
}
