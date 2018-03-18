package com.example.anjaniprasad.medicine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PreviewPhotoActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener{

    String fileName ;
    File imgFile ;

    private TextToSpeech tts;

    private Button btnUpload;

    private PreviewPhotoActivity previewPhotoActivity ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_photo);

        previewPhotoActivity = this ;

        if (getIntent().hasExtra("fileName")){
            fileName = getIntent().getExtras().getString("fileName", "no file") ;

            imgFile = new File(fileName);

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = findViewById(R.id.imageView_preview);
                myImage.setImageBitmap(myBitmap);
            }
        }


        tts = new TextToSpeech(this, this);
        btnUpload = findViewById(R.id.btn_upload) ;
        btnUpload.setOnClickListener(this);
        btnUpload.setEnabled(false);


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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnUpload.setEnabled(true);
                Log.i("TAG", "onInit: Button enabled");

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_upload){
            tts.speak("This image is being processed, please wait.", TextToSpeech.QUEUE_FLUSH, null);

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //uploadBitmap(bitmap);
            try {
                String uploadURL = "http://sleepy-fjord-33963.herokuapp.com/ocr" ;

                //AsyncParams asyncParams = new AsyncParams(uploadURL, imgFile) ;
                //new UploadFileAsync().execute(asyncParams);
                //put(uploadURL, imgFile);
                imageUpload(imgFile.getAbsolutePath());
            } catch (Exception e) {
                Log.e("TAG", "error" + e.getLocalizedMessage() );
                e.printStackTrace();
            }

        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        String uploadURL = "http://sleepy-fjord-33963.herokuapp.com/ocr" ;

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadURL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.i("TAG", "onResponse: " + response.data);
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Log.i("TAG", "uploadBitmap: ");
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void imageUpload(final String imagePath) {

        String uploadURL = "http://sleepy-fjord-33963.herokuapp.com/ocr" ;

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, uploadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("message");

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(previewPhotoActivity, ResultActivity.class) ;
                            intent.putExtra("response", response) ;
                            startActivity(intent);

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addFile("image", imagePath);
        AppController.getInstance().addToRequestQueue(smr);

    }


}
