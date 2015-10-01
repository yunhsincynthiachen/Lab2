package com.example.cynchen.googleinstagram;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

/**
 * Created by cynchen on 9/17/15.
 */
public class GoogleImageGrabber {
    public RequestQueue queue;

    //Makes the volley request
    public GoogleImageGrabber (Context context){
        queue = Volley.newRequestQueue(context);
    }

    //Function that searches the input to the function, given by SearchFragment on search click
    public void searchGoogle(String searchQuery, final searchcallback callback) {
        String query = searchQuery.replaceAll(" ", "+"); //replaces spaces with +
        String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCs50YhgpTH5MgR5qzXaMB7YmFSn4k35j4&cx=007777639942419674881:ndtwi-uhrlg&q="+query+"&searchType=image&imgSize=medium";

        //Requesting the JSON object with the custom search url
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Initializes an arraylist that we will fill with image url's
                        ArrayList<String> imageLinks = new ArrayList<String>();
                        try {
                            //get items array, which we will parse to get all of the links
                            JSONArray items = response.getJSONArray("items");
                            for (int i=0; i<items.length(); i++){
                                JSONObject image_item = items.getJSONObject(i);
                                String link = image_item.getString("link");
                                imageLinks.add(link); //add each link to arraylist of image links
                            }
                        } catch (Exception e) {
                            Log.e("ERROR!", e.getMessage()); //catch error
                        }
                        callback.callback(imageLinks); //use callback to return imagelinks arraylist
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR!", error.getMessage());
                        callback.callback(null); //don't callback anything if unsuccessful
                    }
                }
        );
        queue.add(request); //make the queue request
    }
}
