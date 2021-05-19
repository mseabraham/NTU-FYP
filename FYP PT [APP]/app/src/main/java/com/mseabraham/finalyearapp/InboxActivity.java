package com.mseabraham.finalyearapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mseabraham.finalyearapp.Adapter.ClientAdapter;
import com.mseabraham.finalyearapp.Models.Chat;
import com.mseabraham.finalyearapp.Models.User;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ClientAdapter adapter;

    private List<User> nUser;

    FirebaseUser user;
    DatabaseReference reference;

    private List<String> nClients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setToolbar(R.id.toolbar);

        recyclerView = findViewById(R.id.rycInbox);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();


        nClients = new ArrayList<>();

        //FILL LIST WITH ID'S OF CLIENTS THAT ARE CURRENTLY IN AN ACTIVE CHAT WITH TRAINER
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              nClients.clear();
              for (DataSnapshot dsnp: dataSnapshot.getChildren()){ //ADDS ID OF CLIENTS THAT HAVE BEEN MESSAGED INTO A LIST
                  Chat chat = dsnp.getValue(Chat.class);

                  if (chat.getReceiver().equals(user.getUid())){
                      nClients.add(chat.getSender());
                  }
                  if (chat.getSender().equals(user.getUid())){
                      nClients.add(chat.getReceiver());
                  }
              }
              showChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //FILL INBOX WITH THE NAMES OF CLIENTS CURRENTLY IN AN ONGOING CHAT
    private void showChats() {
        nUser = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Clients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> tmp = new ArrayList<>();
                for(DataSnapshot dsnp : dataSnapshot.getChildren()){
                    User user = dsnp.getValue(User.class).withId(dsnp.getKey());
                    for (String id : nClients){ //FOR EACH CLIENT MESSAGED, ADD USER DETAILS TO A LIST THAT POPULATES INBOX RECYCLER
                        if (dsnp.getKey().equals(id) && !tmp.contains(id)){
                            if(nClients.size() != 0) {
                                nUser.add(user);
                                tmp.add(dsnp.getKey());
                            }
                        }
                    }
                }
                adapter = new ClientAdapter(InboxActivity.this, nUser);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
