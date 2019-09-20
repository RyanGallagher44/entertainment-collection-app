package com.example.finalproject2019;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GamesFragment extends Fragment {

    View myView;
    ListView listView;
    ArrayList<String> titles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.games_layout,container,false);
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
                String search = titles.get(position).replaceAll("\\s","_");
                final DefaultHttpClient httpClient = new DefaultHttpClient();
                final HttpGet httpGet = new HttpGet("http://en.wikipedia.org/wiki/"+search);
                final ResponseHandler<String> resHandler = new BasicResponseHandler();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String page = httpClient.execute(httpGet, resHandler);
                            Log.d("TAG",page+"");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Toast.makeText(getActivity(),titles.get(position)+"",Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }

    public void updateGamesList(ArrayList<String> games){
        Log.d("TAG", "UPDATE GAMES LIST");
        titles = games;
        Log.d("TAG", "TITLES ARRAYLIST IN METHOD: "+titles);
    }
}
