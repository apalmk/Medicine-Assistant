package com.example.anjaniprasad.medicine;

/**
 * Created by ANJANIPRASAD on 3/11/2018.
 */

public class QueryBuilder1 {
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
        return "prescription";
    }


    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

    public String buildContactsGetURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    public String buildContactsUpdateURL(String doc_id)
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl(doc_id);
    }
//    public String createDetails(Feed_data data)
//    {
//        return String
//                .format("{\"document\" : {\"feed\": \"%s\"}, \"safe\" : true}",
//                        data.feed);
//    }

    public String setContactData(Prescription p) {
        return String.format("{\"document\" : {\"name\": \"%s\", "
                        + "\"count\": \"%s\"}, \"safe\" : true}",
                p.name, p.count);
    }
}
