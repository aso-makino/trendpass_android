package com.example.trendpass;

        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ImageView;

        import com.example.trendpass.async.AsyncMyPageReviewActivity;
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

        String userId = "1";

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

    }
}

