package com.clickers.googletwitter;

import static com.clickers.googletwitter.ApiClient.bearerToken;

import android.media.session.MediaSession;

import com.github.simonpercic.oklog3.OkLogInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RequestRetrofit {

    @Headers("Authorization: Bearer AAAAAAAAAAAAAAAAAAAAAJqtTwEAAAAABLpiu1M4%2FonQfyVTb%2BWvCuOjCFE%3DFMyk8yNRRxXguXS8eWyPzTyOWRg49ARSpGXDU3LDmY1GCR2bXI")
    @GET()
    Call<ResponseBody> GetData(@Url String s);
}
