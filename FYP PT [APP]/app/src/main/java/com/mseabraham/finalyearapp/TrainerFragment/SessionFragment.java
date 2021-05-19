package com.mseabraham.finalyearapp.TrainerFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mseabraham.finalyearapp.MainActivity;
import com.mseabraham.finalyearapp.Models.Booking;
import com.mseabraham.finalyearapp.Models.Workout;
import com.mseabraham.finalyearapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SessionFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> exercises = new ArrayList<String>();
    private Booking book;
    private MainActivity main;
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        book = getArguments().getParcelable("bookings");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session, container, false);

        listView = (ListView) view.findViewById(R.id.lstExe);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.item_list, R.id.txtWorkout, exercises);
        listView.setAdapter(adapter);

        main = ((MainActivity)getActivity());



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TAKE THE USER TO THE DATA PAGE TO ADD SETS AND REPS TO THE WORKOUT
                DataFragment frag = new DataFragment();
                Bundle bundle = new Bundle();
                bundle.putString("logdocumentid", book.getLogs());
                bundle.putString("exercisename", exercises.get(position));
                bundle.putInt("place", position);
                frag.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout, frag).commit();
                ft.addToBackStack("WorkoutFragment");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder saveADB = new AlertDialog.Builder(getActivity());
                saveADB.setTitle("Are you sure you want to finish this session?");
                saveADB.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Sessions").document(main.firebaseUser.getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot != null){
                                    Booking booking = documentSnapshot.toObject(Booking.class);

                                }
                            }
                        });
                    }
                });
                saveADB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                saveADB.show();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listSetUp();

    }




    private void listSetUp(){
        FirebaseFirestore.getInstance().collection("Workouts")
                .document(book.getWorkout())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null){
                            Workout workout = documentSnapshot.toObject(Workout.class);
                            exercises.clear();
                            exercises.addAll(workout.getExercises());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
