package com.mseabraham.finalyearapp.TrainerFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mseabraham.finalyearapp.Adapter.BookingAdapter;
import com.mseabraham.finalyearapp.Adapter.SessionAdapter;
import com.mseabraham.finalyearapp.MainActivity;
import com.mseabraham.finalyearapp.Models.Booking;
import com.mseabraham.finalyearapp.Models.SessionData;
import com.mseabraham.finalyearapp.Models.User;
import com.mseabraham.finalyearapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BookingFragment extends Fragment {
    private SessionAdapter sessionAdapter;
    private BookingAdapter bookingAdapter;
    private ArrayList<Booking> book = new ArrayList<>();

    private SimpleDateFormat sDF;
    private Date date;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private User client;
    private MainActivity main;
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bookingView);
        main = ((MainActivity)getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        book.clear();
        if (!getArguments().getBoolean("session")){
            //GET DATE FOR SESSIONS
            sDF = new SimpleDateFormat("dd/MM/YYYY");
            date = (Date) getArguments().getSerializable("date");


            //CREATE 24 HOUR SESSION CARDS AS FREE
            for(int i = 0; i < 24; i++) {
                book.add(new Booking(null, sDF.format(date), null, i , null, false));
            }
            sessionAdapter = new SessionAdapter(requireContext(), book);
            recyclerView.setAdapter(sessionAdapter);
        }
        else{
            client = getArguments().getParcelable("client");
            bookingAdapter = new BookingAdapter(requireContext(), book);
            recyclerView.setAdapter(bookingAdapter);
            main.logged.clear();
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerSetUp();
    }

    private void recyclerSetUp(){

        firestore.collection("Sessions")
                .document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        SessionData sessionData = document.toObject(SessionData.class);
                        if (!getArguments().getBoolean("session")) { //CHECK IF THE SESSION HAS COME FROM PROFILE (true) OR SCHEDULE (false)
                            for (Booking booking : sessionData.bookings) {
                                if (booking.getDate().equals(sDF.format(date))) { //UPDATE TIMES IN LIST WITH DATABASE SAVES
                                    long time = booking.getTime();
                                    book.remove((int) time);
                                    book.add((int) time, booking);
                                }
                            }
                            sessionAdapter.notifyDataSetChanged();
                        }
                        else{
                            for (Booking booking : sessionData.bookings) {
                                if (booking.getClient().equals(client.getUsername())){ //UPDATE LIST WITH CLIENTS SESSION
                                    book.add(booking);
                                }
                            }
                            bookingAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }
}