package com.example.trendpass;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.squareup.picasso.Picasso;

public class ReviewDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        //ロゴ
        Picasso.with(this.getApplicationContext())
                .load(R.drawable.rogo)
                .resize(500,500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.rogoImg));

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
