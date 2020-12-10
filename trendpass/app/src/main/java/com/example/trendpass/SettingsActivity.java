package com.example.trendpass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Picasso.with(this)
                .load(R.drawable.settings_close)
                .fit()
                .centerInside()
                .into((ImageView) findViewById(R.id.closeBtn));

    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageButton closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout logoutRow = findViewById(R.id.logoutRow);
        logoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 画面遷移時に新しいタスクを生成し、現在のタスクを破棄する
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // ログイン情報破棄
                SharedPreferences loginData = getSharedPreferences("login_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = loginData.edit();
                editor.clear();
            }
        });

        LinearLayout signchangeRow = findViewById(R.id.signchangeRow);
        signchangeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SignChangeActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout unsubscribeRow = findViewById(R.id.unsubscribeRow);
        unsubscribeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ConfirmUnsubscribeActivity.class);
                startActivity(intent);
            }
        });
    }
}
