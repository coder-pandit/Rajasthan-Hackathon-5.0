
package com.example.kumar.mharorajasthan;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tour {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("supplier")
    @Expose
    private String supplier;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("perex")
    @Expose
    private String perex;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("original_price")
    @Expose
    private Double originalPrice;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("duration_min")
    @Expose
    private Object durationMin;
    @SerializedName("duration_max")
    @Expose
    private Object durationMax;
    @SerializedName("flags")
    @Expose
    private List<Object> flags = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerex() {
        return perex;
    }

    public void setPerex(String perex) {
        this.perex = perex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Object getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Object durationMin) {
        this.durationMin = durationMin;
    }

    public Object getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(Object durationMax) {
        this.durationMax = durationMax;
    }

    public List<Object> getFlags() {
        return flags;
    }

    public void setFlags(List<Object> flags) {
        this.flags = flags;
    }

}
