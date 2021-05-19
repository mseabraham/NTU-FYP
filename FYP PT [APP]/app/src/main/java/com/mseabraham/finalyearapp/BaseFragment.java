package com.mseabraham.finalyearapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.mseabraham.finalyearapp.Models.Workout;

import java.util.ArrayList;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public abstract class BaseFragment extends Fragment {
    private ListenerRegistration registration;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState)
    {
        return provideYourFragmentView(inflater, parent, savedInstanseState);
    }

    public abstract View provideYourFragmentView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanceState);

    protected void SpinnerSetup(String user, final Spinner spin){
        final ArrayList<Workout> workouts = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //QUERY DATABASE FOR A TRAINERS WORKOUTS
        registration = db.collection("Workouts").whereEqualTo("trainerID", user)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Error:" + e.getMessage());
                        } else {
                            for (DocumentSnapshot snp : queryDocumentSnapshots) {
                                workouts.add(snp.toObject(Workout.class));
                            }
                        }
                        ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(getContext(),
                                R.layout.item_spinner, workouts);
                        spin.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registration.remove();
    }

}