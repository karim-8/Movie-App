package com.example.karim.movieapp.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karim.movieapp.R;
import com.example.karim.movieapp.database.DatabaseAdapter;
import com.example.karim.movieapp.model.Movie;
import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;

    ImageButton bookmark;
    boolean isBookmarked ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        bookmark = (ImageButton) findViewById(R.id.favouriteButton);


        Intent  intent = getIntent();

        movie = intent.getParcelableExtra("movie");

        ImageView imageView =(ImageView)findViewById(R.id.imageView);
        TextView titleTextView = (TextView) findViewById(R.id.tv_title);
        TextView releaseDate = (TextView) findViewById(R.id.tv_release_date);
        TextView rateTextview = (TextView) findViewById(R.id.tv_rate);
        TextView overviewTextview  = (TextView) findViewById(R.id.tv_overview);


        titleTextView.setText(movie.getTitle());
        releaseDate.setText(movie.releaseDate);
        rateTextview.setText(movie.rate);
        overviewTextview.setText(movie.overview);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
        isBookmarked =  databaseAdapter.isMovieBookmarked(movie.title);
        if(isBookmarked){
            isBookmarked = true;
            bookmark.setImageResource(R.drawable.love);
        }else{
            isBookmarked = false;
            bookmark.setImageResource(R.drawable.download);
        }

        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+movie.getImage()).into(imageView);
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("message", "This is my message to be reloaded");

        super.onSaveInstanceState(outState);


    }

    public void markAsFavourite(View view) {

        if(isBookmarked){
            // delete
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
            boolean isDeleted = databaseAdapter.deleteMovie(movie.title);
            if(isDeleted){
                isBookmarked= false;
                bookmark.setImageResource(R.drawable.download);
            }else{
                isBookmarked = true;
            }
        }else{
            // add
            boolean isAdd = new DatabaseAdapter(this).addMovie(movie);
            if(isAdd){
                isBookmarked = true;
                bookmark.setImageResource(R.drawable.love);
            }else {
                isBookmarked = false;
                bookmark.setImageResource(R.drawable.download);
            }

        }
    }
}
