package com.warnercodes.watchable;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private String author;
    private String text;
    private String title;
    private int score;
    private int voteup;
    private int votedown;
    private Date date;


    public Review(String author, String text, String title, int score, int voteup, int votedown, Date date) {
        this.author = author;
        this.text = text;
        this.title = title;
        this.score = score;
        this.voteup = voteup;
        this.votedown = votedown;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVoteup() {
        return voteup;
    }

    public void setVoteup(int voteup) {
        this.voteup = voteup;
    }

    public int getVotedown() {
        return votedown;
    }

    public void setVotedown(int votedown) {
        this.votedown = votedown;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", score=" + score +
                ", voteup=" + voteup +
                ", votedown=" + votedown +
                ", SimpleDateFormat=" + date +
                '}';
    }
}
