package com.narcis.neamtiu.licentanarcis.models;

import java.util.ArrayList;

public class User{

    public String name, email;
    public ArrayList events;

    public User(){

    }

    public User(String name, String email, ArrayList events){
        this.name = name;
        this.email = email;
        this.events = events;
    }
}
