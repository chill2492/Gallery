package edu.vt.cs.cs5254.gallery;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import edu.vt.cs.cs5254.gallery.model.GalleryItem;
import edu.vt.cs.cs5254.gallery.model.GalleryItemLab;

import edu.vt.cs.cs5254.gallery.network.ThumbnailDownloader;


public class GalleryFragment extends Fragment
        implements GalleryItemLab.OnRefreshItemsListener {

    private static final String TAG = "GalleryFragment";

    // view fields
    private RecyclerView mPhotoRecyclerView;

    // model fields
    private GalleryItemLab mItemLab = GalleryItemLab.getInstance();

    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public static GalleryFragment newInstance() {

        return new GalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (!mItemLab.hasGalleryItems()) {
            mItemLab.refreshItems(this);
        }

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                (photoHolder, bitmap) -> {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    photoHolder.bindDrawable(drawable);
                    photoHolder.getGalleryItem().setDrawable(drawable);
                }
        );

        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mPhotoRecyclerView = view.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        setupAdapter();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.clearQueue();
        Log.i(TAG, "Background thread destroyed");
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItemLab.getGalleryItems()));
        }
    }

    @Override
    public void onRefreshItems(List<GalleryItem> galleryItems) {

        setupAdapter();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = itemView.findViewById(R.id.item_image_view);
        }

        public GalleryItem getGalleryItem() {
            return mGalleryItem;
        }

        public void setGalleryItem(GalleryItem galleryItem) {
            mGalleryItem = galleryItem;
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            photoHolder.setGalleryItem(galleryItem);

            if (galleryItem.hasDrawable()) {
                photoHolder.bindDrawable(galleryItem.getDrawable());
            } else {
                Drawable placeholder = ContextCompat.getDrawable(getActivity(), R.drawable.camera);
                photoHolder.bindDrawable(placeholder);
                mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());
            }

        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

}