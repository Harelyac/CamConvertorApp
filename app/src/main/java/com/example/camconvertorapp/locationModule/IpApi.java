package com.example.camconvertorapp.locationModule;


import retrofit2.Call;
import retrofit2.http.GET;

public interface IpApi
{
    @GET("json")
    Call<Response> getResponse();
}
