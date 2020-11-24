package com.example.trendpass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncSpotDetailActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class DispSpotDetailActivity extends AppCompatActivity{

    AsyncSpotDetailActivity asyncSpotDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_spot_detail);

        //ロゴ
        Picasso.with(this.getApplicationContext())
                .load(R.drawable.rogo)
                .resize(500,500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.rogoImg));

        Intent intent = getIntent();
        String spotId = intent.getStringExtra("spotId");

        try {
            String ip= getString(R.string.ip);
            asyncSpotDetailActivity = new AsyncSpotDetailActivity(DispSpotDetailActivity.this);
            asyncSpotDetailActivity.execute(new URL("http://"+ip+":8080/trendpass/DispSpotDetail?spotId=" + spotId));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    protected void onResume() {
        super.onResume();

        ListView listView = (ListView)findViewById(R.id.reviewList);

        //リスト項目をクリック時に呼び出されるコールバックを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //リスト項目クリック時の処理
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent intent = new Intent(DispSpotDetailActivity.this, ReviewDetailActivity.class);
                // clickされたpositionのtextとphotoのID
                String reviewImage = asyncSpotDetailActivity.getReviewImage(position);
                int reviewRating = asyncSpotDetailActivity.getReviewRating(position);
                String reviewContent = asyncSpotDetailActivity.getReviewContent(position);
                String spotName = asyncSpotDetailActivity.getSpotName();

                // インテントにセット
                intent.putExtra("reviewImage",reviewImage);
                intent.putExtra("reviewRating", reviewRating);
                intent.putExtra("reviewContent", reviewContent);
                intent.putExtra("spotName",spotName);
                // Activity をスイッチする
                startActivity(intent);

                //今回は、トースト表示
                //ListView listView =(ListView)parent;
                //String item=(String)listView.getItemAtPosition(position);
                //Toast.makeText(DispSpotDetailActivity.this, "Click: "+item, Toast.LENGTH_SHORT).show();
            }
        });

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

