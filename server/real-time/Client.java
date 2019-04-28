package com.steed.test;

/**
 * 多线程多客户
 *加入容器实现群聊
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * One client send and receive message
 */
public class Client {
    public static void main(String[] args) throws Exception{
        System.out.printf("----Client-----\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("User name");
        String name = br.readLine();
        Socket client = new Socket("localhost", 9999);
        new Thread(new Send(client,name)).start();
        new Thread(new Receive(client)).start();
    }
}
