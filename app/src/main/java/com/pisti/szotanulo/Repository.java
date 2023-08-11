package com.pisti.szotanulo;

public class Repository {
    public int Id;
    public String EnglishMeaning;
    public String HungarianMeaning;
    public byte RememberanceLevel;

    public int getId() {
        return Id;
    }

    public String getEnglishMeaning() {
        return EnglishMeaning;
    }

    public String getHungarianMeaning() {
        return HungarianMeaning;
    }

    public byte getRememberanceLevel() {
        return RememberanceLevel;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public void setHungarianMeaning(String hungarianMeaning) {
        this.HungarianMeaning = hungarianMeaning;
    }

    public void setEnglishMeaning(String englishMeaning) {
        this.EnglishMeaning = englishMeaning;
    }

    public void setRememberanceLevel(byte rememberanceLevel) {
        this.RememberanceLevel = rememberanceLevel;
    }
}
