package com.example.rings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rings.Behaviors.StickyBottomBehavior;
import com.example.rings.EnvironVar.globalenviron;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.NoSuchElementException;


public class createring extends AppCompatActivity {

    Button btnCreatering;
    ProgressBar pgbloadcring;
    Spinner spinncurrency, spinnpricetag;
    TextInputEditText tietname;
    TextInputLayout tietnamelay;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    ConstraintLayout coonstbtn, constanchor;
    ImageView imgdpring;
    Uri imageUri;
    AppCompatCheckBox rule1,rule2,rule3,rule4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createring);
        View anchor = findViewById(R.id.constanchor);
        btnCreatering = findViewById(R.id.btnCreatering);
        pgbloadcring = findViewById(R.id.pgbloadcring);
        tietname = findViewById(R.id.tietname);
        tietnamelay = findViewById(R.id.tietnamelay);
        coonstbtn = findViewById(R.id.coonstbtn);
        imgdpring = findViewById(R.id.imgdpring);
        rule1 = findViewById(R.id.rule1);
        rule2 = findViewById(R.id.rule2);
        rule3 = findViewById(R.id.rule3);
        rule4 = findViewById(R.id.rule4);
        constanchor = findViewById(R.id.constanchor);


        ((CoordinatorLayout.LayoutParams) coonstbtn.getLayoutParams())
                .setBehavior(new StickyBottomBehavior(R.id.constanchor, getResources().getDimensionPixelOffset(R.dimen.margins)));

        spinncurrency = (Spinner) findViewById(R.id.spinncurrency);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ringscurrency, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinncurrency.setAdapter(adapter);

        spinnpricetag = (Spinner) findViewById(R.id.spinnpricetag);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.ringspricetag, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnpricetag.setAdapter(adapter1);

        builder1 = new AlertDialog.Builder(createring.this);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert11 = builder1.create();

        btnCreatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tietname.getText().toString().equals("")){
                    AlertDialog.Builder builderlast = new AlertDialog.Builder(createring.this);
                    builderlast.setCancelable(true);

                    builderlast.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builderlast.setTitle("Empty field");
                    builderlast.setMessage("Please enter a Ring name.");
                    AlertDialog alert1last = builderlast.create();

                    alert1last.show();
                }
                else{
                    btnCreatering.setEnabled(false);
                    btnCreatering.setText("");
                    pgbloadcring.setVisibility(View.VISIBLE);
                    createaring();
                }
            }
        });
    }

    private void createaring(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        try {
            //input your API parameters
            object.put("ring_name", tietname.getText().toString() );
            object.put("ring_price_tag", spinnpricetag.getSelectedItem().toString());
            object.put("ring_currency", spinncurrency.getSelectedItem().toString());
            object.put("email", globalenviron.myemail);
            object.put("rule1", String.valueOf(rule1.isChecked()));
            object.put("rule2", String.valueOf(rule2.isChecked()));
            object.put("rule3", String.valueOf(rule3.isChecked()));
            object.put("rule4", String.valueOf(rule4.isChecked()));
            object1.put("message", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        final String url = "https://us-central1-rings-c715d.cloudfunctions.net/create_ring";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response====="+response);
                        try {
                            int code;
                            code = response.getInt("code");

                            if (code == 200){

                                if (imageUri != null){
                                    // Get a non-default Storage bucket
                                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://ringdp");
                                    // Create a storage reference from our app
                                    StorageReference storageRef = storage.getReference();
                                    // Create a reference to 'ringsprofilepicture/mountains.jpg'
                                    StorageReference ringdp = storageRef.child("rings_profile_picture/"+tietname.getText().toString());

                                    System.out.println("===========path====="+imageUri);
                                    System.out.println("===========image path====="+imageUri.getPath());
                                    UploadTask uploadTask = ringdp.putFile(imageUri);

                                    // Register observers to listen for when the download is done or if it fails
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            System.out.println("==========erroruploading===="+exception.getMessage());
                                            Toast.makeText(createring.this, "Error uploading the Ring profile picture", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                            System.out.println("==========uploaded pic====");
                                        }
                                    });

                                }

                                AlertDialog.Builder builderlast = new AlertDialog.Builder(createring.this);
                                builderlast.setCancelable(true);

                                builderlast.setPositiveButton(
                                        "VIEW",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(getApplicationContext(), menuscreen.class);
                                                startActivity(i);
                                                finish();
                                                dialog.cancel();
                                            }
                                        });

                                builderlast.setNegativeButton(
                                        "LATER",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                btnCreatering.setEnabled(true);
                                                btnCreatering.setText("Create");
                                                pgbloadcring.setVisibility(View.GONE);
                                                dialog.cancel();
                                            }
                                        });

                                builderlast.setTitle("Ring created successfully");
                                builderlast.setMessage("People can now join your ring.");
                                AlertDialog alert1last = builderlast.create();

                                alert1last.show();

                            }
                            else{
                                btnCreatering.setEnabled(true);
                                btnCreatering.setText("Create");
                                pgbloadcring.setVisibility(View.GONE);

                                if (code ==101){
                                    AlertDialog.Builder builderlast = new AlertDialog.Builder(createring.this);
                                    builderlast.setCancelable(true);

                                    builderlast.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    btnCreatering.setEnabled(true);
                                                    btnCreatering.setText("Create");
                                                    pgbloadcring.setVisibility(View.GONE);
                                                    dialog.cancel();
                                                }
                                            });

                                    builderlast.setTitle("Empty field");
                                    builderlast.setMessage("Please enter a Ring name.");
                                    AlertDialog alert1last = builderlast.create();

                                    alert1last.show();
                                }
                                else if(code ==444){

                                    builder1.setTitle("Error");
                                    builder1.setMessage("There is a problem with your account contact us.");
                                    alert11.show();
                                }
                                else if(code ==109){
                                    AlertDialog.Builder builderlast = new AlertDialog.Builder(createring.this);
                                    builderlast.setCancelable(true);

                                    builderlast.setPositiveButton(
                                            "DEPOSIT",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent i = new Intent(getApplicationContext(), depositmoney.class);
                                                    startActivity(i);
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });

                                    builderlast.setNegativeButton(
                                            "NOT NOW",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    btnCreatering.setEnabled(true);
                                                    btnCreatering.setText("Create");
                                                    pgbloadcring.setVisibility(View.GONE);
                                                    dialog.cancel();
                                                }
                                            });
                                }
                                else if(code ==111){
                                    AlertDialog.Builder builderlast = new AlertDialog.Builder(createring.this);
                                    builderlast.setCancelable(true);

                                    builderlast.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    btnCreatering.setEnabled(true);
                                                    btnCreatering.setText("Create");
                                                    pgbloadcring.setVisibility(View.GONE);
                                                    dialog.cancel();
                                                }
                                            });

                                    builderlast.setTitle("Ring name taken");
                                    builderlast.setMessage("The Ring name is already taken.Try a different name.");
                                    AlertDialog alert1last = builderlast.create();

                                    alert1last.show();
                                }
                                else if(code ==888){
                                    AlertDialog.Builder builderlast = new AlertDialog.Builder(createring.this);
                                    builderlast.setCancelable(true);

                                    builderlast.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    btnCreatering.setEnabled(true);
                                                    btnCreatering.setText("Create");
                                                    pgbloadcring.setVisibility(View.GONE);
                                                    dialog.cancel();
                                                }
                                            });

                                    builderlast.setTitle("Maximum limit");
                                    builderlast.setMessage("You cannot create more than 3 Rings");
                                    AlertDialog alert1last = builderlast.create();

                                    alert1last.show();
                                }
                                else{
                                    btnCreatering.setEnabled(true);
                                    btnCreatering.setText("Create");
                                    pgbloadcring.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            System.out.println("=======jsonexception====="+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnCreatering.setEnabled(true);
                btnCreatering.setText("Create");
                pgbloadcring.setVisibility(View.GONE);
                System.out.println("========volleyerror==="+error.getMessage());
                Toast.makeText(getApplicationContext(),"Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setdpforring(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100){
            imageUri = data.getData();
            imgdpring.setImageURI(imageUri);
            imgdpring.setScaleType(ImageView.ScaleType.FIT_XY);
            scaleImage(imgdpring);
        }
    }

    private void scaleImage(ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            //bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}