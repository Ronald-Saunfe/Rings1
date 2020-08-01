package com.example.rings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rings.Network.MySingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    Button btnsignup;
    ProgressBar pbloadsignup;
    TextInputEditText txtedemailsignup, txtedpasswdsignup, txtedcpasswdsignup, txtedusernamesignup;
    TextInputLayout txtedemailsignuplayout, txtedpasswdsignuplayout, txtedcpasswdsignuplayout,txtedusernamesignuplayout;
    View coodsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnsignup = findViewById(R.id.btnsignup);
        pbloadsignup = findViewById(R.id.pbloadsignup);

        txtedemailsignup = findViewById(R.id.txtedemailsignup);
        txtedpasswdsignup = findViewById(R.id.txtedpasswdsignup);
        txtedcpasswdsignup = findViewById(R.id.txtedcpasswdsignup);
        txtedusernamesignup = findViewById(R.id.txtedusernamesignup);

        txtedemailsignuplayout = findViewById(R.id.txtedemailsignuplayout);
        txtedpasswdsignuplayout = findViewById(R.id.txtedpasswdsignuplayout);
        txtedcpasswdsignuplayout = findViewById(R.id.txtedcpasswdsignuplayout);
        txtedusernamesignuplayout = findViewById(R.id.txtedusernamesignuplayout);

        //create an account
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ensure email not empty
                if (txtedemailsignup.getText().toString().equals("")){
                    txtedemailsignuplayout.setError("Enter email address");
                }
                //ensure password not empty
                else if(txtedpasswdsignup.getText().toString().equals("")){
                    txtedpasswdsignuplayout.setError("Enter password");
                }
                //ensure Cpassword not empty
                else if(txtedcpasswdsignup.getText().toString().equals("")){
                    txtedcpasswdsignuplayout.setError("Enter Confirm password");
                }
                //ensure username not empty
                else if(txtedusernamesignup.getText().toString().equals("")){
                    txtedusernamesignuplayout.setError("Enter username");
                }
                else if(!txtedcpasswdsignup.getText().toString().equals(txtedpasswdsignup.getText().toString())){
                    txtedpasswdsignuplayout.setError("Password dont match!");
                }
                else{
                    txtedemailsignuplayout.setError(null);
                    txtedpasswdsignuplayout.setError(null);
                    txtedcpasswdsignuplayout.setError(null);
                    txtedusernamesignuplayout.setError(null);

                    btnsignup.setEnabled(false);
                    btnsignup.setText("");
                    pbloadsignup.setVisibility(View.VISIBLE);

                    CreateAcc();
                }
            }
        });

    }

    // Post Request For JSONObject
    public void CreateAcc() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("email", txtedemailsignup.getText().toString() );
            object.put("Username",txtedpasswdsignup.getText().toString() );
            object.put("password", txtedpasswdsignup.getText().toString());
            object.put("Cpassword", txtedcpasswdsignup.getText().toString());
            object1.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "https://us-central1-rings-c715d.cloudfunctions.net/signup";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response====="+response);
                        int code = 0;
                        try {
                            code = response.getInt("code");
                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }

                        if (code == 200){
                            txtedemailsignuplayout.setError(null);
                            txtedpasswdsignuplayout.setError(null);
                            showMenu();
                        }
                        else if(code ==555){
                            txtedemailsignuplayout.setError("Email already exist");
                        }
                        else if(code ==504){
                            txtedemailsignuplayout.setError("Password do not match");
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnsignup.setEnabled(true);
                btnsignup.setText("Create");
                pbloadsignup.setVisibility(View.GONE);
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void showMenu(){
        Intent intent = new Intent(Signup.this, menuscreen.class);
        startActivity(intent);
        finish();

    }
}