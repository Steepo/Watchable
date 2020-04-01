package com.warnercodes.watchable.ui.activity;

import java.io.Serializable;

public class Movie implements Serializable {
    public int copertina;

    public Movie(int copertina) {
        this.copertina = copertina;
    }

    public int getCopertina() {
        return copertina;
    }

    public void setCopertina(int copertina) {
        this.copertina = copertina;
    }
}


