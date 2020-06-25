package com.warnercodes.watchable;

import org.json.JSONException;
import org.json.JSONObject;

public class Cast {
    private int id;
    private String character;
    private String name;
    private String profile_path;
    private String tipo;

    public Cast() {

    }

    public Cast parseCastJson(JSONObject response, String type) {
        Cast item = new Cast();
        item.setTipo(type);
        try {
            item.setId(response.getInt("id"));
            item.setName(response.getString("name"));
            item.setProfile_path(response.getString("profile_path"));
            item.setCharacter(response.getString("character"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
    }


    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = "https://image.tmdb.org/t/p/w200" + profile_path;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "id=" + id +
                ", character='" + character + '\'' +
                ", name='" + name + '\'' +
                ", profile_path='" + profile_path + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
