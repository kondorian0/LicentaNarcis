package com.narcis.neamtiu.licentanarcis.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User {

    public String name, email;

    public User(){

    }

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }
}
