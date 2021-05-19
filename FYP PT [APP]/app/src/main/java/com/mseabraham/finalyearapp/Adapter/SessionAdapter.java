package com.mseabraham.finalyearapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mseabraham.finalyearapp.TrainerFragment.BookFragment;
import com.mseabraham.finalyearapp.Models.Booking;
import com.mseabraham.finalyearapp.R;
import com.mseabraham.finalyearapp.TrainerFragment.SessionLogFragment;

import java.util.ArrayList;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Booking> bookings;

    private static final String BOOKING_KEY = "bookings";


    public SessionAdapter(Context context, ArrayList<Booking> bookings){
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public SessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionAdapter.ViewHolder holder, final int position) {
        //SETUP OF BOOKED SESSIONS
        Booking object = bookings.get(position);
        String strClient = object.getClient();

        //CHECK FOR AVAILABILITY AND CREATE CARD VIEW TIME SLOT
        if (strClient == null) {
            // Available
            holder.status.setText("FREE");
            holder.status.setTextColor(Color.GREEN);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookFragment frag = new BookFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BOOKING_KEY, bookings.get(position));
                    bundle.putBoolean("quickBook", false);
                    frag.setArguments(bundle);

                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.frame_layout, frag).commit();
                    ft.addToBackStack("SessionFragment");
                }
            });
        } else {
            //Booked
            holder.status.setText("BOOKED");
            holder.status.setTextColor(Color.RED);
            holder.clientName.setBackground(ContextCompat.getDrawable(context, R.drawable.bdr_inbox));

            //VIEW CLICK
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionLogFragment frag = new SessionLogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BOOKING_KEY, bookings.get(position));
                    frag.setArguments(bundle);
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.frame_layout, frag).commit();
                    ft.addToBackStack("SessionFragment");
                }
            });
        }
        holder.time.setText(String.format("%02d:00", object.getTime()));
        holder.clientName.setText(strClient);


    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView time, status, clientName;
        ViewHolder(View itemView){
            super(itemView);
            time = itemView.findViewById(R.id.txtTime);
            status = itemView.findViewById(R.id.txtStatus);
            clientName = itemView.findViewById(R.id.txtClientName);
        }
    }
}
