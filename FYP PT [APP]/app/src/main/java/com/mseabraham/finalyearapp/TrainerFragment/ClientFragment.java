package com.mseabraham.finalyearapp.TrainerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mseabraham.finalyearapp.Adapter.ClientAdapter;
import com.mseabraham.finalyearapp.Models.User;
import com.mseabraham.finalyearapp.R;

import java.util.ArrayList;
import java.util.List;

public class ClientFragment extends Fragment {
    private RecyclerView recyclerView;
    private ClientAdapter adapter;
    private List<User> nUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.frag_clients,container,false);

        //RECYCLER VIEW SETUP
        recyclerView = view.findViewById(R.id.rycClient);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        nUser = new ArrayList<>();

        recyclerSetUp();

        //BUTTON SETUP
        Button addClientButton  = (Button) view.findViewById(R.id.btnAddClient);
        addClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newClient(view);
            }
        });

        return view;
    }

    private void recyclerSetUp() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Clients");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nUser.clear();
                for (DataSnapshot snp : dataSnapshot.getChildren()){
                    User user = snp.getValue(User.class).withId(snp.getKey());

                    if (!snp.getKey().equals(firebaseUser.getUid())){
                        nUser.add(user);
                    }
                }
                adapter = new ClientAdapter(getContext(), nUser);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    //MOVE TO CREATING A FLOW WHICH ADDS NEW CLIENTS TO THE ROSTER
    private void newClient(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myFragment = new NewClientFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, myFragment)
                .addToBackStack(null)
                .commit();
    }
}


