package com.example.vlad.mytranslatorwithyandex_v101.Interfaces;


import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.TranslaterResponse;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TranslateService {
    @GET("translate")
    Call<TranslaterResponse> makeTranslateRequest(
            @QueryMap Map<String, String> options
    );

}
