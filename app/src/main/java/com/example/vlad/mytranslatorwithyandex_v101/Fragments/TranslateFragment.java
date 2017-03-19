package com.example.vlad.mytranslatorwithyandex_v101.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LookupService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.TranslateService;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.LookupResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.Tr;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.TranslaterResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.LookupAdapter;


public class TranslateFragment extends Fragment {
    private EditText input_field;
    private Button translate;
    private String text ="" ;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.translate_fragment, container, false);
        input_field = (EditText) view.findViewById(R.id.inputfield);
        translate = (Button) view.findViewById(R.id.btn_tanslate);
        rv = (RecyclerView)view.findViewById(R.id.recycler_view);

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
                
                manager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(manager);
                adapter = new LookupAdapter(getActivity().getApplication(),RV_data_list(lookup_response));
                rv.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<LookupResponse> call, Throwable t) {
                Log.d(Constants.TAG, "lookup failure" + t.toString());
            }
        });

    }

    private int getDefSize(LookupResponse response){
        return response.getDef().size();
    }

    private List<List<Tr>> getTrLists(LookupResponse response){
        List<List<Tr>> list = new ArrayList<>();
        for(int i=0;i<getDefSize(response);i++){
            list.add(response.getDef().get(i).getTr());
        }
        return list;
    }

    private int getTrListsSize(List<List<Tr>> lists){
        int size =0;
        for(int i=0;i<lists.size();i++){
            size+= lists.get(i).size();
        }
        return size;
    }

    private List<Tr> getTrListsObjects(List<List<Tr>> lists){
        List<Tr> objs = new ArrayList<>();
        for(int i=0;i<lists.size();i++)
        {
            for (int j=0;j<lists.get(i).size();j++)
            {
                objs.add(lists.get(i).get(j));
            }
        }
        return objs;
    }

    private List<String> RV_data_list(LookupResponse response){
        String rowTop = "";
        String rowBot = "";
        int defSize = 0;
        int trSize = 0;
        List<List<Tr>> trList = new ArrayList<>();
        List<Tr> tr_i = new ArrayList<>();
        List<String> rv_rowTop_i = new ArrayList<>();
        List<String> rv_rowBot_i = new ArrayList<>();
        List<String> pos_i = new ArrayList<>();
        List<String> gen_i = new ArrayList<>();
        List<String> mean_i= new ArrayList<>();
        List<String> syn_i = new ArrayList<>();


        defSize = getDefSize(response);
        Log.d(Constants.TAG,"defSize :"+ defSize);
        trList = getTrLists(response);
        Log.d(Constants.TAG,"trList :"+ trList.toString());
        trSize = getTrListsSize(trList);
        Log.d(Constants.TAG,"trSize :"+trSize);
        tr_i = getTrListsObjects(trList);
        Log.d(Constants.TAG,"tr_i :"+tr_i);



        for (int i=0;i<tr_i.size();i++) {

            if(tr_i.get(i).getPos() != null) {
                pos_i.add(tr_i.get(i).getPos());
            }
            else {
                pos_i.add("");

            }
            if(tr_i.get(i).getGen() != null) {
                gen_i.add(tr_i.get(i).getGen());
            }
            else {
                gen_i.add("");
            }

            if(tr_i.get(i).getMean()!= null) {
                for (int j=0;j<tr_i.get(i).getMean().size();j++){
                    mean_i.add(tr_i.get(i).getMean().get(j).getText());
                }
            }
            else {

                mean_i.add("");
            }

            if(tr_i.get(i).getSyn()!= null) {

                for (int j=0;j<tr_i.get(i).getSyn().size();j++){
                    syn_i.add(tr_i.get(i).getSyn().get(j).getText());
                }
            }
            else {

                syn_i.add("");
            }
            Log.d(Constants.TAG,"=====================================ITERATION :"+i+"==========================================================");

            rowTop = tr_i.get(i).getText()+" " + listToString(syn_i);
            Log.d(Constants.TAG,"rowTop :"+ rowTop);
            rowBot = listToString(mean_i);
            Log.d(Constants.TAG,"rowBot :"+ rowBot);
            Log.d(Constants.TAG,"===============================================================================================");
            rv_rowTop_i.add(rowTop);
            rv_rowBot_i.add(rowBot);
            mean_i.clear();
            syn_i.clear();
        }
        for(int i =0;i<rv_rowTop_i.size();i++){
            Log.d(Constants.TAG,"LIST TOP ITEMS: "+ rv_rowBot_i.get(i).toString());
        }
        return rv_rowTop_i;
    }

    private String listToString(List<String> list){
        String text="";
        for(int i=0;i<list.size();i++){
            text+=list.get(i).toString()+" ";
        }
        return text;
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


