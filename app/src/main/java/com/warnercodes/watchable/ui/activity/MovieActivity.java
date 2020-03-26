package com.warnercodes.watchable.ui.activity;

import java.io.Serializable;

public class MovieActivity implements Serializable {
    public int copertina;

    public MovieActivity(int copertina) {
        this.copertina = copertina;
    }

    public int getCopertina() {
        return copertina;
    }

    public void setCopertina(int copertina) {
        this.copertina = copertina;
    }
}


