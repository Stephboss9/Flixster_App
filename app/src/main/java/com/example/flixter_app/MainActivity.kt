package com.example.flixter_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException



private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView //reference to recycler view in main activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvMovies)

        //"this" refers to MainActivity which is a class but also serves as a context for the adapter class
        val movieAdapter = MovieAdapter(this, movies)
        rvMovies.adapter = movieAdapter //binds the recycler view to the data source(MovieAdapter) to display data
        rvMovies.layoutManager = LinearLayoutManager(this) //Binds layout manager to the recycler view
                                                                  // Will display items top to bottom,

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object:JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess $json")
                try {
                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdapter.notifyDataSetChanged() //notifies the adatper that the data set changed
                    Log.i(TAG, "Movie list $movies")
                }
                catch (e: JSONException){
                    Log.e(TAG, "Exception Encountered $e")
                }

            }

        })

    }


}