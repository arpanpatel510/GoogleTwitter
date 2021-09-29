package com.clickers.googletwitter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import okhttp3.ResponseBody;

public class MainActivityViewModel extends ViewModel {
    MutableLiveData<ResponseBody> servicesLiveData;


   public LiveData<ResponseBody> getData(String url){
       servicesLiveData = MainActivityRepository.getApiCall(url);
       return servicesLiveData;
   }



}
