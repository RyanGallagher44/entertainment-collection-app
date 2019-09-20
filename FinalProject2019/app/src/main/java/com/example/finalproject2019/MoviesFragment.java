package com.example.finalproject2019;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {

    View myView;
    ListView listView;
    ArrayList<String> titles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.movies_layout,container,false);
        listView = myView.findViewById(R.id.id_listview);

        setRetainInstance(true);

        if(titles == null){
            titles = new ArrayList<String>();
        }

        Log.d("TAG", "TITLES ARRAYLIST: "+titles);

        ArrayAdapter arrayAdapter = new ArrayAdapter(myView.getContext(),R.layout.custom_layout,titles);

        if(titles.size() > 0)
            listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),titles.get(position)+"",Toast.LENGTH_SHORT).show();
            }
        });
        return myView;
    }

    public void updateMoviesList(ArrayList<String> movies){
        titles = movies;
    }
}
