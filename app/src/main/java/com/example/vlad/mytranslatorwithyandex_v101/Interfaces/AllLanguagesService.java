package com.example.vlad.mytranslatorwithyandex_v101.Interfaces;

import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.AllLanguagesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface AllLanguagesService {
    @GET("getLangs")
    Call<AllLanguagesResponse> makeAllLanguagesRequest(
            @QueryMap Map<String, String> options
    );
}
