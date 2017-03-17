package com.example.vlad.mytranslatorwithyandex_v101.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LookupService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.TranslateService;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.Def;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.LookupResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.TranslaterResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.vlad.mytranslatorwithyandex_v101.R;


public class TranslateFragment extends Fragment {
    private EditText input_field;
    private Button translate;
    private String text ="" ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translate_fragment, container, false);
        input_field = (EditText) view.findViewById(R.id.inputfield);
        translate = (Button) view.findViewById(R.id.btn_tanslate);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = input_field.getText().toString();
                Translate(text);
            }
        });

        return view;
    }

    private void Translate(String text) {

        Retrofit retrofitTR = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitLK = new Retrofit.Builder().baseUrl(Constants.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslateService tr_service = retrofitTR.create(TranslateService.class);
        LookupService lookup_service = retrofitLK.create(LookupService.class);

        final Call<TranslaterResponse> CallToTranslate = tr_service.makeTranslateRequest(getTranslateParams());
        final Call<LookupResponse> CallToLookup = lookup_service.makeLookupRequest(getLookupParams());

        CallToTranslate.enqueue(new Callback<TranslaterResponse>() {
            @Override
            public void onResponse(Call<TranslaterResponse> call, Response<TranslaterResponse> response) {
                TranslaterResponse tr_response = response.body();
                Log.d(Constants.TAG, "TRANSLATE :" +
                        tr_response.getCode() + " " +
                        tr_response.getLang() + " " +
                        tr_response.getText().get(0));
            }

            @Override
            public void onFailure(Call<TranslaterResponse> call, Throwable t) {
                Log.d(Constants.TAG, "translate failure" + t.toString());
            }
        });

        CallToLookup.enqueue(new Callback<LookupResponse>() {
            @Override
            public void onResponse(Call<LookupResponse> call, Response<LookupResponse> response) {
                LookupResponse lookup_response = response.body();
                String check = lookup_response.getDef().get(1).getTr().get(0).getGen();
                if(check==null)
                {
                    Log.d("TAG","CHECK is empty");
                }
                else
                {
                    Log.d("TAG","CHECK is not empty");
                }
                Log.d(Constants.TAG,"RESPONSE :"+ lookup_response.getDef().size()+" ");
                Log.d(Constants.TAG, "LOOK_UP :"+
                        lookup_response.getDef().get(0).getText()+" " +
                        lookup_response.getDef().get(0).getPos()+" "+
                        lookup_response.getDef().get(0).getTr().get(0).getText()+" "+
                        lookup_response.getDef().get(0).getTr().get(0).getPos()+" "+
                        lookup_response.getDef().get(0).getTr().get(0).getSyn().get(0).getText()+" "+
                        lookup_response.getDef().get(0).getTr().get(0).getSyn().get(1).getText());
            }

            @Override
            public void onFailure(Call<LookupResponse> call, Throwable t) {
                Log.d(Constants.TAG, "lookup failure" + t.toString());
            }
        });
    }

    private Map<String,String> getTranslateParams() {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("lang", Constants.EN_RU);
        params.put("text", text);
        return params;
    }

    private Map<String,String> getLookupParams() {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
        params.put("lang", Constants.EN_RU);
        params.put("text", text);
        return params;
    }
}


