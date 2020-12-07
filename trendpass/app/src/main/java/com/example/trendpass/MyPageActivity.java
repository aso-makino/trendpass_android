package com.example.trendpass;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
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

        Button dispReviewBtn = findViewById(R.id.dispRankingBtn);
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

        Button dispSpotBtn = findViewById(R.id.dispPassBtn);
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

        //footerの生成
        ImageButton mapButton = findViewById(R.id.mapbtn);
        ImageButton insertButton = findViewById(R.id.insertbtn);
        ImageButton mapListButton = findViewById(R.id.listbtn);
        ImageButton userButton = findViewById(R.id.userbtn);

        //ユーザーボタンをタッチした時の処理
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしてユーザー設定画面へ
            public void onClick(View view) {
                //設定画面へ
                Intent intent = new Intent(MyPageActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(MyPageActivity.this)
                        .setPositiveButton("現在地からスポット投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //現在地周辺スポット一覧画面へ
//                                Intent intent = new Intent(MyPageActivity.this, NearSpotListActivity.class);
//                                intent.putExtra("latitude",latitude);
//                                intent.putExtra("longitude",longitude);
//                                startActivity(intent);
//                                Log.v("Alert", "スポット一覧へ");
                                ///////////////////////////
                            }
                        })



                        .setNeutralButton("メモしたスポットから投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ////////////////////////////
                                //位置情報履歴画面へ
//                        Intent intent = new Intent(MyPageActivity.this,Activity.class);
//                        startActivity(intent);

                            }
                        })
                        // 口コミ投稿ボタンの処理
                        .setNegativeButton("口コミ投稿", new DialogInterface.OnClickListener() {
                            @Override
                            //口コミ投稿ボタンをタッチ
                            public void onClick(DialogInterface dialog, int which) {
                                ////////////////////////////
                                //口コミ投稿画面へ
//                                Intent intent = new Intent(MyPageActivity.this, Activity.class);
//                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        });


        //スポットリストボタンをタッチした時の処理
        mapListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチスポット一覧表示画面へ遷移する
            public void onClick(View view) {
                    ///////////////////////////
                    //現在地周辺スポット一覧画面へ
                    Intent intent = new Intent(MyPageActivity.this, DispSpotListActivity.class);
                    startActivity(intent);
                    Log.v("Alert", "スポット一覧へ");
            }
        });

        //マップボタンをタッチした時の処理
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチしてユーザー設定画面へ
            public void onClick(View view) {
                //設定画面へ
                Intent intent = new Intent(MyPageActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });
    }
}

