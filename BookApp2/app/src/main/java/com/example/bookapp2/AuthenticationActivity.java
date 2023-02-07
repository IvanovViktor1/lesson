package com.example.bookapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends AppCompatActivity {
    EditText Phone, Password;
    Button register, login;
    ProgressDialog progressDialog;
    UserManagment userManagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Phone = findViewById(R.id.log_phone);
        Password = findViewById(R.id.log_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        userManagment = new UserManagment(this);

        register = findViewById(R.id.btn_register);
        login = findViewById(R.id.btn_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistrationProccess();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLoginProccess();
            }
        });

    }

    private void UserLoginProccess() {
        final String phone = Phone.getText().toString().trim();
        final String password = Password.getText().toString().trim();

        if(phone.isEmpty()||password.isEmpty()){
            message("some fields are empty");
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Urls.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("status");

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if(result.equals("success")){
                            progressDialog.dismiss();

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String name = object.getString("name");
                                String phone = object.getString("phone");
                                String firstname = object.getString("firstname");
                                String lastname = object.getString("lastname");

                                userManagment.UserSessionManagement(name, phone, firstname, lastname);

                                Intent intent = new Intent(AuthenticationActivity.this, Binoculars.class);
                                //// Intent intent = new Intent(AuthenticationActivity.this, ProfileActivity.class);
//                                intent.putExtra("name", name);
//                                intent.putExtra("phone", phone);
                                startActivity(intent);
                                finish();
                                message("User login successfully");
                            }

                        }else{
                            progressDialog.dismiss();
                            message("Login error");
                        }
                    } catch (JSONException e) {
                        message(response);
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    message(error.getMessage());
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map <String, String> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(AuthenticationActivity.this);
            queue.add(request);
        }
    }

    private void UserRegistrationProccess() {
        LayoutInflater inflater = getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        final EditText Name = register_layout.findViewById(R.id.reg_name);
        final EditText Phone = register_layout.findViewById(R.id.reg_phone);
        final EditText Password = register_layout.findViewById(R.id.reg_password);
        final EditText Firstname = register_layout.findViewById(R.id.reg_firstname);
        final EditText Lastname = register_layout.findViewById(R.id.reg_lastname);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(register_layout);
        builder.setTitle("Registration");
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressDialog.show();
                final String name = Name.getText().toString();
                final String phone = Phone.getText().toString();
                final String password = Password.getText().toString();
                final String firstname = Firstname.getText().toString();
                final String lastname = Lastname.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || password.isEmpty()||firstname.isEmpty()||lastname.isEmpty()){
                    message("some fields are empty..");
                    progressDialog.dismiss();
                }else{

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.REGISTRATION_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        message(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        message(error.getMessage());
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map <String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("phone", phone);
                        params.put("password", password);
                        params.put("firstname", firstname);
                        params.put("lastname", lastname);
                        return params;
                    }
                };
                    RequestQueue queue = Volley.newRequestQueue(AuthenticationActivity.this);
                    queue.add(stringRequest);
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void message(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}