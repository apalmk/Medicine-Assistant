package com.example.anjaniprasad.medicine;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Feedback extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private TextToSpeech tts;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tts = new TextToSpeech(this,null);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.feed);
        mp.start();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Feedback.this, Main2Activity.class);
                startActivity(myIntent);
            }
        });
    }

    public void butcl(View view) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.start);
        mp.start();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //result.get(0);

                    Toast.makeText(this, result.get(0),Toast.LENGTH_LONG).show();

                    tts.speak("Your Feedback has successfully been uploaded, click on the bottom right corner to go back", TextToSpeech.QUEUE_FLUSH, null);
                    Feed_data f= new Feed_data();
//                    String listString = "";
//                    for (String s : result)
//                    {
//                        listString += s + "\t";
//                    }
                    f.feed=result.get(0);
                    SaveAsyncTask tsk = new SaveAsyncTask();
                    tsk.execute(f);
                }
                break;
            }

        }
    }
}
