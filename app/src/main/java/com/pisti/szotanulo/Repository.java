package com.pisti.szotanulo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Repository {

    @SerializedName("wordId")
    @Expose
    private Integer wordId;
    @SerializedName("hungarianMeaning")
    @Expose
    private String hungarianMeaning;
    @SerializedName("englishMeaning")
    @Expose
    private String englishMeaning;
    @SerializedName("rememberanceLevel")
    @Expose
    private Integer rememberanceLevel;

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getHungarianMeaning() {
        return hungarianMeaning;
    }

    public void setHungarianMeaning(String hungarianMeaning) {
        this.hungarianMeaning = hungarianMeaning;
    }

    public String getEnglishMeaning() {
        return englishMeaning;
    }

    public void setEnglishMeaning(String englishMeaning) {
        this.englishMeaning = englishMeaning;
    }

    public Integer getRememberanceLevel() {
        return rememberanceLevel;
    }

    public void setRememberanceLevel(Integer rememberanceLevel) {
        this.rememberanceLevel = rememberanceLevel;
    }

}