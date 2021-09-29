package com.clickers.googletwitter;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Handler handler;

    // FOR print a log
    private static final String TAG = "MapsActivity";
    ProgressDialog dialog;

    ArrayList<TwitterModel> markersArray;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // on below line we are adding our
        // locations in our array list.

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        dialog = ProgressDialog.show(MapsActivity.this, "",
                "Featching Tweet location wait...", true);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        textView = findViewById(R.id.textView);

        handler = new Handler(getMainLooper());

        //  String bearerToken = System.getenv("AAAAAAAAAAAAAAAAAAAAALemTwEAAAAAs1iHyIvdS%2Bgpuwx%2BbUBJrwZzldQ%3DFRkvhuGnVCqNu1CB1rxZYZaeJd6Zt2FgnWjN1pVSAbR9cY6BRm");
        //   String bearerToken = "AAAAAAAAAAAAAAAAAAAAALemTwEAAAAAs1iHyIvdS%2Bgpuwx%2BbUBJrwZzldQ%3DFRkvhuGnVCqNu1CB1rxZYZaeJd6Zt2FgnWjN1pVSAbR9cY6BRm";
        String bearerToken = "AAAAAAAAAAAAAAAAAAAAAJqtTwEAAAAABLpiu1M4%2FonQfyVTb%2BWvCuOjCFE%3DFMyk8yNRRxXguXS8eWyPzTyOWRg49ARSpGXDU3LDmY1GCR2bXI";
        // frd key
        //  String bearerToken = "AAAAAAAAAAAAAAAAAAAAADX9TQEAAAAAg3vRA4X0eA2tOOvHVektmrQ4kBQ%3Di6ey7uzMCTSmcjWYGzcjpaScVvOD3ZWTwIYCvwDHCQEY852QzR";
        System.out.println("bearerToken" + "      " + bearerToken);


        if (null != bearerToken) {
            System.out.println("bearerToken1" + "      " + bearerToken);

            Map<String, String> rules = new HashMap<>();
            rules.put("covid-19", "covid 19 geo location");
            rules.put("museum", "museum has geo");
            rules.put("cat has:images", "cat has images");
            try {
                System.out.println("bearerToken11" + "      " + bearerToken + "     " + rules);

                setupRules(bearerToken, rules);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connectStream(bearerToken);
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("bearerToken2" + "      " + bearerToken);

            System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        addMarkers();
    }

    private void addMarker(LatLng latLng, String fullName) {
        dialog.dismiss();
        if (mMap == null)
            return;
        mMap.addMarker(new MarkerOptions().position(latLng).title(fullName));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(1.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    private void connectStream(String bearerToken) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets/search/stream?expansions=geo.place_id&tweet.fields=geo&place.fields=geo")
                .header("authorization", "Bearer " + bearerToken)
                .build();

        try {
            Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                BufferedSource source = responseBody.source();
                Buffer buffer = new Buffer();

                while (!source.exhausted()) {
//                    totalCounter++;
                    responseBody.source().read(buffer, 8192);
                    String data = buffer.readString(Charset.defaultCharset());

                    System.out.println(data);

                    try {
//                        tweetPlacesCounter++;
                        Gson gson = new Gson();
                        TweetPlaces tweetPlaces = gson.fromJson(data, TweetPlaces.class);

                        if (tweetPlaces.includes.places.size() > 0) {
                            Double lat = tweetPlaces.includes.places.get(0).geo.bbox.get(0);
                            Double lng = tweetPlaces.includes.places.get(0).geo.bbox.get(1);
//                            placeCounter++;
                            Log.d("LatLng --- ", String.format(Locale.ENGLISH, "%f, %f", lat, lng));
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    addMarker(new LatLng(lat, lng), tweetPlaces.includes.places.get(0).fullName);
//                                    textView.setText(
//                                            String.format(
//                                                    Locale.ENGLISH,
//                                                    "Latitude : %f, \nLongitude : %f \nCount : %d \nTotalCount : %d \nTweetPlacesCount : %d \nErrorCount : %d",
//                                                    lat, lng, placeCounter, totalCounter, tweetPlacesCounter, errorCounter));
                                }
                            });
                        }

//                        System.out.println(tweetPlaces.data.text);
                    } catch (Exception e) {
//                        errorCounter++;
                        e.printStackTrace();
                    }

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        String str_resp = "2/tweets/search/stream?expansions=geo.place_id&tweet.fields=geo&place.fields=geo";


        /*mainActivityViewModel.getData(str_resp).observe((LifecycleOwner) this, apiResponse -> {
            if (apiResponse != null) {
                try {
                    parseJsonResponse(apiResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/


    }

    /*
     * Helper method to setup rules before streaming data
     * */
    private static void setupRules(String bearerToken, Map<String, String> rules) throws IOException, URISyntaxException, JSONException {

        List<String> existingRules = getRules(bearerToken);
        if (existingRules.size() > 0) {
            deleteRules(bearerToken, existingRules);
        }
        createRules(bearerToken, rules);
    }

    /*
     * Helper method to create rules for filtering
     * */
    private static void createRules(String bearerToken, Map<String, String> rules) throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpPost.setHeader("content-type", "application/json");
        StringEntity body = new StringEntity(getFormattedString("{\"add\": [%s]}", rules));
        httpPost.setEntity(body);
        HttpResponse response = httpClient.execute(httpPost);
        // System.out.println("response"+response);

        HttpEntity entity = response.getEntity();
        if (null != entity) {
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        }


    }

    /*
     * Helper method to get existing rules
     * */
    private static List<String> getRules(String bearerToken) throws URISyntaxException, IOException, JSONException {
        List<String> rules = new ArrayList<>();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("content-type", "application/json");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            JSONObject json = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
            if (json.length() > 1) {
                JSONArray array = (JSONArray) json.get("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = (JSONObject) array.get(i);
                    rules.add(jsonObject.getString("id"));
                }
            }
        }
        return rules;
    }

    /*
     * Helper method to delete rules
     * */
    private static void deleteRules(String bearerToken, List<String> existingRules) throws URISyntaxException, IOException {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/stream/rules");

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpPost.setHeader("content-type", "application/json");
        StringEntity body = new StringEntity(getFormattedString("{ \"delete\": { \"ids\": [%s]}}", existingRules));
        httpPost.setEntity(body);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        }
    }

    private static String getFormattedString(String string, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        if (ids.size() == 1) {
            return String.format(string, "\"" + ids.get(0) + "\"");
        } else {
            for (String id : ids) {
                sb.append("\"" + id + "\"" + ",");
            }
            String result = sb.toString();
            return String.format(string, result.substring(0, result.length() - 1));
        }
    }

    private static String getFormattedString(String string, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        if (rules.size() == 1) {
            String key = rules.keySet().iterator().next();
            return String.format(string, "{\"value\": \"" + key + "\", \"tag\": \"" + rules.get(key) + "\"}");
        } else {
            for (Map.Entry<String, String> entry : rules.entrySet()) {
                String value = entry.getKey();
                String tag = entry.getValue();
                sb.append("{\"value\": \"" + value + "\", \"tag\": \"" + tag + "\"}" + ",");
            }
            String result = sb.toString();
            return String.format(string, result.substring(0, result.length() - 1));
        }
    }

}