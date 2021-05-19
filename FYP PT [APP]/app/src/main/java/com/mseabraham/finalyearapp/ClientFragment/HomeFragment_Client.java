package com.mseabraham.finalyearapp.ClientFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mseabraham.finalyearapp.MainActivity_Client;
import com.mseabraham.finalyearapp.MessageActivity;
import com.mseabraham.finalyearapp.R;

public class HomeFragment_Client extends Fragment {

    private TextView txtTrainerID, txtClient, txtClientID;
    private MainActivity_Client parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__client, container, false);;

        Button msgTrainer = (Button) view.findViewById(R.id.btnMsg);
        Button sessionLogs = (Button) view.findViewById(R.id.btnLog);
        txtClient = (TextView) view.findViewById(R.id.txtDspName);
        txtClientID = (TextView) view.findViewById(R.id.txtUID);
        txtTrainerID = (TextView) view.findViewById(R.id.txtTID);
        TextView txtTrainerName = (TextView) view.findViewById(R.id.txtTName);

        parent = ((MainActivity_Client) getActivity());

        msgTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("trainerID", parent.trainerID);
                intent.putExtra("claim", parent.claim);
                intent.putExtra("user", "Trainer");
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtClientID.setText(parent.firebaseUser.getUid());
        txtClient.setText(parent.firebaseUser.getDisplayName());
        txtTrainerID.setText(parent.trainerID);
    }
}
