package com.bado.ignacio.tcpchat;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        final EditText editText = findViewById(R.id.sample_text);
        Button btnSubmit = findViewById(R.id.btn_submit);
        boolean status = initServer();
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
                    }
                });
            }
        });
    }

    public native boolean initServer();

    public native void sendMessage(String message);
}
