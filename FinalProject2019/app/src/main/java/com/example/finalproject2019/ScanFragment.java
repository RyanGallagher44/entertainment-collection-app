package com.example.finalproject2019;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.WebDetection;
import com.google.api.services.vision.v1.model.WebEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.combineMeasuredStates;

public class ScanFragment extends Fragment {

    View myView;
    Button button;
    ImageView imageView;
    Bitmap bitmap;
    Vision vision;
    String result;
    TextView textView;
    Button yes;
    Button no;
    Button tryAgain;
    ArrayList<String> results;
    String finalResult;
    int count = 0;
    ArrayList<String> games;
    ArrayList<String> movies;
    ArrayList<String> books;
    Button add;
    RadioGroup radioGroup;
    RadioButton videoGamesButton;
    RadioButton moviesButton;
    RadioButton booksButton;
    int selectedRadioButton = 0;

    ScanFragmentListener listener;

    public interface ScanFragmentListener{
        void onScanInputSent(ArrayList<String> games, ArrayList<String> movies, ArrayList<String> books);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.scan_layout,container,false);
        setRetainInstance(true);
        button = myView.findViewById(R.id.id_open_camera);
        imageView = myView.findViewById(R.id.id_picture_taken);
        textView = myView.findViewById(R.id.id_results);
        yes = myView.findViewById(R.id.id_yes);
        no = myView.findViewById(R.id.id_no);
        add = myView.findViewById(R.id.id_add);
        if(savedInstanceState != null){
            games = savedInstanceState.getStringArrayList("gameslist");
            movies = savedInstanceState.getStringArrayList("movieslist");
            books = savedInstanceState.getStringArrayList("bookslist");
        }
        if(games == null){
            games = new ArrayList<String>();
        }
        if(movies == null){
            movies = new ArrayList<String>();
        }
        if(books == null){
            books = new ArrayList<String>();
        }
        radioGroup = myView.findViewById(R.id.id_radiogroup);
        videoGamesButton = myView.findViewById(R.id.id_videogames_button);
        moviesButton = myView.findViewById(R.id.id_movies_button);
        booksButton = myView.findViewById(R.id.id_books_button);
        tryAgain = myView.findViewById(R.id.id_try_again);
        tryAgain.setVisibility(View.INVISIBLE);
        yes.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
        add.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        result = "Scan a game, movie, or book.";
        textView.setText(result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalResult = results.get(count);
                textView.setText("Which collection do you want\nto add "+finalResult+" to?");
                add.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count < results.size() && results.get(count) != null && !results.get(count).equals("null")) {
                    textView.setText(results.get(count) + "?");
                }
                else {
                    textView.setText("Sorry, we couldn't determine\nwhat you scanned.");
                    tryAgain.setVisibility(View.VISIBLE);
                    yes.setVisibility(View.INVISIBLE);
                    no.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.id_videogames_button){
                    selectedRadioButton = 1;
                }
                if (checkedId == R.id.id_movies_button){
                    selectedRadioButton = 2;
                }
                if (checkedId == R.id.id_books_button){
                    selectedRadioButton = 3;
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedRadioButton != 0) {
                    if (selectedRadioButton == 1) {
                        games.add(finalResult);
                        Toast toast = Toast.makeText(getContext(), finalResult + " has been added to " + videoGamesButton.getText() + "!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if (selectedRadioButton == 2) {
                        movies.add(finalResult);
                        Toast toast = Toast.makeText(getContext(), finalResult + " has been added to " + moviesButton.getText() + "!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    if (selectedRadioButton == 3) {
                        books.add(finalResult);
                        Toast toast = Toast.makeText(getContext(), finalResult + " has been added to " + booksButton.getText() + "!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    listener.onScanInputSent(games, movies, books);
                    add.setVisibility(View.INVISIBLE);
                    radioGroup.setVisibility(View.INVISIBLE);
                    textView.setText("Scan a game, movie, or book.");
                    imageView.setVisibility(View.INVISIBLE);
                    tryAgain.setVisibility(View.VISIBLE);
                }else{
                    Toast toast = Toast.makeText(getContext(),"Please select a collection to add "+finalResult+" to.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        return myView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("gameslist",games);
        outState.putStringArrayList("movieslist",movies);
        outState.putStringArrayList("bookslist",books);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ScanFragmentListener){
            listener = (ScanFragmentListener)context;
        }else{
            throw new RuntimeException(context.toString()+" must implement ScanFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);

        imageView.setVisibility(View.VISIBLE);

        button.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);

        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(),new AndroidJsonFactory(),null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("API Key"));
        vision = visionBuilder.build();

        result = "Processing...";
        textView.setText(result);

        webDetection();
    }

    private void webDetection(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] imageInByte = baos.toByteArray();
                    baos.close();

                    Log.d("TAG", "STEP 1");

                    Image inputImage = new Image();
                    inputImage.encodeContent(imageInByte);

                    Log.d("TAG", "STEP 2");

                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("WEB_DETECTION");

                    Log.d("TAG", "STEP 3");

                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(desiredFeature));

                    Log.d("TAG", "STEP 4");

                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));

                    Log.d("TAG", "STEP 5");

                    BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();

                    Log.d("TAG", "STEP 6");

                    List<AnnotateImageResponse> responses = batchResponse.getResponses();

                    Log.d("TAG", "STEP 7");

                    result = "";
                    for (AnnotateImageResponse res : responses) {
                        Log.d("TAG", "IN LOOP");
                        WebDetection annotation = res.getWebDetection();
                        for (WebEntity entity : annotation.getWebEntities()) {
                            result += entity.getDescription() + ",";
                            Log.d("TAG", "SUCCESS " + entity.getDescription() + " : " + entity.getEntityId() + " : " + entity.getScore());
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            count = 0;
                            textView.setText(result);
                            results = new ArrayList<String>();

                            String current = "";
                            for (int i = 0; i < textView.getText().length(); i++) {
                                if (textView.getText().charAt(i) == ',') {
                                    results.add(current);
                                    current = "";
                                } else {
                                    current += textView.getText().charAt(i);
                                }
                            }

                            textView.setText(results.get(count)+"?");
                            yes.setVisibility(View.VISIBLE);
                            no.setVisibility(View.VISIBLE);
                        }
                    });

                    Log.d("TAG", "STEP 8");
                } catch (Exception e) {
                    Log.d("TAG", "ERROR" + e.getMessage());
                }
            }
        });
    }
}
