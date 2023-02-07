package com.example.bookapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserManagment {
    Context context;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static final String PREF_NAME = "User_login";
    public static final String LOGIN = "is_user_login";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";

    public UserManagment(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isUserLogin(){
        return  sharedPreferences.getBoolean(LOGIN, false);
    }

    public String fName(){
        return sharedPreferences.getString(FIRSTNAME, "firstname");
    }

    public void UserSessionManagement(String name, String phone, String firstname, String lastname){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(PHONE, phone);
        editor.putString(FIRSTNAME, firstname);
        editor.putString(LASTNAME, lastname);
        editor.apply();
    }

    public void checkLogin(){
        if(!this.isUserLogin()){
            Intent intent = new Intent(context, AuthenticationActivity.class);
            context.startActivity(intent);
            ((ProfileActivity) context).finish();
        }
    }

    public HashMap<String, String> userDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        user.put(FIRSTNAME, sharedPreferences.getString(FIRSTNAME, null));
        user.put(LASTNAME, sharedPreferences.getString(LASTNAME, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, AuthenticationActivity.class);
        context.startActivity(intent);
        ((ProfileActivity) context).finish();
    }


}
