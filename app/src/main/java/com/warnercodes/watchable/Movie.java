package com.warnercodes.watchable;

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
    private String original_title;
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
    private List<Integer> similar;
    private String youtubekey;
    private String tipo;

    private String director;
    private String writers;
    private String awards;
    private String metascoreScore;
    private String imdbRating;
    private String imdbVotes;
    private String rottenScore;
    private String backdrop;

    private boolean watchlist;
    private boolean Watched;


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

    public void parseJson(JSONObject response) {
        Movie item = new Movie();
        movies = new ArrayList<Movie>();
        generi = new ArrayList<String>();
        similar = new ArrayList<Integer>();
        try {
            response.getBoolean("adult");
            item.setTipo(tipo);
            item.setCopertina(response.getString("poster_path"));
            item.setTitle(response.getString("title"));
            item.setOriginal_title(response.getString("original_title"));
            item.setTagline(response.getString("tagline"));
            item.setTrama(response.getString("overview"));
            item.setImdbId(response.getString("imdb_id"));

            JSONArray genres = response.getJSONArray("genres");
            if (genres.length() > 0)
                for (int i = 0; i < genres.length(); i++) {
                    generi.add(genres.getJSONObject(i).getString("name"));
                }
            item.setGeneri(generi);
            item.setMovieId(response.getInt("id"));
            //item.setImdbId(response.getInt("imdb_id"));
            item.setOverview(response.getString("overview"));
            item.setReleaseDate(response.getString("release_date"));
            item.setRuntime(response.getInt("runtime"));
            movies.add(item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie parseSingleMovieJson(JSONObject response, String type) {
        Movie item = new Movie();
        item.setTipo(type);
        generi = new ArrayList<String>();
        try {
            item.setMovieId(response.getInt("id"));
            item.setCopertina(response.getString("poster_path"));

            item.setTitle(response.getString("title"));
            item.setOriginal_title(response.getString("original_title"));
            item.setMovieId(response.getInt("id"));
            item.setReleaseDate(response.getString("release_date"));
            item.setTrama(response.getString("overview"));
            JSONArray genres = response.getJSONArray("genres");
            item.setImdbId(response.getString("imdb_id"));
            if (genres.length() > 0)
                for (int i = 0; i < genres.length(); i++) {
                    generi.add(genres.getJSONObject(i).getString("name"));
                    Log.i("GENERI", genres.getJSONObject(i).getString("name"));
                }
            item.setGeneri(generi);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return item;
    }

    public Movie parseAdviceMovieJson(JSONObject response, String type) {
        Movie item = new Movie();
        item.setTipo(type);
        generi = new ArrayList<String>();
        try {
            item.setMovieId(response.getInt("id"));
            item.setBackdrop(response.getString("backdrop_path"));
            item.setCopertina(response.getString("poster_path"));
            item.setTitle(response.getString("title"));
            item.setOriginal_title(response.getString("original_title"));
            item.setMovieId(response.getInt("id"));
            item.setReleaseDate(response.getString("release_date"));
            item.setTrama(response.getString("overview"));
            JSONArray genres = response.getJSONArray("genres");
            item.setImdbId(response.getString("imdb_id"));
            if (genres.length() > 0)
                for (int i = 0; i < genres.length(); i++) {
                    generi.add(genres.getJSONObject(i).getString("name"));
                    Log.i("GENERI", genres.getJSONObject(i).getString("name"));
                }
            item.setGeneri(generi);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return item;
    }

    public void addOMdbInfo(JSONObject response) {

        try {
            this.setDirector(response.getString("Director"));
            this.setWriters(response.getString("Writer"));
            this.setAwards(response.getString("Awards"));

            this.setImdbRating(response.getString("imdbRating"));
            this.setImdbVotes(response.getString("imdbVotes"));
            this.setMetascoreScore(response.getString("Metascore"));
            if (response.getJSONArray("Ratings").length() > 1 && response.getJSONArray("Ratings").getJSONObject(1).getString("Source").contains("Rotten"))
                this.setRottenScore(response.getJSONArray("Ratings").getJSONObject(1).getString("Value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void addSimilar(Integer movieId) {
        if (similar == null)
            similar = new ArrayList<Integer>();
        similar.add(movieId);
    }

    public List<Movie> getMovie() {
        return movies;
    }


    public String getCopertina() {
        return copertina;
    }

    public void setCopertina(String copertina) {
        setCopertinaFull("https://image.tmdb.org/t/p/w400" + copertina);
        //Log.i("Copertine", copertina);
    }

    public void setCopertinaFull(String copertina) {
        this.copertina = copertina;
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

    public List<Integer> getSimilar() {
        return similar;
    }

    public void setSimilar(List<Integer> similar) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isInWatchlist() {
        return watchlist;
    }

    public void setInWatchlist(boolean inWatchlist) {
        this.watchlist = inWatchlist;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getMetascoreScore() {
        return metascoreScore;
    }

    public void setMetascoreScore(String metascoreScore) {
        this.metascoreScore = metascoreScore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getRottenScore() {
        return rottenScore;
    }

    public void setRottenScore(String rottenScore) {
        this.rottenScore = rottenScore;
    }

    public boolean isWatched() {
        return Watched;
    }

    public void setWatched(boolean watched) {
        Watched = watched;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = "https://image.tmdb.org/t/p/w400" + backdrop;
        ;
    }
}


