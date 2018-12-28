package com.gsondemo.mine.model;

import com.google.gson.annotations.SerializedName;

public class DiyModel {
    public String url;

    @SerializedName("width")
    public int width;

    @SerializedName("height")
    public int height;

    @SerializedName("title")
    public String title;

    public CookbookImage() {
        width = 0;
        height = 0;
        title = "df";
    }
}
