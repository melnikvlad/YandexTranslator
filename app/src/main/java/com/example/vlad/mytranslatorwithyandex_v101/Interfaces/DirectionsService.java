package com.example.vlad.mytranslatorwithyandex_v101.Interfaces;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DirectionsService {
    @GET("getLangs")
    Call<List<String>> getDirs(
            @QueryMap Map<String, String> options
    );
}
