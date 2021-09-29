package com.clickers.googletwitter;

import android.util.Log;

import com.github.simonpercic.oklog3.OkLogInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class ApiClient {

    static String bearerToken = "AAAAAAAAAAAAAAAAAAAAALemTwEAAAAAs1iHyIvdS%2Bgpuwx%2BbUBJrwZzldQ%3DFRkvhuGnVCqNu1CB1rxZYZaeJd6Zt2FgnWjN1pVSAbR9cY6BRm";


    private static GitApiInterface gitApiInterface;
    static String BASE_URL = "https://api.twitter.com/";

    public static GitApiInterface getClient() {
        if (gitApiInterface == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws
                                IOException {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Authorization", String.format("Bearer %s", bearerToken))
                                    .build();
                            return chain.proceed(request);
                        }


                    })
                    .addInterceptor(interceptor)
                    .build();

          /*  OkLogInterceptor okLogInterceptor = OkLogInterceptor.builder().build();

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
            okHttpBuilder.connectTimeout(20L, TimeUnit.SECONDS);
            okHttpBuilder.readTimeout(20L, TimeUnit.SECONDS).build();
            okHttpBuilder.writeTimeout(20L, TimeUnit.SECONDS);
            okHttpBuilder.addInterceptor((Interceptor)okLogInterceptor);
            OkHttpClient okHttpClient = okHttpBuilder.build();*/
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            gitApiInterface = client.create(GitApiInterface.class);
        }
        return gitApiInterface;
    }

    public interface GitApiInterface {



        @Headers("Content-Type: application/json")
        @GET("2/tweets/search/stream")
        Call<String> getTwitt(@Query("expansions") String expansions,
                                    @Query("tweet.fields") String tweet, @Query("place.fields") String places);


    }

}
