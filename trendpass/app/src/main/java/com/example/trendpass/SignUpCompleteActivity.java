package com.example.trendpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trendpass.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

public class SignUpCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_complete);

        //ロゴ
        Picasso.with(this.getApplicationContext())
                .load(R.drawable.rogo)
                .resize(500,500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.rogoImg));

        Intent intent = getIntent();
        final String msg = intent.getStringExtra("msg");

        TextView msgtv = findViewById(R.id.complete);
        msgtv.setText(msg);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick(View view) {

            Intent intent = new Intent(SignUpCompleteActivity.this, LoginActivity.class);

            startActivity(intent);
        }


    });
    }
}
