package com.example.rings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class loginScreen extends AppCompatActivity {

    TextView txtvSignup, txtloginstatelogin;
    Button btnlogin,btnlsignup,btnlresetpass;
    View coordlogin, vwBg, txtlogin, vwloginindicator, imgsplash, txt, txtedtemail1;
    TextInputEditText txtedtemail,txtedttpass;
    ProgressBar pbloadlogin;
    LinearLayout lnloginNot;
    TextInputLayout txtedtemaillayout, txtedttpasslayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        btnlogin = findViewById(R.id.btnlogin);
        coordlogin = findViewById(R.id.coordlogin);
        vwBg = findViewById(R.id.vwBg);
        txtedtemail = findViewById(R.id.txtedtemail);
        txtedttpass = findViewById(R.id.txtedttpass);
        pbloadlogin = findViewById(R.id.pbloadlogin);
        txtloginstatelogin = findViewById(R.id.txtloginstatelogin);
        lnloginNot = findViewById(R.id.lnloginNot);
        btnlsignup = findViewById(R.id.btnlsignup);
        txtlogin = findViewById(R.id.txtlogin);
        vwloginindicator = findViewById(R.id.vwloginindicator);
        imgsplash = findViewById(R.id.imgsplash);
        txt = findViewById(R.id.txt);
        txtedtemail1 = findViewById(R.id.txtedtemail);
        txtedtemaillayout = findViewById(R.id.txtedtemaillayout);
        txtedttpasslayout = findViewById(R.id.txtedttpasslayout);
        btnlresetpass = findViewById(R.id.btnlresetpass);

        //Start sign up activity
        btnlsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignup();
            }
        });

        Spannable span = Spannable.Factory.getInstance().newSpannable("Sign up instead");
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loginScreen.this, Signup.class);

                startActivity(intent);
            } }, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtedtemail.getText().toString().equals("") && !txtedttpass.getText().toString().equals("") ){
                    //show loading
                    btnlogin.setEnabled(false);
                    btnlogin.setText("");
                    pbloadlogin.setVisibility(View.VISIBLE);
                    txtedtemaillayout.setError(null);
                    txtedttpasslayout.setError(null);
                    lnloginNot.setVisibility(View.GONE);

                    signin();
                }
                else{
                    // show error
                    if (txtedtemail.getText().toString().equals("")){
                        txtedtemaillayout.setError("Enter email address");
                    }
                    else if(txtedttpass.getText().toString().equals("") ){
                        txtedttpasslayout.setError("Enter password");
                    }
                }
            }
        });

        btnlresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReset();
            }
        });
    }

    public void showReset(){
        Intent intent = new Intent(loginScreen.this, resetpassword.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Pair<View, String> p1 = Pair.create(txtlogin, "txtlogin");
            Pair<View, String> p2 = Pair.create(vwloginindicator, "vwloginindicator");
            Pair<View, String> p3 = Pair.create(imgsplash, "img");
            Pair<View, String> p4 = Pair.create(txt, "txt");
            Pair<View, String> p5 = Pair.create(txtedtemail1, "txtedtemail");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(loginScreen.this,
                            p1, p2, p3, p4, p5
                    );
            startActivity(intent, options.toBundle());
        }
        else{
            startActivity(intent);
        }
    }


    public void signin(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("email", txtedtemail.getText().toString() );
            object.put("password", txtedttpass.getText().toString());
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
                                txtedtemaillayout.setError(null);
                                txtedttpasslayout.setError(null);
                                globalenviron.myname = response.getString("Username");
                                globalenviron.myemail = txtedtemail.getText().toString();

                                //For Sandbox Mode
                                globalenviron.daraja = Daraja.with(consumerkey, consumersecret, new DarajaListener<AccessToken>() {
                                    @Override
                                    public void onResult(@NonNull AccessToken accessToken) {
                                        globalenviron.accesstoken = accessToken.getAccess_token();
                                        showMenu();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Toast.makeText(getApplicationContext(),"Error occurred: "+error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                lnloginNot.setVisibility(View.VISIBLE);
                                txtloginstatelogin.setText("Email or password is incorrect");
                                btnlogin.setEnabled(true);
                                btnlogin.setText("Login");
                                pbloadlogin.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnlogin.setEnabled(true);
                btnlogin.setText("Login");
                pbloadlogin.setVisibility(View.GONE);
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void showSignup(){
        Intent intent = new Intent(loginScreen.this, Signup.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Pair<View, String> p1 = Pair.create(txtlogin, "txtlogin");
            Pair<View, String> p2 = Pair.create(vwloginindicator, "vwloginindicator");
            Pair<View, String> p3 = Pair.create(imgsplash, "img");
            Pair<View, String> p4 = Pair.create(txt, "txt");
            Pair<View, String> p5 = Pair.create(txtedtemail1, "txtedtemail");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(loginScreen.this,
                            p1, p2, p3, p4, p5
                    );
            startActivity(intent, options.toBundle());
        }
        else{
            startActivity(intent);
        }
    }

    public void showMenu(){
        Intent intent = new Intent(loginScreen.this, menuscreen.class);
        startActivity(intent);
        finish();
    }
}