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
import android.widget.EditText;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Initialization of all of the buttons on the search for image page
    private Button searchbutton; //the Add to List button
    private Button nextbutton;
    private Button previousbutton;
    private Button savebutton;
    private Button view_allbutton;

    private EditText editsearch; //the search edit text box

    //Initialization of webview to load url's into the view
    private WebView webView;

    //Initialization of array list of all all saved images url (grabbed from db), and then index j to go through the list
    private ArrayList<String> array_images;
    public int j;

    //Calling FeedReaderdbHelper's dbhelper function
    private FeedReaderdbHelper dbhelper;

    //ONCREATE: Begin
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //Connects webview variable with webview from layout, editsearch with the edittext box, and dbhelper from FeedReaderdbHelper
        editsearch = (EditText) rootView.findViewById(R.id.inputsearch);
        webView = (WebView) rootView.findViewById(R.id.viewimage);
        dbhelper = new FeedReaderdbHelper(getActivity()); //readDB is connected to FeedReaderDbHelper in this function

        //Make an array of images that will be filled with the results of the custom search, and initialize j to be 0
        array_images = new ArrayList<String>();
        j = 0;

        //When search button is clicked, the text typed into the edittext field will be obtained and then put into the
        //GoogleImageGrabber class, which will output from callback an arraylist of google images url from custom search
        searchbutton = (Button) rootView.findViewById(R.id.search_button);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editsearch.getText().toString(); //grabs the particular item name string
                GoogleImageGrabber handler = new GoogleImageGrabber(getActivity());
                //checks that there is even a string
                if (str.length() == 0){
                    create_alert_search();
                }
                else {
                    handler.searchGoogle(str, new searchcallback() {
                        @Override
                        public void callback(ArrayList<String> googleimagesreturn) {
                            array_images = googleimagesreturn; //fill our arraylist with the output of the callback
                            webView.loadUrl(googleimagesreturn.get(j)); //load the first (0th) image of the arraylist on the webview
                            Log.d("Success", googleimagesreturn.toString());
                        }
                    });
                }
            }
        });

        //If next button is clicked, there are different scenarios that could happen
        //One: 0 images in the arraylist (from not searching) -> alert dialog notifying user to search for an image pops up
        //Two: j is at the last image of the list -> go to the 0th image
        //Three: All other j index values -> load the (j+1)th image
        nextbutton = (Button) rootView.findViewById(R.id.next_image);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_images.size() == 0) {
                    create_alert_search();
                }
                else if (array_images.size() - 1 == j){
                    j = 0;
                    webView.loadUrl(array_images.get(j));
                }
                else {
                    j += 1;
                    webView.loadUrl(array_images.get(j));
                    Log.v("Yes:", Integer.toString(j));
                }
            }
        });

        //If previous button is clicked, there are different scenarios that could happen:
        //One: 0 images saved -> popup alert dialog again
        //Two: j is at the first image of the list -> go to the last image of list
        //Three: All other j index values -> load the (j-1)th image
        previousbutton = (Button) rootView.findViewById(R.id.previous_image);
        previousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_images.size() == 0) {
                    create_alert_search();
                }
                else if (j == 0){
                    j = array_images.size()-1;
                    webView.loadUrl(array_images.get(j));
                }
                else {
                    j -= 1;
                    webView.loadUrl(array_images.get(j));
                    Log.v("Before:", Integer.toString(j));
                }
            }
        });

        //When the savebutton is clicked when there was no search, popup appears
        //Else: get that particular image url and write it into the database using dbhelper
        savebutton = (Button) rootView.findViewById(R.id.save_button);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_images.size() == 0){
                    create_alert_search();
                }
                else {
                    dbhelper.writeData(array_images.get(j));
                }
            }
        });

        //When you want to switch layout back to view all images: Uses the main activity
        //function "transitionToFragment", with the AllImagesFragment fragment
        view_allbutton = (Button) rootView.findViewById(R.id.view_all_button);
        view_allbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllImagesFragment fr = new AllImagesFragment();
                ((MainActivity)getActivity()).transitionToFragment(fr);
            }
        });

        return rootView;
    }

    //Function that creates the alert dialog and exits out of it when you click out or click ok
    public void create_alert_search() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Search for something first!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
