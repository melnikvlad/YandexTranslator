package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView trans,def,pos;
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
        trans = (TextView)view.findViewById(R.id.translate);
        def = (TextView)view.findViewById(R.id.def);
        pos = (TextView)view.findViewById(R.id.pos);
        input_field = (EditText) view.findViewById(R.id.inputfield);
        translate = (Button) view.findViewById(R.id.btn_tanslate);
        rv = (RecyclerView)view.findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(getActivity());


        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = input_field.getText().toString();
                Translate(text);  // DO ALL STUFF
            }
        });

        return view;
    }
//====================================================== MAIN TRANSLATE AND LOOKUP METHOD ==================================================================================
    private void Translate(String text) {

        Retrofit retrofitTR = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
               .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitLK = new Retrofit.Builder().baseUrl(Constants.BASE_URL2)   // Lookup
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslateService tr_service = retrofitTR.create(TranslateService.class); // Translate sevice
        LookupService lookup_service = retrofitLK.create(LookupService.class); //   Lookup sevice

        final Call<TranslaterResponse> CallToTranslate = tr_service.makeTranslateRequest(getTranslateParams());
        final Call<LookupResponse> CallToLookup = lookup_service.makeLookupRequest(getLookupParams());

        CallToTranslate.enqueue(new Callback<TranslaterResponse>() {   // Translate call
            @Override
            public void onResponse(Call<TranslaterResponse> call, Response<TranslaterResponse> response) {

                TranslaterResponse translate_response = response.body(); // Translate response
                // Lets cut square braces off and view normal text
                trans.setText(translate_response.getText().toString().substring(1,translate_response.getText().toString().length()-1));
            }

            @Override
            public void onFailure(Call<TranslaterResponse> call, Throwable t) {
                Log.d(Constants.TAG, "translate failure" + t.toString());
            }
        });

        CallToLookup.enqueue(new Callback<LookupResponse>() { // Lookup call
            @Override
            public void onResponse(Call<LookupResponse> call, Response<LookupResponse> response) {

                LookupResponse lookup_response = response.body();  // Lookup response
                if(lookup_response.getDef().isEmpty()){ //if Lookup method cant find any sugestions to request text,then need to hide fields to  error messages
                    rv.setVisibility(View.GONE);
                    def.setVisibility(View.GONE);
                    pos.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Cant find translation", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    rv.setVisibility(View.VISIBLE);
                    def.setVisibility(View.VISIBLE);
                    pos.setVisibility(View.VISIBLE);
                    def.setText(lookup_response.getDef().get(0).getText().toString());
                    pos.setText(lookup_response.getDef().get(0).getPos().toString());
                    rv.setLayoutManager(manager); // View in Recycler View
                    adapter = new LookupAdapter(
                            getActivity().getApplication(),
                            RV_top_items_row(lookup_response),
                            RV_bot_items_row(lookup_response));
                    rv.setAdapter(adapter);
                            //________________________
                            // Definition + Synonyms  |
                            // Meaning                | <-- Single Recycler View Item
                            //________________________|
                }
            }

            @Override
            public void onFailure(Call<LookupResponse> call, Throwable t) {
                Log.d(Constants.TAG, "lookup failure" + t.toString());
            }
        });

    }
