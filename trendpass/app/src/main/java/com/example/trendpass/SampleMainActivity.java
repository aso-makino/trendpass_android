package com.example.trendpass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trendpass.async.SampleAsyncActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class SampleMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);



    }

    protected void onResume() {
        super.onResume();

        Button sendBtn = (Button) findViewById(R.id.button);
        sendBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                EditText eText = (EditText) findViewById(R.id.etword);
                String word = eText.getText().toString();

                    try {
                        String ip= getString(R.string.ip);
                        new SampleAsyncActivity(SampleMainActivity.this)
                            .execute(new URL("http://"+ip+":8080/trendpass/SampleServlet?name=" + word));

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
        });
    }
}
