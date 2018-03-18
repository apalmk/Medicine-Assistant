package com.example.anjaniprasad.medicine;

/**
 * Created by ANJANIPRASAD on 3/11/2018.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
/**
 * Async Task to retrieve your stored contacts from mongolab
 * @author KYAZZE MICHAEL
 *
 */
public class AsyncFetch extends AsyncTask<Prescription, Void, ArrayList<Prescription>> {
    static BasicDBObject user = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;

    @Override
    protected ArrayList<Prescription> doInBackground(Prescription... arg0) {

        ArrayList<Prescription> pres = new ArrayList<Prescription>();
        try
        {

            QueryBuilder1 qb = new QueryBuilder1();
            URL url = new URL(qb.buildContactsGetURL());
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

// create a basic db list
            String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);

            DBObject dbObj = (DBObject) o;
            BasicDBList prescription = (BasicDBList) dbObj.get("artificial_basicdb_list");

            for (Object obj : pres) {
                DBObject userObj = (DBObject) obj;

                Prescription temp = new Prescription();
                temp.setName(userObj.get("name").toString());
                temp.setCount(userObj.get("count").toString());
                pres.add(temp);

            }

        }catch (Exception e) {
            Log.e("imp",e.getMessage());
        }

        return pres;
    }


}