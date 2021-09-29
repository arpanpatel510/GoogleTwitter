package com.clickers.googletwitter;


import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  DemoClass {

/*
    public static void main(String args[]) throws IOException, URISyntaxException, JSONException {
        String bearerToken = System.getenv("BEARER_TOKEN");
        if (null != bearerToken) {
            Map<String, String> rules = new HashMap<>();
            rules.put("cats has:images", "cat images");
            rules.put("dogs has:images", "dog images");
            setupRules(bearerToken, rules);
            connectStream(bearerToken);
        } else {
            System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

 */


    private void setRules() {
        String url = "https://api.twitter.com/2/tweets/search/stream/rules";
        //   String url = "https://api.twitter.com/2/tweets/search/all";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                    }
                },
                error -> {
                    error.printStackTrace();
                  //  Toast.makeText(FilteredStreamActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<String, String>();
                String bearerToken= "AAAAAAAAAAAAAAAAAAAAALemTwEAAAAAs1iHyIvdS%2Bgpuwx%2BbUBJrwZzldQ%3DFRkvhuGnVCqNu1CB1rxZYZaeJd6Zt2FgnWjN1pVSAbR9cY6BRm";
                header.put("Authorization",("Bearer "+ bearerToken));
                header.put("Content-Type", "application/json");
                return header;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String,String> param = new HashMap<>();

                try {
                    JSONArray jArray = new JSONArray("add");

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject dataobj = jArray.getJSONObject(i);
                        System.out.println("dataobj"+"     "+dataobj);
                        param.put("value","sub");
                        param.put("tag","tag");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return param;
            }


        };

      //  RequestQueue requestQueue = Volley.newRequestQueue(FilteredStreamActivity.this);
     //   requestQueue.add(stringRequest);
    }


}
