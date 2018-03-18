package com.example.anjaniprasad.medicine;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.med1);
        mp.start();
    }
public void feed(View view)
{
    Intent myIntent = new Intent(Main2Activity.this, Feedback.class);
    startActivity(myIntent);
}

public void pres(View view)
{
    Intent myIntent = new Intent(Main2Activity.this, ViewPrescription.class);
    startActivity(myIntent);
}

    public void send(View view)
    {
        Intent myIntent = new Intent(Main2Activity.this, CameraActivity.class);
        startActivity(myIntent);
    }
}
