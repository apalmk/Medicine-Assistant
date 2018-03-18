package com.example.anjaniprasad.medicine;

/**
 * Created by ANJANIPRASAD on 3/11/2018.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFileAsync extends AsyncTask<AsyncParams, String,  String>{

    private Exception exception;

    protected String doInBackground(AsyncParams... asyncParams) {

        try {
            return put(asyncParams[0].url, asyncParams[0].file);
        } catch (Exception e) {
            this.exception = e ;
        }

        return "no response" ;
    }

    protected void onPostExecute(String response) {

    }

    public static String put(String urls, File file) throws Exception {

        String BOUNDRY = "==================================";
        HttpURLConnection conn = null;

        try {

            // These strings are sent in the request body. They provide information about the file being uploaded
            String contentDisposition = "Content-Disposition: form-data; name=\"userfile\"; image=\"" + file.getName() + "\"";
            String contentType = "Content-Type: application/octet-stream";


            // This is the standard format for a multipart request
            StringBuffer requestBody = new StringBuffer();
            requestBody.append("--");
            requestBody.append(BOUNDRY);
            requestBody.append('\n');
            requestBody.append(contentDisposition);
            requestBody.append('\n');
            requestBody.append(contentType);
            requestBody.append('\n');
            requestBody.append('\n');
            requestBody.append(new String(GetBytesFromFile.getBytesFromFile(file)));
            requestBody.append("--");
            requestBody.append(BOUNDRY);
            requestBody.append("--");

            // Make a connect to the server
            URL url = new URL(urls);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDRY);

            // Send the body
            DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
            dataOS.writeBytes(requestBody.toString());
            dataOS.flush();
            dataOS.close();

            // Ensure we got the HTTP 200 response code
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new Exception(String.format("Received the response code %d from the URL %s", responseCode, url));
            }

            // Read the response
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int bytesRead;
            while((bytesRead = is.read(bytes)) != -1) {
                baos.write(bytes, 0, bytesRead);
            }
            byte[] bytesReceived = baos.toByteArray();
            baos.close();

            is.close();
            String response = new String(bytesReceived);
            Log.i("TAG", "RESPONSEEE: " + response);
            return response ;
            // TODO: Do something here to handle the 'response' string

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }
}


