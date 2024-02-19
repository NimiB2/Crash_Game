package com.project1.mycrashgame.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project1.mycrashgame.Interfaces.Callback_recordClicked;
import com.project1.mycrashgame.R;


public class ListFragment extends Fragment {

    private RecyclerView fragment_RECYCLER_itemList;
    private Callback_recordClicked callbackRecordClicked;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        
        .setOnClickListener(v-> buttonClicked( lat , lon));


        return view;
    }


    private void buttonClicked(double lat ,double lon){
        if(callbackRecordClicked !=null){
            callbackRecordClicked.getRecordMap(lat , lon);
        }
    }

    public ListFragment setCallbackRecordClicked(Callback_recordClicked callbackRecordClicked) {
        this.callbackRecordClicked = callbackRecordClicked;
        return this;
    }
    private void findViews(View view) {
        fragment_RECYCLER_itemList= view.findViewById(R.id.fragment_RECYCLER_itemList);

    }
}