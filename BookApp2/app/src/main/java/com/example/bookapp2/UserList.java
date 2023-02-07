package com.example.bookapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recyclerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();
        LoadAllUsers();
    }

    private void LoadAllUsers() {
        JsonArrayRequest request = new JsonArrayRequest(Urls.SHOW_ALL_DATA_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                for (int i=0; i< array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);
                        String name = object.getString("name").trim();
                        String phone = object.getString("phone").trim();
                        String firstname = object.getString("firstname").trim();
                        String lastname = object.getString("lastname").trim();

                        Users users = new Users();
                        users.setName(name);
                        users.setPhone(phone);
                        users.setFirstname(firstname);
                        users.setLastname(lastname);
                        usersList.add(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                usersAdapter = new UsersAdapter(UserList.this, usersList);
                recyclerView.setAdapter(usersAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserList.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(UserList.this);
        requestQueue.add(request);
    }
}