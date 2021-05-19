package com.mseabraham.finalyearapp.TrainerFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.mseabraham.finalyearapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


public class NewClientFragment extends Fragment {
    private FirebaseFunctions mFunctions;
    private String uid;
    private String email, password, displayName;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFunctions = FirebaseFunctions.getInstance();
        uid = user.getUid();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_client, container, false);

        //TEXTVIEW SETUP
        final TextView txtEmail = (TextView) view.findViewById(R.id.email);
        final TextView txtDisp = (TextView) view.findViewById(R.id.dspName);
        final TextView txtPswd = (TextView) view.findViewById(R.id.password);

        //BUTTON SETUP
        Button addButton  = (Button) view.findViewById(R.id.btnNewClient);
        addButton.setOnClickListener(new View.OnClickListener() { //VALIDATE FIELD ENTRIES
            @Override
            public void onClick(View v) {

                //GET EMAIL, PASSWORD & DISPLAY NAME FROM USER INOUT
                email = txtEmail.getText().toString();
                password = txtPswd.getText().toString();
                displayName = txtDisp.getText().toString();

                //CHECK IF FIELDS HAVE BEEN FILLED IN AND THROW ERROR IF NOT
                if (displayName.isEmpty()){
                    txtDisp.setError("Enter a display name between 1-10 characters.");
                    txtDisp.requestFocus();
                }
                else if (email.isEmpty()){
                    txtEmail.setError("Please enter email.");
                    txtEmail.requestFocus();
                }
                else if (password.isEmpty() || password.length() < 6){
                    txtPswd.setError("Please enter a password, 6 characters or more.");
                    txtPswd.requestFocus();
                }
                else if (email.isEmpty() && password.isEmpty() && displayName.isEmpty()){
                    Toast.makeText(getContext(), "All fields are empty!",Toast.LENGTH_SHORT).show();
                }
                //IF ALL FIELDS ENTERED THEN CREATE AN ACCOUNT IN THE FIREBASE DATABASE, IF UNSUCCESSFUL THEN THROW AN ERROR
                else if (!(email.isEmpty() && password.isEmpty()&& displayName.isEmpty())) {
                    callCloudFunction();
                }
            }
        });


        return view;
    }


    public Task<String> callCloudFunction() {
            FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("email", email);
            data.put("password", password);
            data.put("displayName", displayName);
            data.put("trainerId", uid);

            return mFunctions
                    .getHttpsCallable("mkUser")
                    .call(data)
                    .continueWith(new Continuation<HttpsCallableResult, String>() {
                        @Override
                        public String then(@NonNull Task<HttpsCallableResult> task) {
                            return (String) task.getResult().getData();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(getView().getContext(), "Server Error Completing Request",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (task.getResult().equals("OK")) {
                                    Snackbar.make(view, "New Client Successfully Created!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                   getFragmentManager().popBackStackImmediate();

                                } else {
                                    Toast.makeText(getView().getContext(), "Sign Up Unsuccessful!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
    }
}
