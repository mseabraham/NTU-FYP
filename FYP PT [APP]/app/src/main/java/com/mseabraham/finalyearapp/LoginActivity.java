package com.mseabraham.finalyearapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    //Check if user is still logged in and if so, move to the main activity
    @Override
    public void onStart(){
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           AuthChecker();
        }
    }

    //Log in functionality
    public void logIn(View v) {
        EditText email = findViewById(R.id.etEmail);
        EditText password = findViewById(R.id.etPass);

        //Get email and password from text views
        String eml = email.getText().toString();
        String pwd = password.getText().toString();

        //Check if email and password fields have been filled in and throw error if not
        if (eml.isEmpty()){
            email.setError("Please enter email");
            email.requestFocus();
        }
        else if (pwd.isEmpty()){
            password.setError("Please enter password");
            password.requestFocus();
        }
        else if (eml.isEmpty() && pwd.isEmpty()){
            Toast.makeText(LoginActivity.this, "All fields are empty!",Toast.LENGTH_SHORT).show();
        }
        //If all fields entered then login to application, if unsuccessful then throw an error
        else if (!(eml.isEmpty() && pwd.isEmpty())){
            mAuth.signInWithEmailAndPassword(eml,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(LoginActivity.this,"This account doesn't exist",Toast.LENGTH_SHORT).show();
                            Log.d("SignUp", e.toString());
                        }catch(FirebaseAuthInvalidCredentialsException e){
                            Toast.makeText(LoginActivity.this,"The email address and password entered doesn't match, try again.",Toast.LENGTH_SHORT).show();
                            Log.d("SignUp", e.toString());
                        }
                        catch (Exception e) {
                            Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else{ //CHECK FOR ROLE IN CUSTOM CLAIM AND START DIFFERENT ACTIVITIES FOR TRAINERS AND CLIENT
                        currentUser = mAuth.getCurrentUser();
                        AuthChecker();
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this,"Something went wrong :(",Toast.LENGTH_SHORT).show();
        }
    }

    //Go to sign up page
    public void goSignUp(View v) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    public void AuthChecker(){
        currentUser.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String isClient = (String) getTokenResult.getClaims().get("role");
                if (isClient == null){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Log.d("claimLogin", "Successful Trainer Login");
                } else if (isClient.equals("client")){
                    String trainerID = (String) getTokenResult.getClaims().get("trainerId");
                    Intent intent = new Intent(LoginActivity.this, MainActivity_Client.class);
                    intent.putExtra("claim", isClient);
                    intent.putExtra("trainerID", trainerID);
                    startActivity(intent);
                    Log.d("claimLogin", "Successful Client Login");
                }else{
                    Toast.makeText(LoginActivity.this,"Something went wrong. Try again",Toast.LENGTH_SHORT).show();
                    Log.d("claimLogin", "UnSuccessful Login attempt");
                }

            }
        });
    }
}
