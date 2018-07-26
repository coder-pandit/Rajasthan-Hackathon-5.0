package com.example.kumar.mharorajasthan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data1 {
    @SerializedName("tours")
    @Expose
    private List<Tour> tours = null;

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

}
