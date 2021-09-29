package com.clickers.googletwitter;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dharak Bhatt on 29-Sep-2021.
 */
public class APIMethods {
    static String BASE_URL = "https://api.twitter.com/";

    private static RequestRetrofit apiService;

    private static RequestRetrofit getRetrofitService() {
        if (apiService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(RequestRetrofit.class);
        }

        return apiService;
    }

    /*static void getData(String url){
        Call<ResponseBody> call = getRetrofitService().GetData(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        BufferedSource source = response.body().source();
                        Buffer buffer = new Buffer();

                        try {
                        while (!source.exhausted()) {
                            response.body().source().read(buffer, 8192);
                            String data = buffer.readString(Charset.defaultCharset());

                            System.out.println(data);

                            try {
                                Gson gson = new Gson();
                                TweetPlaces tweetPlaces = gson.fromJson(data, TweetPlaces.class);

                                if (tweetPlaces.includes.places.size() > 0) {
                                    Double lat = tweetPlaces.includes.places.get(0).geo.bbox.get(0);
                                    Double lng = tweetPlaces.includes.places.get(0).geo.bbox.get(1);
                                }

                                System.out.println(tweetPlaces.data.text);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }catch (IOException ioException){
                        ioException.printStackTrace();
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }*/
}
