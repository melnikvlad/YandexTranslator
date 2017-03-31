package com.example.vlad.mytranslatorwithyandex_v101.Interfaces;

import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Translator_getLangsResponse.Directions.DirsResponse;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DirectionsService {
    @GET("getLangs")
    Call<DirsResponse> getDirs(
            @QueryMap Map<String, String> options
    );
}
