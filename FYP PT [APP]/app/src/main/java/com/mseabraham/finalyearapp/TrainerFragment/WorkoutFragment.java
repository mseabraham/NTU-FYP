package com.mseabraham.finalyearapp.TrainerFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.mseabraham.finalyearapp.R;
import com.mseabraham.finalyearapp.TrainerFragment.EditorFragment;

import java.util.ArrayList;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class WorkoutFragment extends Fragment {
    private ArrayList<String> listWorkout, workoutKey;
    ArrayAdapter<String> adapter;
    private ListView listView;

    private String etStr;
    private EditText et;

    private ListenerRegistration registration;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_workout,container,false);

        //BUTTON SETUP
        Button newWorkoutButton  = (Button) view.findViewById(R.id.btnAddWorkout);
        newWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SETUP THE LAYOUT FOR THE ALERT DIALOG BOX
                LinearLayout layout = new LinearLayout(getActivity());
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                //SETUP THE EDIT TEXT TO GET USER INPUT
                et = new EditText(getActivity());

                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 30;
                layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));



                final AlertDialog alertDialog = new AlertDialog.Builder (getActivity())
                        .setTitle("Name Workout")
                        .setPositiveButton("Create", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setView(layout)
                        .show();


                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etStr = et.getText().toString();
                        //VALIDATION OF EDIT TEXT
                        if (etStr.isEmpty()){
                            et.setError("Please name the workout.");
                            et.requestFocus();
                        }
                        else{
                            //MOVE TO A FLOW WHICH CREATES NEW WORKOUTS & SEND WORKOUT NAME
                            Bundle bundle = new Bundle();
                            Fragment myFragment = new EditorFragment();
                            bundle.putString("workout", etStr);
                            myFragment.setArguments(bundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_layout,myFragment).commit();
                            ft.addToBackStack("WorkoutFragment");
                            alertDialog.dismiss();
                        }
                    }
                });


            }
        });


        //LIST VIEW SETUP
        listView = view.findViewById(R.id.lstWorkout);
        listWorkout = new ArrayList<>();
        workoutKey = new ArrayList<>();

        //QUERY DATABASE FOR A TRAINERS WORKOUTS
        registration = db.collection("Workouts").whereEqualTo("trainerID", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }
                else {
                    listWorkout.clear();
                    workoutKey.clear();
                    for (DocumentSnapshot snp : queryDocumentSnapshots){
                        if (snp.getString("name") != null){
                            listWorkout.add(snp.getString("name"));
                            workoutKey.add(snp.getId());
                        }
                    }
                    adapter = new ArrayAdapter<>(getContext(), R.layout.item_list, R.id.txtWorkout,listWorkout);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                android.app.AlertDialog.Builder removeADB = new android.app.AlertDialog.Builder(getActivity());
                removeADB.setTitle("Are you sure you want to permanently delete this workout?");
                removeADB.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = workoutKey.get(position);
                        db.collection("Workouts").document(name).delete();
                        workoutKey.remove(position);
                        listWorkout.remove(position);
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
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registration.remove();
    }
}
