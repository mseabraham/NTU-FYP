package com.mseabraham.finalyearapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText emailField, passwordField, nameField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        //get dynamic link and extract data
        checkDynamicLink();
    }

    private void checkDynamicLink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Log.i("SignUpActivity", "dynamic link successful");

                Uri deepLink = null;
                if (pendingDynamicLinkData != null){
                    deepLink = pendingDynamicLinkData.getLink();
                }

                if (deepLink != null){
                    Log.i("SignUpActivity","Here is deep link URL:\n"+ deepLink.toString());

                    String currentPage = deepLink.getQueryParameter("curPage");
                    int curPage = Integer.parseInt(currentPage);
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("SignUpActivity", "sad didnt work");
            }
        });
    }

    public void onClick(View view) {
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        nameField = findViewById(R.id.dspName);


        //GET EMAIL, PASSWORD & DISPLAY NAME FROM USER INOUT
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        final String displayName = nameField.getText().toString();

        //CHECK IF FIELDS HAVE BEEN FILLED IN AND THROW ERROR IF NOT
        if (displayName.isEmpty()){
            nameField.setError("Enter a display name between 1-10 characters.");
            nameField.requestFocus();
        }
        else if (email.isEmpty()){
            emailField.setError("Please enter email.");
            emailField.requestFocus();
        }
        else if (password.isEmpty() || password.length() < 6){
            passwordField.setError("Please enter a password, 6 characters or more.");
            passwordField.requestFocus();
        }
        else if (email.isEmpty() && password.isEmpty() && displayName.isEmpty()){
            Toast.makeText(SignUpActivity.this, "All fields are empty!",Toast.LENGTH_SHORT).show();
        }
        //IF ALL FIELDS ENTERED THEN CREATE AN ACCOUNT IN THE FIREBASE DATABASE, IF UNSUCCESSFUL THEN THROW AN ERROR
        else if (!(email.isEmpty() && password.isEmpty()&& displayName.isEmpty())){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Sign Up Unsuccessful? Check Terms & Conditions",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //CREATE ENTRIES INTO DATABASE THAT ASSIGNS ROLES TO NEWLY CREATED USER USERS
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String uid = user.getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = database.getReference("Users").child(uid);

                        //STORE USERNAME ALSO IN FIREBASE AUTH API
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build();
                        user.updateProfile(profileUpdates);

                        //ADD USER DETAILS TO FIREBASE REALTIME DATABASE FOR CHAT FEATURES & ROLE CHECK.
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id",uid);
                        hashMap.put("username",displayName);

                        mRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                    }
                }
            });
        }
        else{
            Toast.makeText(SignUpActivity.this,"Something went wrong, Try again!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
