package com.example.vlad.mytranslatorwithyandex_v101.Interfaces;

import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.ServerResponse.getLangsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface AllLanguagesService {
    @GET("getLangs")
    Call<getLangsResponse> makeAllLanguagesRequest(
            @QueryMap Map<String, String> options
    );
}
