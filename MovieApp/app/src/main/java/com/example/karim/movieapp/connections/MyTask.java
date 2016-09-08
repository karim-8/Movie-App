package com.example.karim.movieapp.connections;

//responsible for receiving data from server

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.karim.movieapp.adapters.GridAdapter;
import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.views.MovieDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyTask extends AsyncTask<String, Void, String> {


    GridView gridView;

    private Context context;

    ProgressDialog progressDialog;

    List<Movie> movies;


    public MyTask(Context context,GridView gridView1,List<Movie> movies) {
        this.context = context;
        this.gridView = gridView1;
        this.movies = movies;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {


        String link = strings[0];


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try {

            URL url = new URL(link);

            // Create the request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            return readStream(inputStream);

        } catch (IOException e) {

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                     Log.e("MainActivity", "Error closing stream", e);

                }
            }
        }
    }


    @Override
    protected void onPostExecute(String data) {

        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray moviesArray = jsonObject.getJSONArray("results");
                //create Array list to save data
             //  movies = new ArrayList<>();
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObject = moviesArray.getJSONObject(i);

                    Movie movie = new Movie(movieObject.getString("original_title"),
                            movieObject.getString("poster_path"),
                            movieObject.getString("overview"),
                            movieObject.getString("vote_average"),
                            movieObject.getString("release_date"));
                    movies.add(movie);

                }
                progressDialog.dismiss();

                gridView.setAdapter(new GridAdapter(context, movies));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override

                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(context, MovieDetailActivity.class);

                        intent.putExtra("movie",movies.get(i));
                     //   intent.putExtra("url", movies.get(i).image);
                      //  intent.putExtra("overview", movies.get(i).overview);
                       // intent.putExtra("rate", movies.get(i).rate);
                       // intent.putExtra("release date", movies.get(i).releaseDate);
                        //intent.putExtra("title", movies.get(i).title);
                        context.startActivity(intent);

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                // error in parsing data.
                progressDialog.dismiss();

            }


        } else {
            // error in get data , show error message to user.
            progressDialog.dismiss();
        }


    }


    //buffer reader method take single from input stream ,
// save data which flows into streams

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}