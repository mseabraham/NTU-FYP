package com.mseabraham.finalyearapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mseabraham.finalyearapp.Models.Chat;
import com.mseabraham.finalyearapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> nChat;

    FirebaseUser user;

    public MessageAdapter(Context context, List<Chat> nChat) {
        this.context = context;
        this.nChat = nChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //DISPLAY THE MESSAGES IN INGOING OR OUTGOING MESSAGE BUBBLES
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = nChat.get(position);

        holder.dspMsg.setText(chat.getMsg());
    }


    @Override
    public int getItemCount() {
        return nChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dspMsg;

        public ViewHolder(View itemView) {
            super(itemView);

            dspMsg = itemView.findViewById(R.id.viewMsg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //IDENTIFY SENDER OR RECEIVER TO MATCH THE LAYOUTS
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (nChat.get(position).getSender().equals(user.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

