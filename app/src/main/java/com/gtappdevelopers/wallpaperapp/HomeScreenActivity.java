package com.gtappdevelopers.wallpaperapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeScreenActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {
    //creating variables for recyclerview,progress bar, adapter and array list, edittext and others.
    private RecyclerView categoryRV, wallpaperRV;
    private ProgressBar loadingPB;
    private ArrayList<CategoryRVModal> categoryRVModals;
    private ArrayList<String> wallpaperArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;
    private EditText searchEdt;
    private ImageView searchIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //initializing all variables on below line.
        categoryRV = findViewById(R.id.idRVCategories);
        wallpaperRV = findViewById(R.id.idRVWallpapers);
        searchEdt = findViewById(R.id.idEdtSearch);
        searchIV = findViewById(R.id.idIVSearch);
        loadingPB = findViewById(R.id.idPBLoading);
        wallpaperArrayList = new ArrayList<>();
        categoryRVModals = new ArrayList<>();
        //creating a layout manager for recycler view which is our category.
        LinearLayoutManager manager1 = new LinearLayoutManager(HomeScreenActivity.this, RecyclerView.HORIZONTAL, false);
        //initializing our adapter class on below line.
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModals, this, this);
        //setting layout manager to our category recycler view as horizontal.
        categoryRV.setLayoutManager(manager1);
        categoryRV.setAdapter(categoryRVAdapter);
        //creating a grid layout manager for our wallpaper recycler view.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //setting layout manager and adapter to our recycler view.
        wallpaperRV.setLayoutManager(layoutManager);
        wallpaperRV.setAdapter(wallpaperRVAdapter);
        //on below line we are calling method to get categories to add data in array list.
        getCategories();
        //on below line we are calling get wallpaper method to get data in wallpaper array list.
        getWallpapers();
        //on below line we are adding on click listner for search image view on below line.
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inside on click method we are getting data from our search edittext and validating if the input field is empty or not.
                String searchStr = searchEdt.getText().toString();
                if (searchStr.isEmpty()) {
                    Toast.makeText(HomeScreenActivity.this, "Please enter something to search", Toast.LENGTH_SHORT).show();
                } else {
                    //on below line we are calling a get wallpaper method to get wallpapers by category.
                    getWallpapersByCategory(searchStr);
                }
            }
        });
    }

    //on below line we are creating a method to get the wallpaper by category.
    private void getWallpapersByCategory(String category) {
        //on below line we are clearing our array list.
        wallpaperArrayList.clear();
        //on below line we are making visibility of our progress bar as gone.
        loadingPB.setVisibility(View.VISIBLE);
        //on below line we are creating a string variable for our url and adding url to it.
        String url = "https://api.pexels.com/v1/search?query=" + category + "&per_page=30&page=1";
        //on below line we are creating a new variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(HomeScreenActivity.this);
        //on below line we are making a json object request to get the data from url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
                try {
                    loadingPB.setVisibility(View.GONE);
                    //on below line we are extracting json data.
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        //on below line we are passing data to our array list
                        wallpaperArrayList.add(imgUrl);
                    }
                    //here we are notifying adapter that data has changed in our list.
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying a simple toast message on error response.
                Toast.makeText(HomeScreenActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Add your API key");
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

    }

    private void getWallpapers() {
        //on below line we are clearing our array list.
        wallpaperArrayList.clear();
        //changing visiblity of our progress bar to gone.
        loadingPB.setVisibility(View.VISIBLE);
        //creating a variable for our url.
        String url = "https://api.pexels.com/v1/curated?per_page=30&page=1";
        //on below line we are creating a new variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(HomeScreenActivity.this);
        //on below line we are making a json object request to get the data from url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //on below line we are extracting the data from our response and passing it to our array list.
                loadingPB.setVisibility(View.GONE);
                try {
                    //on below line we are extracting json data.
                    JSONArray photos = response.getJSONArray("photos");
                    for (int i = 0; i < photos.length(); i++) {
                        JSONObject photoObj = photos.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        //on below line we are passing data to our array list
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //handling json exception on below line.
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displaying a toast message on error respone.
                Toast.makeText(HomeScreenActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                //in this method passing headers as key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "563492ad6f917000010000010e57141f30b2437cb7746c1519d63ee5");
                //at last returning headers.
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void getCategories() {
        //on below lines we are adding data to our category array list.
        categoryRVModals.add(new CategoryRVModal("Technology", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fHRlY2hub2xvZ3l8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModals.add(new CategoryRVModal("Programming", "https://images.unsplash.com/photo-1542831371-29b0f74f9713?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZ3JhbW1pbmd8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
        categoryRVModals.add(new CategoryRVModal("Nature", "https://images.pexels.com/photos/2387873/pexels-photo-2387873.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Travel", "https://images.pexels.com/photos/672358/pexels-photo-672358.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Architecture", "https://images.pexels.com/photos/256150/pexels-photo-256150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Arts", "https://images.pexels.com/photos/1194420/pexels-photo-1194420.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Music", "https://images.pexels.com/photos/4348093/pexels-photo-4348093.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Abstract", "https://images.pexels.com/photos/2110951/pexels-photo-2110951.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Cars", "https://images.pexels.com/photos/3802510/pexels-photo-3802510.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        categoryRVModals.add(new CategoryRVModal("Flowers", "https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
    }

    @Override
    public void onCategoryClick(int position) {
        //on below line we are getting category from our array list and calling a method to get wallpapers by category.
        String category = categoryRVModals.get(position).getCategory();
        getWallpapersByCategory(category);
    }
}