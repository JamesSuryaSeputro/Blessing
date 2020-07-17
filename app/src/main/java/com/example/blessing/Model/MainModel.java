package com.example.blessing.Model;

public class MainModel {

    private String learntext;
    private int learnimage;

    public MainModel() {
    }

    public MainModel(String learntext, int learnimage) {
        this.learntext = learntext;
        this.learnimage = learnimage;
    }

    public String getLearntext() {
        return learntext;
    }

    public void setLearntext(String learntext) {
        this.learntext = learntext;
    }

    public int getLearnimage() {
        return learnimage;
    }

    public void setLearnimage(int learnimage) {
        this.learnimage = learnimage;
    }
}
