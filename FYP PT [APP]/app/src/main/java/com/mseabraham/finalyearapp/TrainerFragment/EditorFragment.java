package com.mseabraham.finalyearapp.TrainerFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mseabraham.finalyearapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditorFragment extends Fragment {
    private ArrayList<String> transactionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Bundle bundle;

    //FIREBASE INSTANCES
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static final String TAG = "EditorFragment";
    //TO SEND MAPPED VALUES
    private static final String KEY_NAME = "name";
    private static final String KEY_TRAINER = "trainerID";
    private static final String KEY_EXERCISES = "exercises";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_editor, container, false);

        bundle = new Bundle();
        bundle.putString("workout", getArguments().getString("workout"));

        //BUTTON SETUP
        final Button addUpButton  = (Button) view.findViewById(R.id.btnAddUpper);
        addUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newExe(addUpButton);
            }
        });

        final Button addLowButton  = (Button) view.findViewById(R.id.btnAddLower);
        addLowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newExe(addLowButton);
            }
        });

        //FLOATING ACTION BAR SETUP (SAVE WORKOUT)
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder saveADB = new AlertDialog.Builder(getActivity());
                saveADB.setTitle("Are you sure you want to save this workout?");
                saveADB.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> post = new HashMap<>();
                        post.put(KEY_NAME, getArguments().getString("workout"));
                        post.put(KEY_TRAINER, user.getUid());
                        post.put(KEY_EXERCISES, transactionList);

                        db.collection("Workouts").
                                document()
                                .set(post)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(view, "Workout Saved Successfully", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        getFragmentManager().popBackStackImmediate();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed To Post",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
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

        //LIST VIEW SETUP
        final ListView listView = (ListView) view.findViewById(R.id.lstWorkout);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder removeADB = new AlertDialog.Builder(getActivity());
                removeADB.setTitle("Are you sure you want to delete this exercise?");
                removeADB.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transactionList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                removeADB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                removeADB.show();
                return false;
            }
        });

        //CREATE AN ARRAY LIST TO AMEND WITH EXERCISES
        if (getArguments().getSerializable("key") == null) {
            return view;
        }
        else{
            transactionList = (ArrayList<String>)getArguments().getSerializable("key");
            bundle.putSerializable("key",transactionList);
            adapter = new ArrayAdapter<String>(getContext(), R.layout.item_list, R.id.txtWorkout, transactionList);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }

        return view;
    }


    //MOVE TO A FRAGMENT WHICH ADDS A NEW EXERCISE TO A WORKOUT
    private void newExe(View view){
        switch (view.getId()) {
            case (R.id.btnAddUpper):
                bundle.putString("split", "Upper");
                break;
            case (R.id.btnAddLower):
                bundle.putString("split", "Lower");
                break;
        }

        ExerciseFragment frag = new ExerciseFragment();
        frag.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.frame_layout, frag).commit();
    }
}

