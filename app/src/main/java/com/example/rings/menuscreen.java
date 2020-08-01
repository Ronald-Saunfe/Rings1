package com.example.rings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.Visibility;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;
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
import com.example.rings.Adapters.myringadaptor;
import com.example.rings.EnvironVar.globalenviron;
import com.example.rings.model.myringmodel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class menuscreen extends AppCompatActivity implements myringadaptor.OnShareClickedListener{

    View bgdeposit, Cddepmoney,txtdepositmenu, cmdepoitmoney,txtwithdrawmenu, bgwithdraw;
    TextView txtgreetings, txtmyrings;
    SpeedDialView speedDial;
    Scene homescene,profilescene,settings;
    BottomNavigationView bnv;
    ViewGroup viewhome, viewprofile, viewsettings;
    RecyclerView rcvmyrings;
    LinearLayoutManager layoutManager;
    List<myringmodel> myDataset;
    myringadaptor mAdapter;
    AppBarLayout appblmenu;
    ProgressBar progressbmenu;
    float density;
    ShimmerFrameLayout shimmer_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuscreen);

        // Create the scene root for the scenes in this app
        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.coodmenu);
        // Get the root view and create a transition

        density = getApplicationContext().getResources().getDisplayMetrics().density;

        viewhome =  findViewById(R.id.includeHome);
        viewprofile =  findViewById(R.id.includeprofile);
        viewsettings =  findViewById(R.id.includesettings);

        viewhome.setVisibility(View.VISIBLE);

        Fade mFade = new Fade(Fade.IN);
        mFade.addTarget(viewhome);

        // Start recording changes to the view hierarchy
        TransitionManager.beginDelayedTransition(sceneRoot, mFade);

        homescene = new Scene(sceneRoot, viewhome);
        profilescene = new Scene(sceneRoot, viewhome);
        settings = new Scene(sceneRoot, viewhome);

        Cddepmoney = viewhome.findViewById(R.id.Cddepmoney);
        bgdeposit = viewhome.findViewById(R.id.bgdeposit);
        txtdepositmenu = viewhome.findViewById(R.id.txtdepositmenu);

        cmdepoitmoney = viewhome.findViewById(R.id.cmdepoitmoney);
        txtwithdrawmenu = viewhome.findViewById(R.id.txtwithdrawmenu);
        bgwithdraw = viewhome.findViewById(R.id.bgwithdraw);

        txtgreetings = viewhome.findViewById(R.id.txtgreetings);
        appblmenu = viewhome.findViewById(R.id.appblmenu);
        progressbmenu = viewhome.findViewById(R.id.progressbmenu);

        rcvmyrings = viewhome.findViewById(R.id.rcvmyrings);
        txtmyrings = viewhome.findViewById(R.id.txtmyrings);

        shimmer_view = viewhome.findViewById(R.id.shimmer_view);

        //get the users rings
        myDataset = new ArrayList<myringmodel>();
        mAdapter = new myringadaptor(getApplicationContext(),this,myDataset);
        rcvmyrings.setAdapter(mAdapter);
        getusersrings();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rcvmyrings.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcvmyrings.setLayoutManager(layoutManager);
        rcvmyrings.setItemAnimator(new DefaultItemAnimator());

        bnv = findViewById(R.id.bnv);

        txtgreetings.setText("Hello "+globalenviron.myname+",");

        Cddepmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuscreen.this, depositmoney.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Pair<View, String> p1 = Pair.create(bgdeposit, "bgdeposit");
                    Pair<View, String> p2 = Pair.create(Cddepmoney, "Cddepmoney");
                    Pair<View, String> p3 = Pair.create(txtdepositmenu, "txtdepositmenu");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(menuscreen.this,
                                    p1,p2, p3
                            );
                    startActivity(intent, options.toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });

        cmdepoitmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuscreen.this, withdrawmoney.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Pair<View, String> p1 = Pair.create(cmdepoitmoney, "cmdepoitmoney");
                    Pair<View, String> p2 = Pair.create(txtwithdrawmenu, "txtwithdrawmenu");
                    Pair<View, String> p3 = Pair.create(bgwithdraw, "bgwithdraw");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(menuscreen.this,
                                    p1,p2, p3
                            );
                    startActivity(intent, options.toBundle());
                }
                else{
                    startActivity(intent);
                }
            }
        });

        speedDial = viewhome.findViewById(R.id.speedDial);
        speedDial.inflate(R.menu.ringoptions);
        speedDial.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.createring:
                        Intent intent = new Intent(menuscreen.this, createring.class);
                        startActivity(intent);
                        return false; // true to keep the Speed Dial open
                    case R.id.joinring:
                        Intent intent1 = new Intent(menuscreen.this, joinring.class);
                        startActivity(intent1);
                        return false; // true to keep the Speed Dial open
                    case R.id.train:
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewhome.setVisibility(View.VISIBLE);
                        viewprofile.setVisibility(View.GONE);
                        return true;
                    case R.id.profile:
                        viewhome.setVisibility(View.GONE);
                        viewprofile.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.settings:
                        viewhome.setVisibility(View.GONE);
                        viewprofile.setVisibility(View.GONE);
                        return true;
                }

                return true;
            }
        });
    }

    private void getusersrings(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("email", globalenviron.myemail );
            object1.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Enter the correct url for your api service site
        String url = "https://us-central1-rings-c715d.cloudfunctions.net/getmyrings";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response====="+response);
                        try {
                            int code = response.getInt("code");

                            if (code == 200){
                                // specify an adapter (see also next example)
                                JSONArray ringlist = response.getJSONArray("ringlist");
                                System.out.println("=========ringlist===="+ringlist);

                                for(int i=0; i<ringlist.length();i++){
                                    JSONObject ring_rec = ringlist.getJSONObject(i);
                                    System.out.println("=========ring_rec===="+ring_rec);
                                    System.out.println("=========i===="+i+"====size=="+ringlist.length());

                                    myringmodel m = new myringmodel();
                                    m.setRingname(ring_rec.getString("ring_name"));
                                    m.setCurrency(ring_rec.getString("ring_currency"));
                                    m.setPricetag(ring_rec.getString("ring_price_tag"));
                                    myDataset.add(m);
                                    mAdapter.notifyItemInserted(i);
                                }

                                if (ringlist.length()>=1){
                                    progressbmenu.setVisibility(View.GONE);
                                    ViewGroup.LayoutParams params = appblmenu.getLayoutParams();
                                    //params.height += dpToPx(160);
                                    appblmenu.setLayoutParams(params);
                                    shimmer_view.setVisibility(View.GONE);
                                    txtmyrings.setVisibility(View.VISIBLE);
                                    rcvmyrings.setVisibility(View.VISIBLE);
                                }
                                else {
                                    shimmer_view.setVisibility(View.GONE);
                                    progressbmenu.setVisibility(View.GONE);
                                    ViewGroup.LayoutParams params = appblmenu.getLayoutParams();
                                    params.height -= dpToPx(24);
                                    appblmenu.setLayoutParams(params);
                                }

                            }
                            else{
                                shimmer_view.setVisibility(View.GONE);
                                progressbmenu.setVisibility(View.GONE);
                                ViewGroup.LayoutParams params = appblmenu.getLayoutParams();
                                params.height -= dpToPx(24);
                                appblmenu.setLayoutParams(params);
                            }
                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void cardcliked(int position) {
        System.out.println("=====clicked position========"+position);
    }

    @Override
    public void menuclicked(MenuItem menuname, int position, View vw, LinearLayout ln, ImageButton options, List<myringmodel> mDataset) {
        if (menuname.getTitle().equals("Delete")){
            vw.setVisibility(View.VISIBLE);
            ln.setVisibility(View.VISIBLE);
            options.setEnabled(false);
            deletering(position, vw, ln, options, mDataset);
        }
    }

    private void deletering(final int position, final View vw, final LinearLayout ln, final ImageButton options, List<myringmodel> mdt){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("ring_name", mdt.get(position).getRingname() );
            object.put("email", globalenviron.myemail);
            object1.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "https://us-central1-rings-c715d.cloudfunctions.net/deletering";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response====="+response);
                        try {
                            int code = response.getInt("code");

                            if (code == 200){
                                myDataset.remove(position);
                                mAdapter.notifyItemRemoved(position);
                                if (myDataset.size()==0){
                                    ViewGroup.LayoutParams params = appblmenu.getLayoutParams();
                                    params.height -= dpToPx(160);
                                    appblmenu.setLayoutParams(params);
                                    txtmyrings.setVisibility(View.GONE);
                                    rcvmyrings.setVisibility(View.GONE);
                                }
                            }
                            else{
                                vw.setVisibility(View.GONE);
                                ln.setVisibility(View.GONE);
                                options.setEnabled(true);
                                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                vw.setVisibility(View.GONE);
                ln.setVisibility(View.GONE);
                options.setEnabled(true);
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}