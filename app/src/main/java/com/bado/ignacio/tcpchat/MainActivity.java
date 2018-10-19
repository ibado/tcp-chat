package com.bado.ignacio.tcpchat;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private RecyclerView mChatList;
    private LinearLayoutManager mChatListLayoutManager;
    private ChatMessageAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.sample_text);
        FloatingActionButton btnSubmit = findViewById(R.id.btn_submit);
        mChatList = findViewById(R.id.rv_main);
        initRecycler();

        initClient(8096);
        final Handler handler = new Handler();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(editText.getText().toString());
                        editText.setText("");
                    }
                });
            }
        });
    }

    private void initRecycler() {
        mChatList.setHasFixedSize(true);

        mChatListLayoutManager = new LinearLayoutManager(this);

        List<ChatMessage> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new ChatMessage("hola que hace item " + i, i % 2 == 0));
        }
        mChatListAdapter = new ChatMessageAdapter(list);

        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatList.setAdapter(mChatListAdapter);
    }

    public void executeMe(String message) {
        Toast.makeText(this, "msg: ".concat(message), Toast.LENGTH_SHORT).show();
    }

    public native boolean initClient(int port);

    public native void sendMessage(String message);
}
