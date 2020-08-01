package com.example.rings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class resetpassword extends AppCompatActivity {

    Button btnresetpass;
    ProgressBar pbloadloginrestpass;
    TextInputLayout txtedtemaillayoutresetpass, txtedttcpasslayoutresetpass, txtedttpasslayoutresetpass;
    TextInputEditText txtedtemailresetemail, txtedttcpassresetpass, txtedttpassresetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        btnresetpass = findViewById(R.id.btnresetpass);
        pbloadloginrestpass = findViewById(R.id.pbloadloginrestpass);

        txtedtemaillayoutresetpass = findViewById(R.id.txtedtemaillayoutresetpass);
        txtedtemailresetemail = findViewById(R.id.txtedtemailresetemail);

        txtedttcpasslayoutresetpass = findViewById(R.id.txtedttcpasslayoutresetpass);
        txtedttcpassresetpass = findViewById(R.id.txtedttcpassresetpass);

        txtedttpasslayoutresetpass = findViewById(R.id.txtedttpasslayoutresetpass);
        txtedttpassresetpass = findViewById(R.id.txtedttpassresetpass);

        btnresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnresetpass.getText().toString().equals("Send reset link")){
                    if (txtedtemailresetemail.getText().toString().equals("")){
                        txtedtemaillayoutresetpass.setError("Enter email address");
                    }
                    else{
                        txtedtemaillayoutresetpass.setError(null);
                        btnresetpass.setEnabled(false);
                        btnresetpass.setText("");
                        pbloadloginrestpass.setVisibility(View.VISIBLE);
                        checkemailexistance();
                    }
                }
                else{
                    if (txtedttpassresetpass.getText().toString().equals("")){
                        txtedttpasslayoutresetpass.setError("Enter password");
                    }
                    else if (txtedttcpassresetpass.getText().toString().equals("")){
                        txtedttcpasslayoutresetpass.setError("Enter confirm password");
                    }
                    else if (!txtedttcpassresetpass.getText().toString().equals(txtedttpassresetpass.getText().toString())){
                        txtedttpasslayoutresetpass.setError("Password does not match");
                    }
                    else{
                        txtedttpasslayoutresetpass.setError(null);
                        txtedttcpasslayoutresetpass.setError(null);
                        //resetpassword();
                    }
                }


            }
        });

        //check if the activity is opened from a url
        //if so enter a new password for the specified email
        Intent mainIntent = getIntent();
        if (mainIntent!=null && mainIntent.getData()!=null
                && (mainIntent.getData().getScheme().equals("http"))){
            Uri data = mainIntent.getData();
            List<String> pathSegments = data.getPathSegments();
            if(pathSegments.size()>0){
                System.out.println("=====segment email===0===="+pathSegments.get(0));
            }
            //update the ui accordingly
            txtedtemaillayoutresetpass.setVisibility(View.GONE);
            btnresetpass.setText("Change password");

            txtedttcpasslayoutresetpass.setVisibility(View.VISIBLE);
            txtedttpasslayoutresetpass.setVisibility(View.VISIBLE);
        }
    }

    //todo security update the record when updating the password
    private void resetpassword(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("email", txtedtemailresetemail.getText().toString() );
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

                        try {
                            int code = 0;
                            code = response.getInt("code");

                            if (code == 200){
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gmail.com"));
                                startActivity(browserIntent);
                            }
                            else if(code ==101){
                                txtedtemaillayoutresetpass.setError("Email does not exist");
                            }

                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnresetpass.setEnabled(true);
                btnresetpass.setText("Send reset link");
                pbloadloginrestpass.setVisibility(View.GONE);
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void checkemailexistance(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("email", txtedtemailresetemail.getText().toString() );
            object1.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "https://us-central1-rings-c715d.cloudfunctions.net/sendresetemail";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response====="+response);

                        try {
                            int code = 0;
                            code = response.getInt("code");

                            if (code == 200){
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gmail.com"));
                                startActivity(browserIntent);
                            }
                            else if(code ==101){
                                txtedtemaillayoutresetpass.setError("Email does not exist");
                            }

                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnresetpass.setEnabled(true);
                btnresetpass.setText("Send reset link");
                pbloadloginrestpass.setVisibility(View.GONE);
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}