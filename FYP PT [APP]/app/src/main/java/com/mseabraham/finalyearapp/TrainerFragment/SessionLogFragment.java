package com.mseabraham.finalyearapp.TrainerFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mseabraham.finalyearapp.BaseFragment;
import com.mseabraham.finalyearapp.Models.Booking;
import com.mseabraham.finalyearapp.R;


public class SessionLogFragment extends BaseFragment {
    private Booking session;
    private View view;
    private Spinner spinner;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_session_log, parent, false);

        session = (Booking) getArguments().getParcelable("bookings");
        Button edit = (Button) view.findViewById(R.id.btnEdit);
        Button cancel = (Button) view.findViewById(R.id.btnCancel);

        //BUTTON SETUP
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.view_edit, viewGroup, false);
                builder.setView(dialogView);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                spinner = alertDialog.findViewById(R.id.spinExe);
                SpinnerSetup(user.getUid(), spinner);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //db.fi
            }
        });

        TextView client = view.findViewById(R.id.txtName);
        TextView date = view.findViewById(R.id.txtDate);
        TextView time = view.findViewById(R.id.txtTime);
        TextView exe = view.findViewById(R.id.txtExe);

        client.setText(session.getClient());
        date.setText(session.getDate());
        time.setText(String.format("%02d:00", session.getTime()));
        exe.setText(session.getWorkout());

        return view;
    }

}
