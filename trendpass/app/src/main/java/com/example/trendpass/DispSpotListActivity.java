package com.example.trendpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.trendpass.async.AsyncDispRankingActivity;
import com.example.trendpass.async.AsyncDispSpotListActivity;
import com.example.trendpass.async.AsyncMyPageReviewActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_list);

        //　ユーザーIDを取得
        SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = loginData.getString("userId", "");

        try {
            String ip= getString(R.string.ip);
            new AsyncDispSpotListActivity(DispSpotListActivity.this)
                    .execute(new URL("http://"+ip+":8080/trendpass/SpotListServlet?userId=" + userId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Button rankingBtn = findViewById(R.id.dispRankingBtn);
        rankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String ip= getString(R.string.ip);
                    new AsyncDispRankingActivity(DispSpotListActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/RankingServlet"));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button dispPassBtn = findViewById(R.id.dispPassBtn);
        dispPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //　ユーザーIDを取得
                    SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                    String userId = loginData.getString("userId", "");

                    String ip= getString(R.string.ip);
                    new AsyncDispSpotListActivity(DispSpotListActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/SpotListServlet?userId=" + userId));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
