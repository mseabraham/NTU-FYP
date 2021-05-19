package com.mseabraham.finalyearapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mseabraham.finalyearapp.Models.User;
import com.mseabraham.finalyearapp.TrainerFragment.ClientFragment;
import com.mseabraham.finalyearapp.TrainerFragment.DataFragment;
import com.mseabraham.finalyearapp.TrainerFragment.ScheduleFragment;
import com.mseabraham.finalyearapp.TrainerFragment.WorkoutFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements DataFragment.OnFragmentInteractionListener{
    CircleImageView usrImg;
    TextView usrName;

    public FirebaseUser firebaseUser;
    DatabaseReference ref;

    public List<Integer> logged;

    private ArrayList<Integer> allLogged;

    @Override
    public void onFragmentSetLogged(ArrayList<Integer> complete) {
        allLogged = complete;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(getNavListener);
        logged = new ArrayList<>();

        //LOAD FRAGMENTS INTO FRAME LAYOUT
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ClientFragment()).commit();

        //<!-------CUSTOM TOOLBAR SETUP----------->
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        usrImg = findViewById(R.id.imgProfile);
        usrName = findViewById(R.id.txtProfile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        //CHANGE USERNAME PLATE IN TAB LAYOUT
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usrName.setText(user.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        //Check if user is signed
    }

    //ONCLICK LISTENER FOR CHANGING OF FRAGMENTS USING NAVIGATION BAR
    private BottomNavigationView.OnNavigationItemSelectedListener getNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_clients:
                    selectedFragment = new ClientFragment();
                    break;
                case R.id.navigation_workout:
                    selectedFragment = new WorkoutFragment();
                    break;
                case R.id.navigation_schedule:
                    selectedFragment = new ScheduleFragment();
                    break;
            }
            //LOAD DIFFERENT FRAGMENTS INTO THE FRAME LAYOUT
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
            return true;
        }
    };


    //MENU CONTROLS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                return true;
            case R.id.inbox:
                startActivity(new Intent(MainActivity.this, InboxActivity.class));
                return true;
        }
        return false;
    }



}
