package com.example.trendpass;

import android.content.Intent;
import android.os.Bundle;

import com.example.trendpass.async.AsyncDelete;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;

public class ConfirmUnsubscribeActivity extends AppCompatActivity {

    //変数
    private int userId = 0000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_unsubscribe);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button sendBtn = (Button) findViewById(R.id.button3);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            String ip = getString(R.string.ip);

            public void onClick(View v) {
                try {
                    new AsyncDelete(ConfirmUnsubscribeActivity.this)
                            .execute(new URL("http://" + ip + ":8080/trendpass/SampleServlet?userId=" + userId ));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ConfirmUnsubscribeActivity.this, CompleteUnsubscribeActivity.class);
                startActivity(intent);
            }
        });
    }
}