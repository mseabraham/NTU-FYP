package com.mseabraham.finalyearapp.TrainerFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mseabraham.finalyearapp.Adapter.DataAdapter;
import com.mseabraham.finalyearapp.MainActivity;
import com.mseabraham.finalyearapp.Models.Log;
import com.mseabraham.finalyearapp.Models.LogDocument;
import com.mseabraham.finalyearapp.Models.Set;
import com.mseabraham.finalyearapp.R;

import java.util.ArrayList;

public class DataFragment extends Fragment {
    private DataAdapter adapter;

    private ArrayList<Set> set = new ArrayList<Set>();

    private String logDocumentId;
    private String exerciseName;

    private MainActivity main;

    public interface OnFragmentInteractionListener {
        void onFragmentSetLogged(ArrayList<Integer> complete);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        logDocumentId = getArguments().getString("logdocumentid");
        exerciseName = getArguments().getString("exercisename");
        main = ((MainActivity) getActivity());

        //RECYCLER VIEW SETUP
        RecyclerView recyclerView = view.findViewById(R.id.rycData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DataAdapter(getContext(), set);
        recyclerView.setAdapter(adapter);

        Button addSet = (Button) view.findViewById(R.id.btnAddSet);
        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.add(new Set (0,0));
                adapter.notifyDataSetChanged();
            }
        });

        Button btnLog = (Button) view.findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Log log = new Log(set, exerciseName);
                final LogDocument document = new LogDocument();
                final DocumentReference logDocument = db.collection("Logs").document(logDocumentId);
                logDocument.update("data", FieldValue.arrayUnion(log)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        int position = getArguments().getInt("place");
                       // main.logged.add(position);
                        getFragmentManager().popBackStackImmediate();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        logDocument.set(document).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                getFragmentManager().popBackStackImmediate();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();     
                            }
                        });
                    }
                });
            }
        });

        return view;
    }
}
