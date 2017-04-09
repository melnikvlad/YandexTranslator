package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.FavouriteAdapter;
/*
    In this fragment we only setup Recycler view to view data from "Favourite" table,as you can see it from adapter
 */
public class FavouriteFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private FavouriteAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);
        searchView          = (SearchView)view.findViewById(R.id.serchview_settings);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivity().getApplicationContext());
        sharedPreferences = getPreferences();

        setupSearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        //Delete item from History Rv by swiping on item to the left and also delete this from SQLite db
        //Also delete item info from Lookup table to avoid errors and clear cache from not existing in history additional translates
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //alert for confirm to delete
                    builder.setMessage("Вы уверены,что хотите удалить слово из избранного?");
                    builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            db.deleteFavouriteItem(adapter.getWord(position),adapter.getTrans(position),adapter.getDirs(position));
                            db.deleteFavouriteDetailItem(adapter.getWord(position),adapter.getDirs(position));
                            adapter.deleteItem(position);
                            adapter.notifyItemRemoved(position-1);    //item removed from recylcerview
                            return;
                        }
                    }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemRemoved(position + 1);
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                            return;
                        }
                    }).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv); //set swipe to recylcerview

        rv.setLayoutManager(manager);
        adapter = new FavouriteAdapter(
                getActivity(),
                db.getWordsFromFavouriteTable(),
                db.getTranslatesFromFavouriteTable(),
                db.getDirsFromFavouriteTable()
        );
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        return view;
    }
    public void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Найти в избранном");
    }

    private Context getActivityContex(){
        Context applicationContext = MainActivity.getContextOfApplication();
        return applicationContext;
    }

    private SharedPreferences getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivityContex());
        return prefs;
    }
}
