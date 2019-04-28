package com.steed.test;
/**
 * 多线程多客户
 * 目标：加入容器实现群聊
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chat {
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
        private String name;

        //接收消息
        public Channel(Socket client){
            this.client = client;
            try {
                dis = new DataInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());
                isRunning =true;
                this.name = receive();
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
            boolean isPrivate = msg.startsWith("@");
            if (isPrivate) {
                int idx = msg.indexOf(":");
                String targetName = msg.substring(1,idx);
                msg = msg.substring(idx + 1);
                for (Channel others : all) {
                    if (others.name.equals(targetName)) {
                        others.send(this.name+" send privately: "+msg);
                        break;
                    }
                }
            }else {
                for (Channel others : all) {
                    if (others == this) {
                        continue;
                    }
                    others.send(this.name + " said: " + msg);
                }
            }
        }

        //释放资源
        private void release() {
            this.isRunning = false;
            Utils.close(dis, dos, client);
            all.remove(this);
            sendOthers(this.name+" has left...");
        }

        @Override
        public void run() {
            while (isRunning) {
                String msg = receive();
                if (!msg.equals("")) {
                    System.out.println("client "+client.hashCode()+" send: "+ msg);
                    //send(msg);
                    sendOthers(msg);
                }
            }
        }
    }
}
