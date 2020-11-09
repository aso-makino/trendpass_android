package com.example.trendpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.trendpass.async.SampleAsyncActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class SampleMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);



    }

    protected void onResume() {
        super.onResume();

        findViewById(R.id.mapbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("mapが押されました");
            }
        });

        findViewById(R.id.insertbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("insertが押されました");
            }
        });

        findViewById(R.id.listbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("listが押されました");
            }
        });

        findViewById(R.id.userbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("userが押されました");
            }
        });

        Button sendBtn = (Button) findViewById(R.id.button);
        sendBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                EditText eText = (EditText) findViewById(R.id.etword);
                String word = eText.getText().toString();

                    try {
                        String ip= getString(R.string.ip);
                        new SampleAsyncActivity(SampleMainActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/SampleServlet?name=" + word));

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
        });
    }
}
