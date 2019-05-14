package com.example.comp6239;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class TutorChatActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mMessageEdt;
    private static TextView mConsoleTxt;
    private String appointment_id;

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
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        myId = bundle.getString("my_id");
        counterpartId = bundle.getString("counterpart_id");
        appointment_id = bundle.getString("appointment_id");


        initView();

        //BackTool Bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

//        mConsoleTxt.setText("2131232133213");
        StringBuffer tmp = mConsoleStr;
        if(mConsoleStr.length()==0) {
            tmp = setMessage();}
        mConsoleTxt.setText(tmp);
        if(!isStartRecieveMsg) {
            initSocket();
        }

    }

    private void initView() {
        mMessageEdt = findViewById(R.id.msg_edt);
        mConsoleTxt = findViewById(R.id.console_txt);
        mConsoleTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
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
                    mSocket = new Socket("35.178.209.191", 9999);
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
                mMessageEdt.setText("");
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
            if (msg.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Input Content", Toast.LENGTH_LONG).show();
            }else {
                mWriter.writeUTF(msg);
                mWriter.flush();
                mHandler.sendMessage(mHandler.obtainMessage(0, "I: " + msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isStartRecieveMsg = false;
    }

//    private static String getTime(long millTime) {
//        Date d = new Date(millTime);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(d);
//    }

    public StringBuffer setMessage() {
        String rawString;
        JSONObject jsonObject;
        JSONArray jsonArray;
        rawString = GetDataFromPHP.getChatMessage(Integer.parseInt(myId), Integer.parseInt(counterpartId));
        try {
            jsonObject = new JSONObject(rawString);
            jsonArray = jsonObject.getJSONArray("messages");
            for (int i = jsonArray.length()-1; i > -1; i--) {
                JSONObject info = jsonArray.getJSONObject(i);
                if (info.getString("sender_user_id").equals(myId)) {
                    mConsoleStr.append("I: "+info.getString("details")+ "\n");
                }else {mConsoleStr.append("Other: "+info.getString("details")+ "\n");}
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mConsoleStr;
    }

    //Back Tool Bar settings
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent;
//                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                homeIntent = new Intent(getApplicationContext(), AppointmentApproveActivity.class);
                homeIntent.putExtra("appointment_id", appointment_id);
                startActivity(homeIntent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
