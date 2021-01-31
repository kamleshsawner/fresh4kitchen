package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import singa.tech.fresh4kitchen.R;
import static singa.tech.fresh4kitchen.util.Constant.SPLASH_TIME;

public class SplashActivity extends AppCompatActivity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ctx, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME);
    }
}
