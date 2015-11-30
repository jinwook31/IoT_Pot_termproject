package com.example.jiil.tremproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Activity;

/**
 * Created by jiil on 2015-11-27.
 */
public class ChatBubbleActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private int degree;
    private int humidity;
    private int degiDeg;
    private int degiHum;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    Intent intent;
    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        degree = Integer.parseInt(i.getStringExtra("currDeg"));
        humidity = Integer.parseInt(i.getStringExtra("currHum"));
        degiDeg = i.getIntExtra("desiDeg",0);
        degiHum = i.getIntExtra("desiHum",0);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listView1);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.chatText);


        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });


    }

    private boolean sendChatMessage(){
        ChatMessage myChat = new ChatMessage(!side,chatText.getText().toString());
        chatText.setText("");
        chatArrayAdapter.add(myChat);


        if(myChat.message.contains("배고")) {
            if(humidity < degiHum-1) {
                chatArrayAdapter.add(new ChatMessage(side, "오지게 배고프다"));
            }
            else if (degiHum-1<= humidity && humidity <= degiHum +1){
                chatArrayAdapter.add(new ChatMessage(side,"안배고프다 말걸지마라"));
            }
            else {
                chatArrayAdapter.add(new ChatMessage(side,"야 너무 배불러 그만줘"));
            }
        }
        else if(myChat.message.contains("추") ||myChat.message.contains("춥") ||myChat.message.contains("온도")
                ||myChat.message.contains("덥") ||myChat.message.contains("더워")||myChat.message.contains("차갑")
                ||myChat.message.contains("차가")){
            if(degree < degiDeg -1) {
                chatArrayAdapter.add(new ChatMessage(side, "야 몸이 덜덜 떨린다. 온도좀 올려봐"));
            }
            else if(degiDeg-1 <= degree &&degree <= degiDeg+1){
                chatArrayAdapter.add(new ChatMessage(side,"음 딱 좋은 것 같은데."));
            }
            else {
                chatArrayAdapter.add(new ChatMessage(side,"야 이러다가 익겠다. 온도좀 내려"));
            }
        }
        else if(myChat.message.contains("상태") || myChat.message.contains("정보") ||myChat.message.contains("어때")) {
            chatArrayAdapter.add(new ChatMessage(side,"음 딱 좋은 것 같은데."));
        }
        else if(myChat.message.contains("뭐해") ||myChat.message.contains("뭐하") ||myChat.message.contains("뭐함")) {
            chatArrayAdapter.add(new ChatMessage(side,"보면 모르냐 숨쉬고 있지."));
        }
        else {
            chatArrayAdapter.add(new ChatMessage(side,"뭐라는거야?"));
        }

        //side = !side;
        return true;
    }

//    public boolean onKeyDown(int keyCode,KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                new AlertDialog.Builder(this)
//                        .setTitle("종료")
//                        .setMessage("종료 하시겠습니까?")
//                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        })
//                        .setPositiveButton("아니오",null).show();
//                return false;
//            default:
//                return false;
//        }
//    }
}
