package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.Constant;

public class RegisterSuccessfullyActivity extends AppCompatActivity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ctx = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAffinity();
                startActivity(new Intent(ctx, MainActivity.class));
            }
        }, Constant.SPLASH_TIME);
    }
}
