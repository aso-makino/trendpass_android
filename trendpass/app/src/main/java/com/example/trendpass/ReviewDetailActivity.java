package com.example.trendpass;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.squareup.picasso.Picasso;

public class ReviewDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);


        Intent intent = getIntent();
        // intentで受け取ったものを取り出す
        int reviewRating = intent.getIntExtra("reviewRating",0);
        String reviewContent = intent.getStringExtra("reviewContent");
        String reviewImage = intent.getStringExtra("reviewImage");
        String spotName = intent.getStringExtra("spotName");

        //スポット名
        TextView spotNametv = this.findViewById(R.id.spotName);
        spotNametv.setText(spotName);
        spotNametv.setTextSize(22.0f);

        //口コミ画像
        String ip= getString(R.string.ip);
        Picasso.with(this.getApplicationContext())
                .load("http://"+ip+":8080/trendpass/DisplayImage?name=" +reviewImage)
                .resize(500,500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.reviewImg));

        //評価
        RatingBar ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                Drawable progressDrawable = ratingBar.getProgressDrawable();
                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable, ContextCompat.getColor(ratingBar.getContext(), R.color.ratingbar));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ratingBar.setRating(reviewRating);


        //口コミ内容
        TextView textView = findViewById(R.id.reviewContent);
        textView.setText(reviewContent);

    }

    protected void onResume() {
        super.onResume();

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
                Intent intent = new Intent(ReviewDetailActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        //投稿ボタンをタッチした時の処理
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //ボタンタッチして投稿種類選択ダイアログ表示
            public void onClick(View view) {

                new AlertDialog.Builder(ReviewDetailActivity.this)
                        .setPositiveButton("現在地からスポット投稿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ////////////////////////////
                                //現在地周辺スポット一覧画面へ
//                                Intent intent = new Intent(ReviewDetailActivity.this, NearSpotListActivity.class);
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
//                        Intent intent = new Intent(ReviewDetailActivity.this,Activity.class);
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
//                                Intent intent = new Intent(ReviewDetailActivity.this, Activity.class);
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
                Intent intent = new Intent(ReviewDetailActivity.this, DispSpotListActivity.class);
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
                Intent intent = new Intent(ReviewDetailActivity.this, DispMapActivity.class);
                startActivity(intent);
            }
        });


    }
}
