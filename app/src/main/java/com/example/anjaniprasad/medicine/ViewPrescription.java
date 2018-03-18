package com.example.anjaniprasad.medicine;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewPrescription extends AppCompatActivity {
    ArrayList<Prescription> returnValues = new ArrayList<Prescription>();
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);
        tts = new TextToSpeech(this,null);
        AsyncFetch task = new AsyncFetch();
        try {
            returnValues = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(Prescription x: returnValues){

            String name,count,sen;
            name= x.getName();
            count= x.getCount();
            sen="The medicine "+name+" has to be taken "+count+" times";
            tts.speak(sen, TextToSpeech.QUEUE_FLUSH, null);
            System.out.println(sen);
        }


    }



    public void go(View view)
    {
        Intent myIntent = new Intent(ViewPrescription.this, Main2Activity.class);
        startActivity(myIntent);
    }

}

