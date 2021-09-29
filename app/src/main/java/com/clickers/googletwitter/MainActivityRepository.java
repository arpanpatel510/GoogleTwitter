package com.clickers.googletwitter;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityRepository {
    static String bearerToken = "AAAAAAAAAAAAAAAAAAAAAJqtTwEAAAAABLpiu1M4%2FonQfyVTb%2BWvCuOjCFE%3DFMyk8yNRRxXguXS8eWyPzTyOWRg49ARSpGXDU3LDmY1GCR2bXI";

    public static MutableLiveData<ResponseBody> getApiCall(String url) {
        MutableLiveData<ResponseBody> serviceSetterGetter = new MutableLiveData<>();
        CompositeDisposable mCompositeDisposable;
//        RequestRetrofit requestInterface = RequestRetrofit.Comp.create();
        mCompositeDisposable = new CompositeDisposable();

        /*mCompositeDisposable.add(requestInterface.GetData(
                url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseBody -> {
                            serviceSetterGetter.postValue(responseBody);
                        },
                        Error -> {
                            Log.d("Error----",Error.getMessage());
                        }
                ));*/


        return serviceSetterGetter;
    }


}
