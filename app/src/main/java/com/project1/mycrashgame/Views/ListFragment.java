package com.project1.mycrashgame.Views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.project1.mycrashgame.Adapters.Player_Adapter;
import com.project1.mycrashgame.DataBase.DataBase;
import com.project1.mycrashgame.Interfaces.Callback_recordClicked;
import com.project1.mycrashgame.Model.Player;
import com.project1.mycrashgame.R;
import com.project1.mycrashgame.RecordsActivity;
import com.project1.mycrashgame.Utilities.SharedPreferencesManager;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    private static final String RECOREDS_LIST = "RECOREDS_LIST";
    private DataBase dataBase;
    private Callback_recordClicked callbackRecordClicked;
    private RecyclerView fragment_RECYCLER_itemList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews(getContext());
        return view;
    }


    public void setCallback(Callback_recordClicked callbackRecordClicked) {
        if (callbackRecordClicked != null)
            this.callbackRecordClicked = callbackRecordClicked;
    }

    public void initViews(Context context) {
        dataBase = new Gson().fromJson(SharedPreferencesManager.getInstance().getString(RECOREDS_LIST, ""), DataBase.class);
        Player_Adapter playerAdapter = new Player_Adapter(callbackRecordClicked,dataBase.getRecords(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fragment_RECYCLER_itemList.setLayoutManager(linearLayoutManager);
        fragment_RECYCLER_itemList.setAdapter(playerAdapter);

    }


    private void findViews(View view) {
        fragment_RECYCLER_itemList = view.findViewById(R.id.fragment_RECYCLER_itemList);

    }
}