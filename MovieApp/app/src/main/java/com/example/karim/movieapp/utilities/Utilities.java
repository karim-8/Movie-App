package com.example.karim.movieapp.utilities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {


    //put Your Keys here
    public static String popular_Link = "http://api.themoviedb.org/3/movie/popular?api_key=1e092583515b033fc103f888d09b7806";
    public static String Top_Rated = "http://api.themoviedb.org/3/movie/top_rated?api_key=1e092583515b033fc103f888d09b7806";

    //checking connections
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




}
