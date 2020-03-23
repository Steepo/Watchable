package com.warnercodes.watchable;

import android.media.Image;

import java.util.List;

class Movie {
    private Image copertina;
    private String title;
    private String trama;
    private  List<String> generi;

    public Movie(Image copertina, String title, String trama, List<String> generi) {
        this.copertina = copertina;
        this.title = title;
        this.trama = trama;
        this.generi = generi;
    }

    public Image getCopertina() {
        return copertina;
    }

    public String getTitle() {
        return title;
    }

    public String getTrama() {
        return trama;
    }

    public List<String> getGeneri() {
        return generi;
    }
}


