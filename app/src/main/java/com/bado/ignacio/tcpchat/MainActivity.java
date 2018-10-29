package com.bado.ignacio.tcpchat;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
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
    private Thread mMessageListener;
    private List<ChatMessage> mChatMessages;

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
                        String message = editText.getText().toString();
                        sendMessage(message);
                        mChatMessages.add(new ChatMessage(message, true));
                        editText.setText("");
                    }
                });
            }
        });

        mMessageListener = new MessageListener(new ShowStrategy() {
            @Override
            public void showMessage(String message) {
                mChatMessages.add(new ChatMessage(message, false));
                mChatListAdapter.notifyItemInserted(mChatListAdapter.getItemCount());
            }
        });
        mMessageListener.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessageListener.interrupt();
    }

    private void initRecycler() {
        mChatList.setHasFixedSize(true);

        mChatListLayoutManager = new LinearLayoutManager(this);

        mChatMessages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mChatMessages.add(new ChatMessage("hola que hace item " + i, i % 2 == 0));
        }
        mChatListAdapter = new ChatMessageAdapter(mChatMessages);

        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatList.setAdapter(mChatListAdapter);
    }

    private class MessageListener extends Thread {

        Handler mHadler;
        private ShowStrategy mStrategy;

        MessageListener(ShowStrategy showStrategy) {
            mHadler = new Handler(Looper.getMainLooper());
            mStrategy = showStrategy;
        }

        @Override
        public void run() {
            while (true) {

                final String message = getMessage();
                mHadler.post(new Runnable() {
                    @Override
                    public void run() {
                        mStrategy.showMessage(message);
                    }
                });
            }
        }
    }

    interface ShowStrategy  {
        void showMessage(String message);
    }

    public native boolean initClient(int port);

    public native void sendMessage(String message);

    public native String getMessage();
}
