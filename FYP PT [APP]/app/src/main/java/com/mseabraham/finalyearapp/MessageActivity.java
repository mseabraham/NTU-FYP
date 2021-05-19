package com.mseabraham.finalyearapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mseabraham.finalyearapp.Adapter.MessageAdapter;
import com.mseabraham.finalyearapp.Models.Chat;
import com.mseabraham.finalyearapp.Models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends BaseActivity {
    TextView username;
    CircleImageView userImg;
    EditText txtMsg;
    RecyclerView recyclerView;

    FirebaseUser user;
    DatabaseReference reference;

    Intent intent;
    String userid;

    MessageAdapter msgAdpt;
    List<Chat> nchat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setToolbar(R.id.toolbar);


        username = findViewById(R.id.username);
        userImg = findViewById(R.id.userImg);
        txtMsg = findViewById(R.id.msgText);

        //<-------RECYCLERVIEW SETUP----------->
        recyclerView = findViewById(R.id.rycMsg);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        intent = getIntent();

        user = FirebaseAuth.getInstance().getCurrentUser();
        checkUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                username.setText(value.getUsername());
                if(value.getImg() != null){
                    if (!(value.getImg().equals("default"))){
                        Picasso.get().load(value.getImg()).into(userImg);
                    }
                }
                readMsg(user.getUid(), userid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //VALIDATE THAT INPUT FIELDS HAVE BEEN ENTERED AND SEND MSG TO DATABASE ON BUTTON CLICK
    public void sndMsg(View view) {
        String msg = txtMsg.getText().toString();
        if(!msg.isEmpty()){
            postMsg(user.getUid(), userid, msg);
        }
        else{
            Toast.makeText(MessageActivity.this, "Message requires texts to send.",Toast.LENGTH_SHORT).show();
        }
        txtMsg.setText("");
    }

    //SEND MESSAGE TO THE DATABASE
    private void postMsg (String sender, String receiver, String msg ){
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg",msg);

        reference.child("Chats").push().setValue(hashMap);
    }

    //LISTEN TO THE DATABASE FOR NEW MESSAGES SENT TO UPDATE THE RECYCLER VIEW
    public void readMsg(final String sender, final String receiver){
        nchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nchat.clear();

                for (DataSnapshot snp : dataSnapshot.getChildren()){
                    Chat chat = snp.getValue(Chat.class);
                    if(chat.getReceiver().equals(sender) && chat.getSender().equals(receiver) ||
                            chat.getReceiver().equals(receiver) && chat.getSender().equals(sender) ){
                        nchat.add(chat);
                    }
                }
                msgAdpt = new MessageAdapter(MessageActivity.this, nchat);
                recyclerView.setAdapter(msgAdpt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void checkUser (){
        String isClient = intent.getExtras().getString("claim");
        if (isClient == null){
            intent = getIntent();
            userid = intent.getStringExtra("userid");
            reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Clients").child(userid);
        } else if (isClient.equals("client")){
            userid = intent.getExtras().getString("trainerID");
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        }
        else{
            Log.d("claimMsg", "Trouble checking account claims");
        }

    }


}
