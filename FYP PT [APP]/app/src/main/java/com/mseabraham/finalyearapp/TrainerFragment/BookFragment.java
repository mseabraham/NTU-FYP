package com.mseabraham.finalyearapp.TrainerFragment;

import android.os.Bundle;


import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.mseabraham.finalyearapp.BaseFragment;
import com.mseabraham.finalyearapp.Models.Booking;
import com.mseabraham.finalyearapp.Models.LogDocument;
import com.mseabraham.finalyearapp.Models.User;
import com.mseabraham.finalyearapp.Models.Workout;
import com.mseabraham.finalyearapp.R;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;


public class BookFragment extends BaseFragment {
    private static final String TAG = "BookingFragment";

    private Spinner spinnerClient;
    private Spinner spinnerTime;
    private TextView txtDate;
    private Spinner spinnerExe;
    private CalendarView calendar;

    private ArrayList<String> client;
    private ArrayList<HashMap<String,String>> nUsers;
    private HashMap<String, String> userMap;
    private Integer[] timesSlots;

    private WriteBatch batch;
    private DocumentReference doc;
    private Booking toAdd;
    private HashMap<String,ArrayList<Booking>> toPost = new HashMap<>();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Clients");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View view;


    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {

        view = inflater.inflate(R.layout.fragment_book, parent, false);

        txtDate = (TextView) view.findViewById(R.id.prvwDate);
        spinnerExe = (Spinner) view.findViewById(R.id.spnExe);
        spinnerTime = (Spinner) view.findViewById(R.id.spnTime);
        spinnerClient = (Spinner) view.findViewById(R.id.spnClient);
        calendar = (CalendarView) view.findViewById(R.id.calendarView);
        Button btnBook = (Button) view.findViewById(R.id.btnBook);

        calendar.setMinDate(System. currentTimeMillis() - 1000);

        nUsers = new ArrayList<>();
        userMap = new HashMap<String, String>();
        client = new ArrayList<>();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookClient = (String) spinnerClient.getSelectedItem();
                String bookDate = txtDate.getText().toString();
                long bookTime = Long.parseLong(spinnerTime.getSelectedItem().toString());
                Workout bookWorkout = (Workout) spinnerExe.getSelectedItem();

                if (bookWorkout == null || bookClient == null){
                    Toast.makeText(getContext(), "Please First Go And Create A Workout And/Or Add A Client", Toast.LENGTH_LONG).show();
                }
                else {


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    batch = db.batch();
                    doc = db.collection("Sessions")
                            .document(user.getUid());

                    DocumentReference logDocRef = db.collection("Logs").document();

                    toAdd = new Booking(bookClient, bookDate, bookWorkout.getWorkoutId(), bookTime, logDocRef.getId(),false);
                    batch.update(doc, "bookings", FieldValue.arrayUnion(toAdd));
                    batch.commit().addOnFailureListener(new OnFailureListener() { //UPDATE SESSION DOCUMENT WITH NEW
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ArrayList<Booking> toStore = new ArrayList<>();
                            toStore.add(toAdd);
                            toPost.put("bookings", toStore);
                            BookFragment.this.db.collection("Sessions")
                                    .document(user.getUid())
                                    .set(toPost)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(view, "Your First Is Session Successfully Booked!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            getFragmentManager().popBackStackImmediate();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed Save Session.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(view, "Session Successfully Booked!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
                }
            }
        });


        fieldSetup();
        //SPINNER SETUPS
        SpinnerSetup(user.getUid(), spinnerExe);

        ArrayAdapter<Integer> time_adapter = new ArrayAdapter<Integer>(getContext(),R.layout.item_spinner, timesSlots);
        spinnerTime.setAdapter(time_adapter);
        return view;
    }

    private void fieldSetup(){
        assert getArguments() != null;
        if (!getArguments().getBoolean("quickBook")){
            Booking session =  getArguments().getParcelable("bookings");

            assert session != null;
            timesSlots = new Integer[]{(int) session.getTime()};
            txtDate.setText(session.getDate());
            spinnerTime.setEnabled(false);
            calendar.setEnabled(false);
            calendar.setVisibility(View.INVISIBLE);
            calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "The Date Cannot Be Changed From This View.",Toast.LENGTH_SHORT).show();
                }
            });

            //QUERY DATABASE FOR A TRAINERS WORKOUTS
            reference.addValueEventListener(postListener);
        }
        else{
            timesSlots = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
            //client = new ArrayList<String>(Collections.singleton(session.getClient()));
        }
    }

    private ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snp : dataSnapshot.getChildren()){
                User users = snp.getValue(User.class).withId(snp.getKey());

                if (!snp.getKey().equals(user.getUid())){
                    userMap.put("id", snp.getKey());
                    userMap.put("name",users.getUsername());
                    client.add(users.getUsername());
                    nUsers.add(userMap);
                }
            }
            ArrayAdapter<String> client_adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, client);
            spinnerClient.setAdapter(client_adapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reference.removeEventListener(postListener);
    }
}
