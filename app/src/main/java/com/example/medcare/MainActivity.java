package com.example.medcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_TIMEOUT = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
//                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
