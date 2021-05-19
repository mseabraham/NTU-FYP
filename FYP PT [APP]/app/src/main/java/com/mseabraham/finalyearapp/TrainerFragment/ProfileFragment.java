package com.mseabraham.finalyearapp.TrainerFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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

import com.mseabraham.finalyearapp.MessageActivity;
import com.mseabraham.finalyearapp.Models.User;
import com.mseabraham.finalyearapp.R;
import com.mseabraham.finalyearapp.TrainerFragment.BookFragment;
import com.mseabraham.finalyearapp.TrainerFragment.BookingFragment;

public class ProfileFragment extends Fragment {
    private User client;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_profile, container, false);

        TextView name = view.findViewById(R.id.txtClient);

        client =  getArguments().getParcelable("user");
        name.setText(client.getUsername());

        //BUTTON SETUP
        Button bookButton  = (Button) view.findViewById(R.id.btnBook);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBook(view);
            }
        });

        Button msgButton  = (Button) view.findViewById(R.id.btnMsg);
        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("userid", client.getId());
                intent.putExtra("user", client.getUsername());
                startActivity(intent);
            }
        });

        Button startButton = (Button) view.findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingFragment frag = new BookingFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("client", client);
                bundle.putBoolean("session", true);
                frag.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout, frag).commit();
                ft.addToBackStack("ProfileFragment");
            }
        });


        return view;
    }

    //MOVE TO FRAGMENT WHICH BOOKS SESSION FOR CLIENT TO THE DATABASE
    public void onBook(View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myFragment = new BookFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, myFragment)
                .addToBackStack(null)
                .commit();
    }

}
