package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.HistoryAdapter;

import static android.R.id.list;

/*
    In this fragment we only setup Recycler view to view data from "History" table,as you can see it from adapter
 */
public class HistoryFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private HistoryAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.history_fragment, container, false);
        searchView = (SearchView)view.findViewById(R.id.serchview_settings);
        rv         = (RecyclerView)view.findViewById(R.id.recycler);
        manager    = new LinearLayoutManager(getActivity());
        db         = new DataBaseSQLite(getActivity().getApplicationContext());

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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                final int last = adapter.getItemCount();

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //alert for confirm to delete
                    builder.setMessage("Вы уверены,что хотите удалить слово из истории?");    //set message

                    builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(Constants.TAG,"POSITION :"+position);
                            Log.d(Constants.TAG,"LAST :"+last);
                            Log.d(Constants.TAG,"BEFORE :"+db.getWordsFromHistoryTable().toString());
                            db.deleteHistoryItem(adapter.getWord(position),adapter.getTrans(position),adapter.getDirs(position));
                            Log.d(Constants.TAG,"AFTER :"+db.getWordsFromHistoryTable().toString());
                            adapter.deleteItem(position);
                            adapter.notifyItemRemoved(position-1);    //item removed from recylcerview
                            return;
                        }
                    }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv); //set swipe to recylcerview


        rv.setLayoutManager(manager);
        adapter = new HistoryAdapter(
                getActivity(),
                db.getWordsFromHistoryTable(),
                db.getTranslatesFromHistoryTable(),
                db.getDirsFromHistoryTable()
        );
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        return view;
    }

    public void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Найти в истории");
    }
}
