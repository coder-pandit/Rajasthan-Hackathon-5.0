package com.example.kumar.mharorajasthan;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackagesFragment extends Fragment {

    private static final String TAG = "PackagesFragment";
    private static final String URL = "https://api.sygictravelapi.com/1.1/en/tours/viator?parent_place_id=region:271";


    private List<Place> packageList;

    private RecyclerView packageListView;

    public PackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_packages, container, false);

        packageListView = view.findViewById(R.id.package_list);

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int maxLogSize = 1000;
                for(int i = 0; i <= response.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i+1) * maxLogSize;
                    end = end > response.length() ? response.length() : end;
                    Log.d(TAG, response.substring(start, end));
                }
                // Log.d(TAG, "onResponse: " + response);
                // GsonBuilder builder = new GsonBuilder();
                // Gson gson = builder.create();
                // try {
                //     JSONObject root = new JSONObject(response);
                //     JSONObject data = root.getJSONObject("data");
                //     Data data1 = gson.fromJson(data.toString(), Data.class);
                //     placesList = new ArrayList<>(data1.getPlaces());
                //
                //     mAdapter = new HomeAdapter(getContext(), placesList);
                //     placesListView.setAdapter(mAdapter);
                // } catch (JSONException e) {
                //     e.printStackTrace();
                // }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("x-api-key", "YYiClh1fKL1PHoiFAo01X7O51SuS8HfJMH0E7Wn2");
                return header;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);


        return view;
    }

}
