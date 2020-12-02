package com.example.trendpass;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trendpass.async.AsyncSignUpActivity;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class SignUpConfirmActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 0;
    private static final int CHOSE_FILE_CODE = 1002;
    private Bitmap imgView;
    private String picturePath;//画像パス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm);

        //ロゴ
        Picasso.with(this.getApplicationContext())
                .load(R.drawable.rogo)
                .resize(500, 500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) this.findViewById(R.id.rogoImg));

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String mail = intent.getStringExtra("mail");
        final String pass = intent.getStringExtra("pass");
        final String sex = intent.getStringExtra("sex");
        final String birth = intent.getStringExtra("birth");
        final String userIcon = intent.getStringExtra("userIcon");
        final String imageView = intent.getStringExtra("image");

        TextView nameTV = findViewById(R.id.userNameTV);
        TextView mailTV = findViewById(R.id.mailTV);
        TextView passTV = findViewById(R.id.passwordTV);
        TextView sexTV = findViewById(R.id.sexTV);
        TextView birthTV = findViewById(R.id.birthTV);


        nameTV.setText(name);
        mailTV.setText(mail);
        passTV.setText(pass);
        sexTV.setText(sex);
        birthTV.setText(birth);
        Picasso.with(this.getApplicationContext())
                .load("file://"+userIcon)
                .resize(500, 500)
                .placeholder(R.drawable.noimage)
                .centerInside()
                .into((ImageView) findViewById(R.id.userIcon));


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // bitmap(Bitmap)に画像データが入っている前提
                ByteBuffer byteBuffer = ByteBuffer.allocate(imgView.getByteCount());
                imgView.copyPixelsToBuffer(byteBuffer);
                byte[] byteImage = byteBuffer.array();

                String pathName = picturePath;
                int endIndex = pathName.lastIndexOf('/'); // 最後の'/'のインデックスを検索
                String fileName = pathName.substring(endIndex + 1); //


                String ip = getString(R.string.ip);
                String postJson = "{\"userName\":\"" + name + "\",\"userMail\":\"" + mail + "\",\"password\":\"" + pass + "\",\"sex\":\"" + sex + "\",\"birth\":\"" + birth + "\"}";
                AsyncSignUpActivity asyncSignUpActivity = new AsyncSignUpActivity(SignUpConfirmActivity.this);
                asyncSignUpActivity.execute("http://" + ip + ":8080/trendpass/SignUp",byteImage, postJson,fileName);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpConfirmActivity.this, SignUpActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("mail", mail);
                intent.putExtra("pass", pass);
                intent.putExtra("sex", sex);
                intent.putExtra("birth", birth);
                intent.putExtra("userIcon", userIcon);
                startActivity(intent);
            }


        });
    }
}
