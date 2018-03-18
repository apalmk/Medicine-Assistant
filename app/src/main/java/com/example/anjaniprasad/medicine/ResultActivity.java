package com.example.anjaniprasad.medicine;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    String response ;

    TextView tvExpiry ;
    TextView tvName ;

    String expiryData ;
    String nameData ;

    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_pic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tts = new TextToSpeech(this, this);
        tvExpiry = findViewById(R.id.expiry_data) ;
        tvName = findViewById(R.id.name_data) ;

        if (getIntent().hasExtra("response")) {
            response = getIntent().getStringExtra("response") ;

            try {
                JSONObject jsonObject = new JSONObject(response) ;

                expiryData = jsonObject.getString("expiry") ;
                nameData = jsonObject.getString("name");


                tvExpiry.setText(expiryData);
                tvName.setText(nameData);


                try{
                    tts.speak("Your Expiry is: " + expiryData + " and your name is: "  + nameData , TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e){

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                try{
                    tts.speak("Your Expiry is: " + expiryData + " and the name is: "  + nameData , TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e){

                }
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}
