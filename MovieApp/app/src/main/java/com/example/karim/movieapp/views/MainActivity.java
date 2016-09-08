package com.example.karim.movieapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.karim.movieapp.R;
import com.example.karim.movieapp.adapters.GridAdapter;
import com.example.karim.movieapp.connections.MyTask;
import com.example.karim.movieapp.database.DatabaseAdapter;
import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;

    List<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>(1);
        gridView = (GridView) findViewById(R.id.gridView1);


        if (savedInstanceState == null){
            if (Utilities.isNetworkAvailable(MainActivity.this)== true){
                TextView textView = (TextView) findViewById(R.id.errortextview);
                textView.setVisibility(View.INVISIBLE);
                startTask();

            }else{
                final TextView textView = (TextView) findViewById(R.id.errortextview);
                textView.setVisibility(View.VISIBLE);textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(Utilities.isNetworkAvailable(MainActivity.this)) {
                           startTask();
                           textView.setVisibility(View.INVISIBLE);
                       }
                    }
                });
            }


        }else {

            movies = savedInstanceState.getParcelableArrayList("list");
            GridAdapter gridAdapter = new GridAdapter(MainActivity.this,movies);
            gridView.setAdapter(gridAdapter);
            setGridItemListener();

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) movies);

    }



    public void startTask(){
        MyTask myTask = new MyTask(MainActivity.this,gridView,movies);
        myTask.execute(Utilities.popular_Link);

    }


    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MyTask myTask;
        switch (item.getItemId()) {

            case R.id.favourite:
                DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
                movies =  databaseAdapter.getAllMovies();
                gridView.setAdapter(new GridAdapter(this,movies));
                setGridItemListener();
                break;
            case R.id.top_rated:

                myTask = new MyTask(MainActivity.this,gridView,movies);
                myTask.execute(Utilities.Top_Rated);
                return true;

            case R.id.most_pop:

                myTask = new MyTask(MainActivity.this,gridView,movies);
                myTask.execute(Utilities.popular_Link);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void setGridItemListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie",movies.get(i));
                MainActivity.this.startActivity(intent);

            }
        });
    }


}
