package edu.vt.cs.cs5254.gallery;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.vt.cs.cs5254.gallery.model.GalleryItem;
import edu.vt.cs.cs5254.gallery.network.FlickrFetcher;

import static org.junit.Assert.*;

public class FlickrFetcherTest {

    private static final String TAG = "FlickrFetcherTest";

    @Test
    public void parseItems() {

        FlickrFetcher flickrFetcher = new FlickrFetcher();
        List<GalleryItem> items = new ArrayList<>();

        try{
            
            InputStream is = FlickrFetcherTest.class.getResourceAsStream("/json/flickr.json");
            String string = getString(is);

            JSONObject jsonBody = new JSONObject(string);

            flickrFetcher.parseItems(items, jsonBody);

        } catch (JSONException je){

                System.out.println( TAG + "JSONException");
        }

        assertEquals(8, items.size());
        GalleryItem item0 = items.get(0);
        assertEquals("47671605341", item0.getId());
        assertEquals("Lost in Baker Street", item0.getCaption());
        assertEquals("https://live.staticflickr.com/65535/47671605341_c519390246_m.jpg",
                item0.getUrl());
        assertEquals(51.522962, item0.getLat(), 0.0001);
        assertEquals(-0.154752, item0.getLon(), 0.0001);
    }



    private String getString(InputStream inputStream) {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                sb.append(line);
            }
        } catch (IOException ioe) {
            System.out.println(TAG + ": IOException");
        }
        return sb.toString();
    }
}