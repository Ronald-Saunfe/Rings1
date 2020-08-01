package com.example.rings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Settings;
import com.example.rings.EnvironVar.globalenviron;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.androidstudy.daraja.util.TransactionType.CustomerPayBillOnline;

public class depositmoney extends AppCompatActivity {

    Spinner spinnerdep;
    Button btndepositmoney;
    ProgressBar pgbdepositmoney;
    TextInputEditText mobilenotie;
    TextInputLayout mobilenoextinpl;
    String amount="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositmoney);


         spinnerdep = (Spinner) findViewById(R.id.spinnerdep);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.paymentoptions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerdep.setAdapter(adapter);

        pgbdepositmoney = findViewById(R.id.pgbdepositmoney);
        btndepositmoney = findViewById(R.id.btndepositmoney);


        //
        btndepositmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerdep.getSelectedItem().toString().equals("M-pesa")){
                    btndepositmoney.setEnabled(false);
                    btndepositmoney.setText("");
                    pgbdepositmoney.setVisibility(View.VISIBLE);

                    LayoutInflater inflater = getLayoutInflater();
                    View deleteDialogView = inflater.inflate(R.layout.dialogmobileno, null);
                    mobilenotie = deleteDialogView.findViewById(R.id.mobilenotie);
                    mobilenoextinpl = deleteDialogView.findViewById(R.id.mobilenoextinpl);
                    final AlertDialog.Builder deleteDialog = new  AlertDialog.Builder(depositmoney.this);
                    deleteDialog.setView(deleteDialogView);
                    final AlertDialog dialog = deleteDialog.create();
                    deleteDialogView.findViewById(R.id.btnsubmitdialog1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mobilenotie.getText().equals("")){
                                mobilenoextinpl.setError("Enter mobile number");
                            }
                            else{
                                mobilenoextinpl.setError(null);
                                depositmoney();
                                dialog.dismiss();
                            }
                        }
                    });
                    deleteDialogView.findViewById(R.id.btncanceldialog1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            btndepositmoney.setEnabled(true);
                            btndepositmoney.setText("Deposit");
                            pgbdepositmoney.setVisibility(View.GONE);
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
    private void depositmoney(){
        String passkeybase= "174379bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"+Settings.generateTimestamp();

        String encodeValue = Base64.encodeToString(passkeybase.getBytes(), Base64.NO_WRAP);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("BusinessShortCode", "174379" );
            object.put("Password",encodeValue);
            object.put("AccountReference", "ssad" );
            object.put("TransactionDesc", "sds");
            object.put("Timestamp",  Settings.generateTimestamp());
            object.put("TransactionType", "CustomerPayBillOnline");
            object.put("Amount", "1" );
            object.put("PartyA","254740209963");
            object.put("PartyB", "174379" );
            object.put("PhoneNumber","254740209963");
            object.put("CallBackURL", "https://us-central1-rings-c715d.cloudfunctions.net/mpesacallbackhandler" );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("=====response mpesa====="+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("========mpesavolleyerror==="+error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("authorization", "Bearer "+globalenviron.accesstoken);
                map.put("content-type", "application/json");
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void deprc(){
        String passkeybase= "174379bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"+Settings.generateTimestamp();

        String encodeValue = Base64.encodeToString(passkeybase.getBytes(), Base64.NO_WRAP);
        System.out.println("======timestamp===="+Settings.generateTimestamp());
        System.out.println("======base64===="+encodeValue);

        LNMExpress lnmExpress = new LNMExpress(
                "174379",
                encodeValue,
                CustomerPayBillOnline,
                "1",
                "254740209963",
                "174379",
                "254740209963",
                "https://us-central1-rings-c715d.cloudfunctions.net/mpesacallbackhandler",
                "dskl",
                "Rings deposit"
        );
        globalenviron.daraja.requestMPESAExpress(lnmExpress,
                new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        System.out.println("=======mpesa==results1========"+lnmResult);
                        System.out.println("=======mpesa==results2========"+lnmResult.ResponseDescription);
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("=======mpesa==error========"+error);
                    }
                }
        );
    }
}