package com.bado.ignacio.tcpchat;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private RecyclerView mChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.sample_text);
        FloatingActionButton btnSubmit = findViewById(R.id.btn_submit);
        mChatList = findViewById(R.id.rv_main);

        boolean status = initClient(8096);
        if (!status)
            Toast.makeText(getApplicationContext(), "CONNECTION ERROR", Toast.LENGTH_LONG).show();
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

    public void executeMe(String message) {
        Toast.makeText(this, "msg: ".concat(message), Toast.LENGTH_SHORT).show();
    }

    public native boolean initClient(int port);

    public native void sendMessage(String message);
}
