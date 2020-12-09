package com.example.trendpass;

import android.os.Bundle;

import com.example.trendpass.async.AsyncInsert;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.net.MalformedURLException;
import java.net.URL;

public class InsertReviewActivity extends AppCompatActivity {

    //変数
    private int userId = 0000000;
    private int spotId = 0000000012;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);

        /*レーティングバー周りの処理*/

        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar2);
        // ☆の最大数の設定及び取得
        ratingBar.setNumStars(5);
        int num = ratingBar.getNumStars();
        // 現在のレイティングの設定及び取得
        ratingBar.setRating(2.5f);
        float rating = ratingBar.getRating();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button sendBtn = (Button) findViewById(R.id.button2);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String ip = getString(R.string.ip);
                EditText eText = (EditText) findViewById(R.id.title) ;
                String title = eText.getText().toString();
                EditText eText2 = (EditText) findViewById(R.id.review2);
                String review2 = eText2.getText().toString();

                try {
                    new AsyncInsert(InsertReviewActivity.this)
                            .execute(new URL("http://" + ip + ":8080/trendpass/SampleServlet?title="
                                    + title + "spotId=" + spotId + "userId=" + userId + "rating=" + num + "review=" + review2 ));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}