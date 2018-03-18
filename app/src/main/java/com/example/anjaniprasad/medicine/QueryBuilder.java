package com.example.anjaniprasad.medicine;

/**
 * Created by ANJANIPRASAD on 3/11/2018.
 */

public class QueryBuilder {


    public String getDatabaseName() {
        return "apps";
    }


    public String getApiKey() {
        return "FIw3R9TO9oAafLrPRw-JDU8GpnaH9Cjo";
    }

    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }


    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }


    public String documentRequest()
    {
        return "feedback";
    }


    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }


    public String createDetails(Feed_data data)
    {
        return String
                .format("{\"document\" : {\"feed\": \"%s\"}, \"safe\" : true}",
                        data.feed);
    }


}
