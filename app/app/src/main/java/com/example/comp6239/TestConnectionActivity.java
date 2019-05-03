package com.example.comp6239;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TestConnectionActivity extends Activity implements View.OnClickListener {
    private EditText mMessageEdt;
    private static TextView mConsoleTxt;


    private static StringBuffer mConsoleStr = new StringBuffer();

    private Socket mSocket=null;
    private boolean isStartRecieveMsg = false;

    protected DataInputStream mReader=null;
    protected DataOutputStream mWriter=null;

    private String content = "";
    //接收线程发送过来信息，并用TextView显示
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            mConsoleStr.append(msg.obj.toString()+ "\n"); //add time + "  " + getTime(System.currentTimeMillis())
            mConsoleTxt.setText(mConsoleStr);
        }
    };

    private String myId = "11";
    private String counterpartId ="12";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);
        initView();
        if(!isStartRecieveMsg) {
            initSocket();
        }
    }

    private void initView() {
        mMessageEdt = findViewById(R.id.msg_edt);
        mConsoleTxt = findViewById(R.id.console_txt);
        mConsoleTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.send_btn).setOnClickListener(this);
        findViewById(R.id.clear_btn).setOnClickListener(this);
    }

    /**
     * 初始化socket
     */
    private void initSocket() {
        //新建一个线程，用于初始化socket和检测是否有接收到新的消息
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    isStartRecieveMsg = true;
                    mSocket = new Socket("35.177.19.117", 9999);
                    System.out.println("successfully connect");

                    mWriter = new DataOutputStream(mSocket.getOutputStream());
                    mReader = new DataInputStream(mSocket.getInputStream());

                    String ID = myId+":"+counterpartId;
                    mWriter.writeUTF(ID);

                    //接收来自服务器的消息
                    while(isStartRecieveMsg) {
                            while ((content = mReader.readUTF()) != null) {
                                mHandler.sendMessage(mHandler.obtainMessage(0, "Other: "+content));
                        }
                        Thread.sleep(200);
                    }
                    mReader.close();
                    mWriter.close();
                    mSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                send();
                break;
            case R.id.clear_btn:
                mConsoleStr.delete(0, mConsoleStr.length());
                mConsoleTxt.setText(mConsoleStr.toString());
                break;
            default:
                break;
        }
    }

    /**
     * 发送
     */
    private void send() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                sendMsg();
                return null;
            }
        }.execute();
    }
    /**
     * 发送消息
     */
    protected void sendMsg() {
        try {
            String msg = mMessageEdt.getText().toString().trim();
            mWriter.writeUTF(msg);
            mWriter.flush();
            System.out.println(mWriter);
            mHandler.sendMessage(mHandler.obtainMessage(0, "I: "+msg));
            System.out.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartRecieveMsg = false;
    }

    private static String getTime(long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

}
