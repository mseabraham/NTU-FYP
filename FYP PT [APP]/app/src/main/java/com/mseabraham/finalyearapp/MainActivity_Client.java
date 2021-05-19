package com.mseabraham.finalyearapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mseabraham.finalyearapp.ClientFragment.HomeFragment_Client;
import com.mseabraham.finalyearapp.ClientFragment.LogsFragment_Client;
import com.mseabraham.finalyearapp.ClientFragment.SessionFragment_Client;
import com.mseabraham.finalyearapp.TrainerFragment.ClientFragment;
import com.mseabraham.finalyearapp.TrainerFragment.ScheduleFragment;
import com.mseabraham.finalyearapp.TrainerFragment.WorkoutFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_Client extends AppCompatActivity {

    public String trainerID, claim;
    public FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__client);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(getNavListener);

        Intent intent = getIntent();
        trainerID = intent.getStringExtra("trainerID");
        claim = intent.getStringExtra("claim");

        //LOAD FRAGMENTS INTO FRAME LAYOUT
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment_Client()).commit();

        //<!-------CUSTOM TOOLBAR SETUP----------->
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        CircleImageView usrImg = findViewById(R.id.imgProfile);
        TextView usrName = findViewById(R.id.txtProfile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usrName.setText(firebaseUser.getDisplayName());
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
                case R.id.navigation_logs:
                    selectedFragment = new LogsFragment_Client();
                    break;
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment_Client();
                    break;
                case R.id.navigation_session:
                    selectedFragment = new SessionFragment_Client();
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
                startActivity(new Intent(MainActivity_Client.this,LoginActivity.class));
                finish();
                return true;
            case R.id.inbox:
                startActivity(new Intent(MainActivity_Client.this, InboxActivity.class));
                return true;
        }
        return false;
    }
}
