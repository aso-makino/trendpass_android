package com.example.trendpass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncSpotDetailActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_detail);

        Intent intent = getIntent();
        String spotId = intent.getStringExtra("spotId");

        try {
            String ip= getString(R.string.ip);
            new AsyncSpotDetailActivity(DispSpotDetailActivity.this)
                    .execute(new URL("http://"+ip+":8080/trendpass/DispSpotDetail?spotId=" + spotId));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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


    }
}

