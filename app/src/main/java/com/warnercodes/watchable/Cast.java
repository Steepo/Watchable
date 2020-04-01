package com.warnercodes.watchable;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;


public class Cast {
    private int actorID;
    private String copertina;
    private String name;
    private int runtime;
    private RequestQueue requestQueue;
    private String tipo;


    public Cast(String copertina, String name, int actorID, int runtime) {
        this.copertina = copertina;
        this.name = name;
        this.actorID = actorID;
        this.runtime = runtime;
    }

    public Cast() {

    }
    public Cast parseActorJson(JSONObject response, String tipo)  {
        Cast item = new Cast();
        try {
            item.setTipo(tipo);
            item.setCopertina(response.getString("profile_path"));
            item.setName(response.getString("name"));
            item.setActorID(response.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "copertina=" + copertina +
                "nome="+ name +
                ", castId=" + actorID +
                ", runtime=" + runtime +
                ", requestQueue=" + requestQueue +
                '}';
    }

    public int getActorID() {
        return actorID;
    }

    public void setActorID(int actorID) {
        this.actorID = actorID;
    }

    public String getCopertina() {
        return copertina;
    }

    public void setCopertina(String copertina) {
        this.copertina = "https://image.tmdb.org/t/p/w500"+copertina;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
