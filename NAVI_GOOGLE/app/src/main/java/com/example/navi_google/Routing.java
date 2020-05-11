package com.example.navi_google;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class Routing extends Application {

    private static final String baseURL = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String TAG = "Routing";
    public JSONObject mJSONresult;
    private static Routing mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public Routing()
    {
        mJSONresult = new JSONObject();
    }

    public void getRoute(LatLng orig, LatLng dest, String apiKey)
    {
        String requestURL = baseURL +
                String.format("origin=%s&destination=%s&mode=walking&key=%s",
                        orig.toString(),dest.toString(), apiKey);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "getRoute Request response:" + response.toString());
                        mJSONresult = response;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "getRoute Request error:" + error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(mInstance).addToRequestQueue(jsonObjectRequest);
    }

    public int parseResult()
    {
        if (mJSONresult.length() == 0) return -1;
        try{
            JSONObject route;
            if (mJSONresult.has("status") && mJSONresult.has("routes"))
            {
                if (mJSONresult.getString("status") == "OK"){
                    route = mJSONresult.getJSONObject("routes");
                }
                else {
                    Log.i(TAG, "parseResult Error: json object is empty");
                    return -1;
                }
            }

        } catch (JSONException e) {
            Log.i(TAG, "parseResult Error:" + e.toString());
        }
        return 0;
    }

}
