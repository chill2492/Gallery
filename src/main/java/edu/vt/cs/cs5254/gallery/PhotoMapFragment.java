package edu.vt.cs.cs5254.gallery;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.vt.cs.cs5254.gallery.model.GalleryItem;
import edu.vt.cs.cs5254.gallery.model.GalleryItemLab;


public class PhotoMapFragment extends SupportMapFragment
        implements GalleryItemLab.OnRefreshItemsListener {

    private static final String TAG = "PhotoMapFragment";

    // model field
    private GalleryItemLab mItemLab = GalleryItemLab.getInstance();

    // view field
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (!mItemLab.hasGalleryItems()) {
            mItemLab.refreshItems(this);
        }

        getMapAsync(googleMap -> {
            mMap = googleMap;
            refreshView();
        });

    }


    private void refreshView() {
        if (mMap == null || !mItemLab.hasGalleryItems()) return;

        for (GalleryItem item : mItemLab.getGalleryItems()) {
            LatLng latLng = new LatLng(item.getLat(), item.getLon());
            MarkerOptions options = new MarkerOptions().position(latLng).title(item.getCaption());

            mMap.setInfoWindowAdapter(new MarkerInfoWindow());
            Marker marker = mMap.addMarker(options);
            marker.setTag(item);

        }

    }

    public static PhotoMapFragment newInstance() {
        return new PhotoMapFragment();
    }

    @Override
    public void onRefreshItems(List<GalleryItem> mItems) {
        Log.i(TAG,mItems.size() + " items have been retrieved");
        refreshView();
    }

    private class MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.info_window_marker, null);
            TextView title = view.findViewById(R.id.marker_title);
            ImageView thumbnail = view.findViewById(R.id.marker_thumbnail);
            GalleryItem item = (GalleryItem) marker.getTag();
            title.setText(item.getCaption());

            if (item.hasDrawable()) {
                thumbnail.setImageDrawable(item.getDrawable());
            } else {
                thumbnail.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.camera));
            }

            return view;
        }
    }
}
