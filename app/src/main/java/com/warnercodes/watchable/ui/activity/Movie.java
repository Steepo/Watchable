package com.warnercodes.watchable.ui.activity;

import java.io.Serializable;
import android.util.Log;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Movie {
    private String copertina;
    private String title;
    private String tagline;
    private String trama;
    private List<String> generi;
    private int movieId;
    private String imdbId;
    private String overview;
    private String releaseDate;
    private int runtime;
    private RequestQueue requestQueue;
    private List<Movie> movies;
    private List<Movie> similar;
    private String youtubekey;



    public Movie(String copertina, String title, String tagline, String trama, List<String> generi, int movieId, String imdbId, String overview, String releaseDate, int runtime) {
        this.copertina = copertina;
        this.title = title;
        this.tagline = tagline;
        this.trama = trama;
        this.generi = generi;
        this.movieId = movieId;
        this.imdbId = imdbId;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
    }

    public Movie() {

    }

    public void parseJson(JSONObject response)  {
        Movie item = new Movie();
        movies = new ArrayList<Movie>();
        generi = new ArrayList<String>();
        try {
            response.getBoolean("adult");

            item.setCopertina(response.getString("poster_path"));
            item.setTitle(response.getString("original_title"));
            item.setTagline(response.getString("tagline"));
            item.setTrama(response.getString("overview"));

            JSONArray genres = response.getJSONArray("genres");
            if (genres.length() > 0)
                for (int i=0; i < genres.length(); i++) {
                    generi.add(genres.getJSONObject(i).getString("name"));
                }
            item.setGeneri(generi);
            item.setMovieId(response.getInt("id"));
            //item.setImdbId(response.getInt("imdb_id"));
            item.setOverview(response.getString("overview"));
            item.setReleaseDate(response.getString("release_date"));
            item.setRuntime(response.getInt("runtime"));
            Log.i("DEBUG", item.toString());
            movies.add(item);
            Log.i("DEBUG", movies.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie parseSingleFilmJson(JSONObject response)  {
        Movie item = new Movie();
        try {
            item.setCopertina(response.getString("poster_path"));
            item.setMovieId(response.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "copertina=" + copertina +
                ", title='" + title + '\'' +
                ", tagline='" + tagline + '\'' +
                ", trama='" + trama + '\'' +
                ", generi=" + generi +
                ", movieId=" + movieId +
                ", imdbId=" + imdbId +
                ", overview='" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", runtime=" + runtime +
                ", requestQueue=" + requestQueue +
                '}';
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Movie getMovie(){
        return this;
    }



    public String getCopertina() {
        return copertina;
    }

    public void setCopertina(String copertina) {
        this.copertina = "https://image.tmdb.org/t/p/w500"+copertina;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public List<String> getGeneri() {
        return generi;
    }

    public void setGeneri(List<String> generi) {
        this.generi = generi;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public List<Movie> getSimilar() {
        return similar;
    }
    public void setSimilar(List<Movie> similar) {
        this.similar = similar;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getYoutubekey() {
        return youtubekey;
    }

    public void setYoutubekey(String youtubekey) {
        this.youtubekey = youtubekey;
    }


}


