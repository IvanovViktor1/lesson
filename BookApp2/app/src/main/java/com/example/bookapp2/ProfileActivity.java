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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText Name, Phone, Firstname, Lastname;
    Button update, cPass;
    ProgressDialog progressDialog;
    UserManagment userManagment;
    TextView textFname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textFname = findViewById(R.id.txtFname);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        userManagment = new UserManagment(this);
        userManagment.checkLogin();

        Name = findViewById(R.id.p_name);
        Phone = findViewById(R.id.p_phone);
        Firstname = findViewById(R.id.p_firstname);
        Lastname = findViewById(R.id.p_lastname);
        update = findViewById(R.id.btn_update);
        cPass = findViewById(R.id.btn_changePassword);


        HashMap<String, String> user = userManagment.userDetails();
        String mName = user.get(userManagment.NAME);
        final String mPhone = user.get(userManagment.PHONE);
        String mFirstname = user.get(userManagment.FIRSTNAME);
        String mLastname = user.get(userManagment.LASTNAME);


//        Intent i = getIntent();
//        String mName = i.getStringExtra("name");
//        final String mPhone = i.getStringExtra("phone");
        textFname.setText(mFirstname);
        Name.setText(mName);
        Phone.setText(mPhone);
        Firstname.setText(mFirstname);
        Lastname.setText(mLastname);


        cPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View resetpasswordlayout = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.change_password, null);
                EditText Oldpass = resetpasswordlayout.findViewById(R.id.edtName);
                EditText NewPass = resetpasswordlayout.findViewById(R.id.edtPhone);
                EditText ConformPass = resetpasswordlayout.findViewById(R.id.edtNewPass2);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("CHANGE PASSWORD");
                builder.setView(resetpasswordlayout);
                builder.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldpassword = Oldpass.getText().toString().trim();
                        String newpassword = NewPass.getText().toString().trim();
                        String conformpassword = ConformPass.getText().toString().trim();

                        if(oldpassword.isEmpty() || newpassword.isEmpty() || conformpassword.isEmpty()){
                            message("some filed are empty");
                        } else {
                            progressDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.RESET_PASSWORD_URL, new Response.Listener<String>() {
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
                                    Map<String, String> params = new HashMap<>();
                                    params.put("oldpassword", oldpassword);
                                    params.put("newpassword", newpassword);
                                    params.put("conformpassword", conformpassword);
                                    params.put("phone", mPhone);
                                    return params;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                            queue.add(stringRequest);
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = Name.getText().toString().trim();
                final String phone = Phone.getText().toString().trim();
                final String firstname = Firstname.getText().toString().trim();
                final String lastname = Lastname.getText().toString().trim();

                if(name.isEmpty()||phone.isEmpty()||firstname.isEmpty()||lastname.isEmpty()){
                    message("some fields are empty");
                } else {
                    progressDialog.setTitle("Updating...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_INFO_URL, new Response.Listener<String>() {
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
                            Map <String, String> updateParams = new HashMap<>();
                            updateParams.put("name", name);
                            updateParams.put("phone", phone);
                            updateParams.put("mPhone", mPhone);
                            updateParams.put("firstname", firstname);
                            updateParams.put("lastname", lastname);
                            return updateParams;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
                    queue.add(stringRequest);
                }
            }
        });
    }


    public void message (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        userManagment.logout();
    }

    public void goHome(View view) {
        Intent intent = new Intent(ProfileActivity.this, Binoculars.class);
        startActivity(intent);
    }

    public void goStudentHome(View view ) {
//        Intent intent = new Intent(ProfileActivity.this, MainSFragment.class);
//        startActivity(intent);
    }

}