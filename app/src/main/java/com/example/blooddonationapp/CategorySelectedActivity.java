package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.blooddonationapp.adapter.UserAdapter;
import com.example.blooddonationapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategorySelectedActivity extends AppCompatActivity {

    private Toolbar toolbar_cat;
    private RecyclerView recyclerView_cat;

    private List<User> userList;
    private UserAdapter userAdapter;

    private  String title = "";
    private LinearLayout empty_ly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selected);

        toolbar_cat = findViewById(R.id.toolbar_cat);
        setSupportActionBar(toolbar_cat);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView_cat = findViewById(R.id.recycleView_cat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView_cat.setLayoutManager(linearLayoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(CategorySelectedActivity.this,userList);
        recyclerView_cat.setAdapter(userAdapter);

        empty_ly = findViewById(R.id.empty_ly);


        if(getIntent().getExtras() !=null){
            title = getIntent().getStringExtra("group");
            getSupportActionBar().setTitle("Blood Group "+title);

            if(title.equals("Compatible with me")){
                getSupportActionBar().setTitle("Compatible with me");
                getCompatibleUsers();
            }else{
                readUsers();
            }


        }

    }

    private void getCompatibleUsers() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type = snapshot.child("type").getValue().toString();
                if(type.equals("donor")){
                    result = "recipient";
                }else{
                    result = "donor";
                }

                String bloodGroups =  snapshot.child("bloodGroups").getValue().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
                Query query = reference.orderByChild("search").equalTo(result+bloodGroups);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            User user = snapshot1.getValue(User.class);
                            userList.add(user);
                        }
                        if(userList.isEmpty()){
                            recyclerView_cat.setVisibility(View.GONE);
                            empty_ly.setVisibility(View.VISIBLE);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type = snapshot.child("type").getValue().toString();
                if(type.equals("donor")){
                    result = "recipient";
                }else{
                    result = "donor";
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
                Query query = reference.orderByChild("search").equalTo(result+title);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            User user = snapshot1.getValue(User.class);
                            userList.add(user);
                        }
                        if(userList.isEmpty()){
                            recyclerView_cat.setVisibility(View.GONE);
                            empty_ly.setVisibility(View.VISIBLE);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}