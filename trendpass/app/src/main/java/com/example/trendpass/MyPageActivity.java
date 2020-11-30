package com.example.trendpass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncMyPageReviewActivity;
import com.example.trendpass.async.AsyncMyPageSpotActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Picasso.with(this)
                .load(R.drawable.settings)
                .fit()
                .centerInside()
                .into((ImageView) findViewById(R.id.settings_btn));

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = loginData.getString("userId", "");

        try {
            String ip= getString(R.string.ip);
            new AsyncMyPageReviewActivity(MyPageActivity.this)
                    .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void onResume() {
        super.onResume();

        Button dispReviewBtn = findViewById(R.id.dispReviewBtn);
        dispReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //　ユーザーIDを取得
                    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                    String userId = loginData.getString("userId", "");

                    String ip= getString(R.string.ip);
                    new AsyncMyPageReviewActivity(MyPageActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button dispSpotBtn = findViewById(R.id.dispSpotBtn);
        dispSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //　ユーザーIDを取得
                    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                    String userId = loginData.getString("userId", "");

                    String ip= getString(R.string.ip);
                    new AsyncMyPageSpotActivity(MyPageActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/MyPageServlet?userId=" + userId));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }
}

