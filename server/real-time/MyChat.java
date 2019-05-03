package com.steed.server; /**
 * 多线程多客户
 * 目标：加入容器实现群聊
 */

import com.steed.server.MySQL;
import com.steed.server.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyChat {
    private static CopyOnWriteArrayList<Channel> all = new CopyOnWriteArrayList<Channel>();//多线程中需要改和遍历的最好不用ArrayList, 用这个并发容器
    public static void main(String[] args) throws IOException {
        System.out.println("----Server----");
        ServerSocket server = new ServerSocket(9999);
        while (true) {
            Socket client = server.accept();
            System.out.printf("One client: "+ client.hashCode()+" establish connection...\n");
            Channel c = new Channel(client);
            all.add(c);//管理所有成员
            new Thread(c).start();
        }
    }

    //一个客户一个线程
   static class Channel implements Runnable{
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client;
        private boolean isRunning;
        private String MyId;
        private String CounterpartId;

        //接收消息
        public Channel(Socket client){
            this.client = client;
            try {
                dis = new DataInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());
                isRunning =true;
                String ID = receive();
                int index = ID.indexOf(":");
                this.MyId = ID.substring(0, index);
                this.CounterpartId = ID.substring(index+1);
                System.out.println(MyId);
                System.out.println(CounterpartId);
            } catch (IOException e) {
                System.out.printf("----client exception----");
                release();
            }
        }
        private String receive() {
            String msg = "";
            try {
                msg = dis.readUTF();
            } catch (IOException e) {
                release();
            }
            return msg;
        }

        //发送消息
        private void send(String msg) {
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                System.out.printf("----send exception----");
                e.printStackTrace();
                release();
            }
        }

        //群聊

        /**
         * 私聊：@xxx:msg
         * @param msg
         */
        private void sendOthers(String msg) {
            Boolean isFind = false;
            MySQL mySQL = new MySQL();
            for (Channel others : all) {
                    if (others.MyId.equals(CounterpartId)) {
                        others.send(msg);
                        isFind = true;
                        mySQL.writeToSQL(MyId, CounterpartId, msg, 1);
                        break;
                    }
                }
            if(!isFind) {
                mySQL.writeToSQL(MyId, CounterpartId, msg, 0);
                }
            }


        //释放资源
        private void release() {
            this.isRunning = false;
            Utils.close(dis, dos, client);
            all.remove(this);
        }

        @Override
        public void run() {
            MySQL mySQL = new MySQL();
            ArrayList<String> res = mySQL.readFromSQL(this.MyId);
            if (res != null) {
                for (int i = 0; i < res.size(); i++) {
                    this.send(res.get(i));
                }
            }
            while (isRunning) {
                String msg = receive();
                if (!msg.equals("")) {
                    System.out.println("client "+client.hashCode()+" send: "+ msg);
                    sendOthers(msg);
                }
            }
        }
    }
}
