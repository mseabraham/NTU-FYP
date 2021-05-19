package com.mseabraham.finalyearapp.TrainerFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mseabraham.finalyearapp.R;
import com.mseabraham.finalyearapp.TrainerFragment.EditorFragment;

import java.util.ArrayList;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;


public class ExerciseFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> lstExe;
    ListView listView;

    ArrayList<String> transactions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        listView = view.findViewById(R.id.lstExercise);

        transactions = new ArrayList<>();
        //BUNDLE CREATED TO SEND SELECTED EXERCISES BACK TO PREVIOUS FRAGMENT
        final Bundle sndBundle = new Bundle();

        //BUNDLE RETRIEVED FROM PREVIOUS FRAGMENT (PROVIDES SAVED EXERCISES & SPLIT TO POPULATE EXERCISE LIST)
        Bundle bundle = this.getArguments();
        final String bodySplit = bundle.getString("split");
        if (getArguments().getSerializable("key") != null){
            transactions = (ArrayList<String>)getArguments().getSerializable("key");
        }


        lstExe = new ArrayList<String>();
        //QUERY DATABASE FOR EXERCISES DEPENDING ON CHOSEN SPLIT AND FILL LIST WITH EXERCISES
        db.collection("Exercises").whereEqualTo("split",bodySplit).addSnapshotListener(new EventListener<QuerySnapshot>() {
          @Override
          public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
              if (e!=null){
                  Log.d(TAG,"Error:"+e.getMessage());
              }
              else {
                  lstExe.clear();

                  for (DocumentSnapshot snp : queryDocumentSnapshots){
                          lstExe.add(snp.getId());
                  }
                  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_list, R.id.txtWorkout, lstExe);
                  adapter.notifyDataSetChanged();
                  listView.setAdapter(adapter);

              }
          }
        });

        //ADD AND REMOVE EXERCISES FROM SELECTION
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exercise = lstExe.get(position);
                if (!transactions.contains(exercise)){
                        transactions.add(exercise);
                }
                else{
                    Snackbar.make(view, "This Exercise Is Already Included In Workout!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }

            }
        });

        //SEND SELECTED EXERCISES BACK TO THE WORKOUT LIST IN THE EDITOR FRAGMENT
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sndBundle.putSerializable("key", transactions);
                sndBundle.putString("workout", getArguments().getString("workout"));
                Fragment myFragment = new EditorFragment();
                myFragment.setArguments(sndBundle);
                getFragmentManager().beginTransaction().add(R.id.frame_layout,myFragment).commit();
            }
        });
        return view;
    }
}
