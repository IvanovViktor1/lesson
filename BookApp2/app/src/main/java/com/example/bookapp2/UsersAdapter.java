package com.example.bookapp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder>{
    Context context;
    List<Users> usersList;

    public UsersAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        return new UsersHolder(userLayout);
    }

    @Override
//    public void onBindViewHolder(@NonNull UsersHolder holder, int position) {
    public void onBindViewHolder(@NonNull UsersHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Users users = usersList.get(position);
        holder.Name.setText(users.getName());
        holder.Phone.setText(users.getPhone());
        holder.Firstname.setText(users.getFirstname());
        holder.Lastname.setText(users.getLastname());



        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View editLayout = LayoutInflater.from(context).inflate(R.layout.edit_user_details, null);
                final EditText Name = editLayout.findViewById(R.id.edtName);
                final EditText Phone = editLayout.findViewById(R.id.edtPhone);
                final EditText Firstname = editLayout.findViewById(R.id.edtFirstname);
                final EditText Lastname = editLayout.findViewById(R.id.edtLastname);

                Name.setText(users.getName());
                Phone.setText(users.getPhone());
                Firstname.setText(users.getFirstname());
                Lastname.setText(users.getLastname());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit "+ users.getFirstname() + " " + users.getLastname());
                builder.setView(editLayout);

                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = Name.getText().toString();
                        String phone = Phone.getText().toString();
                        String mPhone = users.getPhone();
                        String firstname = Firstname.getText().toString();
                        String lastname = Lastname.getText().toString();

                        if (name.isEmpty()||phone.isEmpty()||firstname.isEmpty()||lastname.isEmpty()){
                            Toast.makeText(context, "Some fields are empty", Toast.LENGTH_SHORT).show();
                        } else {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_USER_DETAIL_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("name", name);
                                    params.put("phone", phone);
                                    params.put("mPhone", mPhone);
                                    params.put("firstname", firstname);
                                    params.put("lastname", lastname);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });








        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE USER");
                builder.setMessage("Conform to DELETE" + users.getName());


                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_USER_URL,
                          new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    String result = object.getString("state");

                                    if (result.equals("delete")){
//                                        Delete(position);
                                        Delete(holder.getAbsoluteAdapterPosition());
                                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                          }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                          })  {
//                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> deleteParams = new HashMap<>();
                                deleteParams.put("phone", users.getPhone());
                                return deleteParams;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });



                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UsersHolder extends RecyclerView.ViewHolder {
        TextView Name, Phone, Firstname, Lastname;
        Button Edit;
        ImageButton Delete;
        public UsersHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.rcy_name);
            Phone = itemView.findViewById(R.id.rcy_phone);
            Firstname = itemView.findViewById(R.id.rcy_firstname);
            Lastname = itemView.findViewById(R.id.rcy_lastname);
            Edit = itemView.findViewById(R.id.rcy_edit);
            Delete = itemView.findViewById(R.id.rcy_delete);
        }
    }

    public void Delete(int item){
        usersList.remove(item);
        notifyItemRemoved(item);
    }

}