//=========================================================================================================================================================

    private int getDefSize(LookupResponse response){ // How many Def[] arrays ?
        return response.getDef().size();
    }

    private List<List<Tr>> getTrLists(LookupResponse response){ // Get all Tr[] from every Def[] array
        List<List<Tr>> list = new ArrayList<>();
        for(int i=0;i<getDefSize(response);i++){
            list.add(response.getDef().get(i).getTr());
        }
        return list;
    }

    private int getTrListsSize(List<List<Tr>> lists){ // How many objects in every Tr[] array
        int size =0;
        for(int i=0;i<lists.size();i++){
            size+= lists.get(i).size();
        }
        return size;
    }

    private List<Tr> getTrListsObjects(List<List<Tr>> lists){ // Get all Tr{} objects from every Tr[] array
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

    private List<Tr> getTranslates(LookupResponse response){  // Get list of all translates
        int defSize = 0;
        int trSize = 0;
        List<List<Tr>> trList = new ArrayList<>();
        List<Tr> tr_i = new ArrayList<>();
        List<String> rv_rowTop_i = new ArrayList<>();

        defSize = getDefSize(response);
        Log.d(Constants.TAG,"defSize :"+ defSize);
        trList = getTrLists(response);
        Log.d(Constants.TAG,"trList :"+ trList.toString());
        trSize = getTrListsSize(trList);
        Log.d(Constants.TAG,"trSize :"+trSize);
        tr_i = getTrListsObjects(trList);
        Log.d(Constants.TAG,"tr_i :"+tr_i);
        return tr_i;

    }

    private String listToString(List<String> list){  // Turn list items to single textline
        String text="";
        for(int i=0;i<list.size();i++){
            if(i == list.size()-1){
                text+=list.get(i).toString();
            }
            else
            {
                text+=list.get(i).toString()+", ";
            }

        }
        return text;
    }


    private List<String> RV_top_items_row(LookupResponse response){// Inflate cardview top row for Recycler View adapter

        List<Tr> tr_i;
        List<String> rv_rowTop_i = new ArrayList<>();
        List<String> pos_i = new ArrayList<>();
        List<String> gen_i = new ArrayList<>();
        List<String> syn_i = new ArrayList<>();
        String rowTop="";

        tr_i = getTranslates(response); // All translates

        for (int i=0;i<tr_i.size();i++) { // Loop through all translates

            if(tr_i.get(i).getPos() != null) { // if pos field exists --> add it to  pos list
                pos_i.add(tr_i.get(i).getPos());
            }
            else {
                pos_i.add(""); // else make it empty

            }
            if(tr_i.get(i).getGen() != null) {  // if gen field exists --> add it to gen list
                gen_i.add(tr_i.get(i).getGen());
            }
            else {
                gen_i.add("");  // else make it empty
            }

            if(tr_i.get(i).getSyn()!= null) { // if syn array exists --> add it to syn list

                for (int j=0;j<tr_i.get(i).getSyn().size();j++){
                    syn_i.add(tr_i.get(i).getSyn().get(j).getText());
                }
            }
            else {

                syn_i.add("");  // else make it empty
            }
            Log.d(Constants.TAG,"=====================================ITERATION :"+i+"==========================================================");
            String listtoString = listToString(syn_i);
            if(!listtoString.isEmpty()){        // Format string
                rowTop = tr_i.get(i).getText()+", " + listtoString;
            }
            else
            {
                rowTop = tr_i.get(i).getText();
            }

            Log.d(Constants.TAG,"rowTop :"+ rowTop);
            rv_rowTop_i.add(rowTop);// top row would be like : Translate + Synonyms
            syn_i.clear();
        }

        return rv_rowTop_i;
    }

    private List<String> RV_bot_items_row(LookupResponse response){ // Inflate cardview bottom row for Recycler View adapter
        String rowBot = "";
        List<Tr> tr_i;
        List<String> rv_rowBot_i = new ArrayList<>();
        List<String> mean_i= new ArrayList<>();


        tr_i = getTranslates(response); // All translates

        for (int i=0;i<tr_i.size();i++) { // if mean array exists --> add it to mean list

            if(tr_i.get(i).getMean()!= null) {
                for (int j=0;j<tr_i.get(i).getMean().size();j++){
                    mean_i.add(tr_i.get(i).getMean().get(j).getText());
                }
            }
            else {

                mean_i.add(""); // else make it empty
            }

            Log.d(Constants.TAG,"=====================================ITERATION :"+i+"==========================================================");
            String listtoString = listToString(mean_i);
            if(!listtoString.isEmpty()) // Format string
            {
                rowBot = "("+listToString(mean_i)+")";
            }
            else{
                rowBot="";
            }

            Log.d(Constants.TAG,"rowBot :"+ rowBot);
            rv_rowBot_i.add(rowBot); // bottom row would be like : Means
            mean_i.clear();
        }
        return rv_rowBot_i;
    }

    private Map<String,String> getTranslateParams() { // Params for Translate retrofit request
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("lang", Constants.EN_RU);
        params.put("ui", Constants.UI);
        params.put("text", text);
        return params;
    }

    private Map<String,String> getLookupParams() {  // Params for Lookup retrofit request
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
        params.put("lang", Constants.EN_RU);
        params.put("ui", Constants.UI);
        params.put("text", text);
        return params;
    }
}


