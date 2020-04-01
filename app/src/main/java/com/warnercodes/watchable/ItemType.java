package com.warnercodes.watchable;

public class ItemType {
    private String titolo;
    private int type;

    public ItemType(String titolo, int type) {
        this.titolo = titolo;
        this.type = type;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
