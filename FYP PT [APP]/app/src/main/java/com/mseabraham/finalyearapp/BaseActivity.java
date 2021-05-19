package com.mseabraham.finalyearapp;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class BaseActivity extends AppCompatActivity {
    private MainActivity mainActivity;

    //SET DEFAULT TOOLBAR ACROSS DIFFERENT PAGES
    public  void setToolbar(@IdRes int toolbarID){
        try {
            Toolbar toolbar = findViewById(toolbarID);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { finish(); }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    //NAVIGATE BACKWARDS THROUGH THE APPLICATION
    public  void fragBack(@IdRes int toolbarID){
        try {
            Toolbar toolbar = findViewById(toolbarID);
            mainActivity.setSupportActionBar(toolbar);
            mainActivity.getSupportActionBar().setTitle("");
            mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.popBackStack(); }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void onBackPressed(View view)
    {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.popBackStack();
    }

}
