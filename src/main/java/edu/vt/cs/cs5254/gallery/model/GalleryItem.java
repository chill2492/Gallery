package edu.vt.cs.cs5254.gallery.model;

import android.graphics.drawable.Drawable;

public class GalleryItem {
    private String mCaption = "";
    private String mId = "";
    private String mUrl = "";

    private double mLat = 0.0;
    private double mLon = 0.0;

    private Drawable mDrawable = null;

    @Override
    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) { mUrl = url; }

    public double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(Double lon) {
        mLon = lon;
    }

    public Drawable getDrawable() { return mDrawable; }

    public void setDrawable(Drawable drawable) { mDrawable = drawable; }

    public boolean hasDrawable() { return !(mDrawable == null); }

}
