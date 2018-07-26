
package com.example.kumar.mharorajasthan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Place {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("rating_local")
    @Expose
    private Double ratingLocal;
    @SerializedName("quadkey")
    @Expose
    private String quadkey;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("bounding_box")
    @Expose
    private BoundingBox boundingBox;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_suffix")
    @Expose
    private String nameSuffix;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("marker")
    @Expose
    private String marker;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("parent_ids")
    @Expose
    private List<String> parentIds = null;
    @SerializedName("perex")
    @Expose
    private String perex;
    @SerializedName("customer_rating")
    @Expose
    private Object customerRating;
    @SerializedName("star_rating")
    @Expose
    private Object starRating;
    @SerializedName("star_rating_unofficial")
    @Expose
    private Object starRatingUnofficial;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Double getRatingLocal() {
        return ratingLocal;
    }

    public void setRatingLocal(Double ratingLocal) {
        this.ratingLocal = ratingLocal;
    }

    public String getQuadkey() {
        return quadkey;
    }

    public void setQuadkey(String quadkey) {
        this.quadkey = quadkey;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public String getPerex() {
        return perex;
    }

    public void setPerex(String perex) {
        this.perex = perex;
    }

    public Object getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Object customerRating) {
        this.customerRating = customerRating;
    }

    public Object getStarRating() {
        return starRating;
    }

    public void setStarRating(Object starRating) {
        this.starRating = starRating;
    }

    public Object getStarRatingUnofficial() {
        return starRatingUnofficial;
    }

    public void setStarRatingUnofficial(Object starRatingUnofficial) {
        this.starRatingUnofficial = starRatingUnofficial;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
