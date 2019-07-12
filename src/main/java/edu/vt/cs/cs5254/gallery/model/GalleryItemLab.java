package edu.vt.cs.cs5254.gallery.model;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.vt.cs.cs5254.gallery.network.FlickrFetcher;


public class GalleryItemLab {

    private List<GalleryItem> mGalleryItems;
    public static final GalleryItemLab INSTANCE = new GalleryItemLab();
    private static final String TAG = "GalleryItemLab";

    private GalleryItemLab() {
        mGalleryItems = new ArrayList<>();
    }

    public static final GalleryItemLab getInstance() {

        return INSTANCE;
    }

    public interface OnRefreshItemsListener {
        void onRefreshItems(List<GalleryItem> galleryItems);
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        private OnRefreshItemsListener mListener;

        public FetchItemsTask(OnRefreshItemsListener listener) {
            mListener = listener;
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mGalleryItems = items;
            Log.i(TAG, items.size() + " items have been retrieved");
            mListener.onRefreshItems(mGalleryItems);
        }

    }

    public void refreshItems(OnRefreshItemsListener listener) {
        new FetchItemsTask(listener).execute();
    }

    public boolean hasGalleryItems() {
        if (mGalleryItems == null || mGalleryItems.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<GalleryItem> getGalleryItems() {
        return mGalleryItems;
    }

    public GalleryItem getGalleryItem(int position) {
        return mGalleryItems.get(position);
    }






}
