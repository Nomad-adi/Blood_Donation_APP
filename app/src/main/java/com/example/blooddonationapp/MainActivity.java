package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blooddonationapp.adapter.UserAdapter;
import com.example.blooddonationapp.model.User;
import com.google.android.material.navigation.NavigationView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private CircleImageView nav_profile_image;
    private TextView nav_user_name,nav_email,nav_blood_groups,nav_type;

    private DatabaseReference userRef;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<User> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Blood Donation App");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this,userList);
        recyclerView.setAdapter(userAdapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = Objects.requireNonNull(snapshot.child("type").getValue()).toString();
                if(type.equals("donor")){
                    readRecipients();
                }else{
                    readDonor();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.nav_user_img);
        nav_blood_groups = navigationView.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_user_name = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_email = navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_type = navigationView.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    nav_user_name.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);

                    String bloodGroups = snapshot.child("bloodGroups").getValue().toString();
                    nav_blood_groups.setText(bloodGroups);

                    if(snapshot.hasChild("profilePictureUrl")){
                        String img_url = snapshot.child("profilePictureUrl").getValue().toString();
                        Glide.with(getApplicationContext()).load(img_url).into(nav_profile_image);
                    }
                    else{
                        nav_profile_image.setImageResource(R.drawable.user);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readDonor() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = ref.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Donor found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipients() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = ref.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Recipients found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.home:
                Intent intenthome = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intenthome);
                break;

            case R.id.aplus:
                Intent intent3 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent3.putExtra("group","A+");
                startActivity(intent3);
                break;
            case R.id.anegative:
                Intent intent4 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent4.putExtra("group","A-");
                startActivity(intent4);
                break;
            case R.id.b_positive:
                Intent intent5 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent5.putExtra("group","B+");
                startActivity(intent5);
                break;
            case R.id.b_negative:
                Intent intent6 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent6.putExtra("group","B-");
                startActivity(intent6);
                break;
            case R.id.ab_postive:
                Intent intent7 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent7.putExtra("group","AB+");
                startActivity(intent7);
                break;

            case R.id.ab_negative:
                Intent intent8 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent8.putExtra("group","AB-");
                startActivity(intent8);
                break;

            case R.id.o_postive:
                Intent intent9 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent9.putExtra("group","O+");
                startActivity(intent9);
                break;
            case R.id.o_negative:
                Intent intent10 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent10.putExtra("group","O-");
                startActivity(intent10);
                break;

            case R.id.compatible:
                Intent intent11 = new Intent(MainActivity.this,CategorySelectedActivity.class);
                intent11.putExtra("group","Compatible with me");
                startActivity(intent11);
                break;


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent_logout = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent_logout);
                break;

            case R.id.user_profile:
                Intent intent_profile = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent_profile);
                break;

            case R.id.about_menu:
                Intent intent15 = new Intent(MainActivity.this, AboutusActivity.class);
                startActivity(intent15);
                break;

            default:
                return true;
        }
        return true;
    }
}