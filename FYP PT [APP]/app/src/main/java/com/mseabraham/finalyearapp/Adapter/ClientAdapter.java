package com.mseabraham.finalyearapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mseabraham.finalyearapp.InboxActivity;
import com.mseabraham.finalyearapp.MainActivity;
import com.mseabraham.finalyearapp.MessageActivity;
import com.mseabraham.finalyearapp.Models.User;
import com.mseabraham.finalyearapp.TrainerFragment.ProfileFragment;
import com.mseabraham.finalyearapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    private Context context;
    private List<User> nUser;

    public ClientAdapter(Context context, List<User> nUser){
        this.context = context;
        this.nUser = nUser;
    }

    public void updateData(List<User> newData) {
        this.nUser = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new ViewHolder(view);
    }

    //ASSIGN USER DETAILS RETRIEVED FROM DATABASE TO DISPLAY FIELDS
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = nUser.get(position);

        holder.clientName.setText(user.getUsername());
        if (!(user.getImg().equals("default"))){
            Picasso.get().load(user.getImg()).into(holder.clientPic);
        }
        //CHECK WHICH ACTIVITY CLIENT LIST LAYOUT IS BEING USED IN AND COMPLETE DIFFERENT ONCLICK FOR EACH
        if (this.context instanceof InboxActivity){
            holder.clientLst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userid", user.getId());
                    context.startActivity(intent);
                }
            });

            holder.clientName.setTextColor(Color.parseColor("#2d2d2d"));
        }
        else if (this.context instanceof MainActivity){
            holder.clientLst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", user);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new ProfileFragment();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, myFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() { return nUser.size();}


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView clientName;
        public ImageView clientPic;
        RelativeLayout clientLst;

        public ViewHolder(View itemView){
            super(itemView);

            clientName = itemView.findViewById(R.id.txtClient);
            clientPic = itemView.findViewById(R.id.imgClient);
            clientLst = itemView.findViewById(R.id.lytClient);
        }
    }
}
