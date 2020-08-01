package com.example.rings.model;

import android.net.Uri;

public class myringmodel {
    String Ringname;
    String Status;
    String Pricetag;
    String Currency;
    android.net.Uri Uripath;

    public void setRingname(String value){
        Ringname = value;
    }

    public void setPricetag(String value){
        Pricetag = value;
    }

    public void setCurrency(String value){
        Currency = value;
    }

    public void setStatus(String value){
        Status = value;
    }

    public void setUripath(Uri value){
        Uripath = value;
    }

    public String getCurrency(){
        return Currency;
    }

    public String getPricetag(){
        return Pricetag;
    }

    public String getRingname(){
        return Ringname;
    }

    public String getStatus(){
        return Status;
    }

    public Uri getUripath(){
        return Uripath;
    }

}
