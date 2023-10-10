package com.example.myapplication;

public class Single_ton_data {
    private static Single_ton_data instance;
    private String sharedVariable;
    private String user_id;
    private String user_pass;
    private String user_group;

    private Single_ton_data() {
        user_id="";
        user_pass="";
        user_group="";
    }

    public static Single_ton_data getInstance() {
        if (instance == null) {
            instance = new Single_ton_data();
        }
        return instance;
    }
    public String getSharedVariable() {
        return sharedVariable;
    }
    public void setSharedVariable(String value) {
        sharedVariable = value;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String u_id){
        user_id=u_id;
    }
    public String getUser_pass() {
        return user_pass;
    }
    public void setUser_pass(String u_pass){
        user_pass=u_pass;
    }

    public String getUser_group() {
        return user_group;
    }
    public void setUser_group(String u_group){
        user_group=u_group;
    }
}