package com.example.finalproject2019;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ScanFragment.ScanFragmentListener {

    TextView nav_user;
    TextView nav_email;

    TextView text_one;
    TextView text_two;
    TextView text_three;
    TextView text_four;
    ImageView logo;

    ScanFragment scanFragment;
    GamesFragment gamesFragment;
    MoviesFragment moviesFragment;
    BooksFragment booksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text_one = findViewById(R.id.id_text_one);
        text_two = findViewById(R.id.id_text_two);
        text_three = findViewById(R.id.id_text_three);
        text_four = findViewById(R.id.id_text_four);
        logo = findViewById(R.id.id_logo);

        scanFragment = new ScanFragment();
        gamesFragment = new GamesFragment();
        moviesFragment = new MoviesFragment();
        booksFragment = new BooksFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_user = (TextView)headerView.findViewById(R.id.id_nav_name);
        nav_email = (TextView)headerView.findViewById(R.id.id_nav_email);
        nav_user.setText("Entertainment Collection");
        nav_email.setText("10012046@sbstudents.org");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_scan_layout) {
            text_one.setVisibility(View.INVISIBLE);
            text_two.setVisibility(View.INVISIBLE);
            text_three.setVisibility(View.INVISIBLE);
            text_four.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction().replace(R.id.content_frame,scanFragment).commit();
        } else if (id == R.id.nav_games_layout) {
            text_one.setVisibility(View.INVISIBLE);
            text_two.setVisibility(View.INVISIBLE);
            text_three.setVisibility(View.INVISIBLE);
            text_four.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction().replace(R.id.content_frame,gamesFragment).commit();
        } else if (id == R.id.nav_movies_layout) {
            text_one.setVisibility(View.INVISIBLE);
            text_two.setVisibility(View.INVISIBLE);
            text_three.setVisibility(View.INVISIBLE);
            text_four.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction().replace(R.id.content_frame,moviesFragment).commit();
        } else if (id == R.id.nav_books_layout) {
            text_one.setVisibility(View.INVISIBLE);
            text_two.setVisibility(View.INVISIBLE);
            text_three.setVisibility(View.INVISIBLE);
            text_four.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction().replace(R.id.content_frame,booksFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onScanInputSent(ArrayList<String> games, ArrayList<String> movies, ArrayList<String> books) {
        gamesFragment.updateGamesList(games);
        moviesFragment.updateMoviesList(movies);
        booksFragment.updateBooksList(books);
    }
}
