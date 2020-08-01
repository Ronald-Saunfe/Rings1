package com.example.rings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.rings.EnvironVar.globalenviron;
import com.example.rings.Network.MySingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class splashScreen extends AppCompatActivity  implements DroidListener  {

    View vw, imgv, txtsplash,vwLoad;
    ProgressBar pbsplash;
    Button btnTryAGainSplash;
    LinearLayout lnNet;
    private DroidNet mDroidNet;
    boolean netavailable;
    AppCompatActivity actv;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txtsplash = findViewById(R.id.txtsplash);
        vw = findViewById(R.id.vwBg1);
        imgv = findViewById(R.id.imgsplash);
        vwLoad = findViewById(R.id.vwLoad);
        pbsplash = findViewById(R.id.pbsplash);
        btnTryAGainSplash = findViewById(R.id.btnTryAGainSplash);
        lnNet = findViewById(R.id.lnNet);

        actv = this;

        txtsplash.post(new Runnable() {
            @Override
            public void run() {
                vwLoad.animate().alpha(1f)
                        .setDuration(300)
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        pbsplash.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .setInterpolator(new DecelerateInterpolator())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                        final Handler handler= new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkInternetConnection();
                                            }
                                        },2000);
                                    }
                                });
                    }
                });

            }
        });

        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);

        btnTryAGainSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
            }
        });
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            //do Stuff with internet
            netavailable=true;
        } else {
            //no internet
            netavailable=false;
        }
    }

    public void checkInternetConnection(){
        if (netavailable==false){
            pbsplash.animate()
                    .setDuration(800)
                    .alpha(0f)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            vwLoad.animate()
                                    .alpha(0f)
                                    .setInterpolator(new DecelerateInterpolator())
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            lnNet.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    });
        }
        else {
            lnNet.setVisibility(View.GONE);
            pbsplash.setAlpha(1f);
            vwLoad.setAlpha(1f);
            checkupdate();
        }
    }

    private void checkupdate(){
        String url = "https://us-central1-rings-c715d.cloudfunctions.net/CheckUpdate";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            System.out.println("=====response====="+response);
                            String name = response.getString("versionCode");
                            JSONObject jsonObj1 = new JSONObject(name);
                            System.out.println("=====response1====="+name);
                            String name2 = jsonObj1.getString("versionCode");
                            float versionCode=Float.parseFloat(name2);

                            //get the versionCode
                            //check if the app is upto date
                            float AppversionCode = BuildConfig.VERSION_CODE;
                            if (versionCode != AppversionCode)
                            {
                                //start the update activity
                                Intent intt = new  Intent(getApplicationContext(),updatescreen.class);
                                intt.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intt);
                            }
                            else
                            {
                                checklastloginin();
                            }
                        }
                        catch (JSONException err){
                            System.out.println("==========json excpeion========"+err.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO: Handle error
                        Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    public void checklastloginin(){
        showLogin();
    }

    public void showLogin(){
        Intent intent = new Intent(splashScreen.this, loginScreen.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Pair<View, String> p1 = Pair.create(vw, "toolb");
            Pair<View, String> p2 = Pair.create(txtsplash, "txt");
            Pair<View, String> p3 = Pair.create(imgv, "img");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(splashScreen.this,
                            p1,p2,p3
                    );
            startActivity(intent, options.toBundle());
        }
        else{
            startActivity(intent);
        }
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }
}