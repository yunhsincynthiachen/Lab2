package com.example.cynchen.googleinstagram;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllImagesFragment extends Fragment {

    public AllImagesFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Initialization of all of the buttons on the images page
    private Button nextbutton_all;
    private Button previousbutton_all;
    private Button switch_to_search;
    private Button remove_image;

    //Initialization of webview to load url's into the view
    private WebView webView_all;

    //Initialization of array list of all all saved images url (grabbed from db), and then index k to go through the list
    private ArrayList<String> saved_images_all;
    public int k;

    //Calling FeedReaderdbHelper's dbhelper function
    private FeedReaderdbHelper dbhelper;

    //ONCREATE: Begin
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_allimages, container, false);

        //Connects webview variable with webview from layout, and dbhelper from FeedReaderdbHelper
        webView_all = (WebView) rootView.findViewById(R.id.viewimage_all);
        dbhelper = new FeedReaderdbHelper(getActivity());

        //Make an array of saved images by reading the database
        saved_images_all = new ArrayList<String>(dbhelper.readData());
        k = 0; //initialize k to be 0

        //First step, check if there is anything in saved_images_all, or else the app will crash trying to
        //index from something with a size 0
        if (saved_images_all.size() == 0){
                webView_all.loadUrl("about:blank"); //nothing: put blank webview
        }
        else {
            webView_all.loadUrl(saved_images_all.get(0)); //something: show the first image
        }

        //If next button is clicked, there are different scenarios that could happen
        //One: 0 images saved -> alert dialog notifying user to save an image pops up
        //Two: 1 image saved -> do nothing, keep loading that one image
        //Three: k is at the last image of the list -> go to the 0th image
        //Four: All other k index values -> load the (k+1)th image
        nextbutton_all = (Button) rootView.findViewById(R.id.next_image_all);
        nextbutton_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved_images_all.size() == 0) {
                    create_alert_all_images(); //alert dialog creation function
                }
                else if (saved_images_all.size() == 1){
                    webView_all.loadUrl(saved_images_all.get(k));
                }
                else if (saved_images_all.size() - 1 == k){
                    k = 0;
                    webView_all.loadUrl(saved_images_all.get(k));
                }
                else {
                    k += 1;
                    webView_all.loadUrl(saved_images_all.get(k));
                }
            }
        });

        //If previous button is clicked, there are different scenarios that could happen:
        //One: 0 images saved -> popup alert dialog again
        //Two: 1 image saved -> do nothing, keep loading that one image
        //Three: k is at the first image of the list -> go to the last image
        //Four: All other k index values -> load the (k-1)th image
        previousbutton_all = (Button) rootView.findViewById(R.id.previous_image_all);
        previousbutton_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved_images_all.size() == 0){
                    create_alert_all_images(); //alert dialog creation function
                }
                else if (saved_images_all.size() == 1){
                    webView_all.loadUrl(saved_images_all.get(k));
                }
                else if (k == 0){
                    k = saved_images_all.size()-1;
                    webView_all.loadUrl(saved_images_all.get(k));
                }
                else {
                    k -= 1;
                    webView_all.loadUrl(saved_images_all.get(k));
                }
            }
        });

        //Removing images: 1. remove from database, 2. remove from list of saved images
        //One: 0 images saved -> popup alert dialog again
        //Two: Removed image is the last image of the list -> remove that image from db and arraylist, then check if the
        //new arraylist size is zero (in this case, load blank webview) or not (in this case, load the first image of the new list)
        //Three: For all other k values -> delete from db and arraylist, then check if the new arraylist is zero (in this case, load blank
        //webview) or not (in this case, load the new kth value of the shifted list)
        remove_image = (Button) rootView.findViewById(R.id.remove_image_button);
        remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved_images_all.size() == 0){
                    create_alert_all_images();
                }
                else if (k == saved_images_all.size()-1){
                    dbhelper.deleteData(saved_images_all.get(k));
                    saved_images_all.remove(saved_images_all.get(k));
                    if (saved_images_all.size() == 0) {
                        webView_all.loadUrl("about:blank");
                    }
                    else {
                        webView_all.loadUrl(saved_images_all.get(0));
                    }
                }
                else {
                    dbhelper.deleteData(saved_images_all.get(k));
                    saved_images_all.remove(saved_images_all.get(k));
                    if (saved_images_all.size() == 0) {
                        webView_all.loadUrl("about:blank");
                    }
                    else {
                        webView_all.loadUrl(saved_images_all.get(k));
                    }
                }
            }
        });

        //When you want to switch layout back to search for more images: Uses the main activity
        //function "transitionToFragment", with the SearchFragment called
        switch_to_search = (Button) rootView.findViewById(R.id.search_images_button);
        switch_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment main_search_frag = new SearchFragment();
                ((MainActivity)getActivity()).transitionToFragment(main_search_frag);
            }
        });

        return rootView;
    }

    //Function that creates the alert dialog and exits out of it when you click out or click ok
    public void create_alert_all_images() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Add an image first!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
