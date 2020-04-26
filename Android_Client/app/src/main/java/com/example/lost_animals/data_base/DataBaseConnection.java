package com.example.lost_animals.data_base;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lost_animals.custom_views.GoogleMarkerAddition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class DataBaseConnection {
    private static RequestQueue requestQueue;
    public static void setMarkers(final GoogleMap map, Context context, LatLng latLng, float distance){
        requestQueue = Volley.newRequestQueue(context);
        String url = ServerSettings.ROOT_URL+"get_markers/?dist_in_metres=2000&lat="+latLng.latitude+"&lon="+latLng.longitude;
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            Log.d("Tag",response.toString(4));
                            for(int i = 0 ; i < jsonArray.length(); i ++){
                                JSONObject marker_info_json = jsonArray.getJSONObject(i);
                                LatLng latLng1 = new LatLng(marker_info_json.getDouble("latitude"),marker_info_json.getDouble("longitude"));
                                MarkerInfo markerInfo = new MarkerInfo(latLng1,marker_info_json.getString("url"));
                                GoogleMarkerAddition.addMarker(map,markerInfo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonRequest);
    }
}
