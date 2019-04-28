package com.steed.test;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable {
    private Socket client;
    private DataInputStream dis;
    private boolean isRunning;

    public Receive(Socket client) {
        this.client = client;
        isRunning = true;
        try {
            dis = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.printf("----client exception----");
            release();
        }
    }

    private String reveive() {
        String msg = "";
        try {
            msg = dis.readUTF();
        } catch (IOException e) {
            System.out.printf("----receive exception----");
            release();
        }
        return msg;
    }

    private void release() {
        isRunning = false;
        Utils.close(dis,client);
    }
    @Override
    public void run() {
        while (isRunning) {
            String msg = reveive();
            if (!msg.equals("")) {
                System.out.println(msg);
            }
        }
    }
}
