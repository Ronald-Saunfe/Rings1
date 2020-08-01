package com.example.rings.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.example.rings.EnvironVar.globalenviron;
import com.example.rings.R;

import org.json.JSONException;
import org.json.JSONObject;

public class national extends Fragment {

    View vv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vv = inflater.inflate(R.layout.fragment_national, container, false);

        return vv;
    }

    private void loadrings(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            //object.put("email", txtedtemail.getText().toString() );
            //object.put("password", txtedttpass.getText().toString());
            object1.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "https://us-central1-rings-c715d.cloudfunctions.net/Login";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response====="+response);
                        try {
                            int code = response.getInt("code");
                            String consumerkey = response.getString("CONSUMER_KEY");
                            String consumersecret = response.getString("CONSUMER_SECRET");

                            if (code == 200){

                            }
                            else{
                            }
                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}