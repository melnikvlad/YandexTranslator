package com.example.vlad.mytranslatorwithyandex_v101.Interfaces;

import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.LookupResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface LookupService {
    @GET("lookup")
    Call<LookupResponse> makeLookupRequest(
            @QueryMap Map<String, String> options
    );
}
