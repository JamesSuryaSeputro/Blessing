package com.example.blessing.Model;

public class LearningModel {

    private String learntext;
    private int learnimage;

    public LearningModel() {
    }

    public LearningModel(String learntext, int learnimage) {
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
