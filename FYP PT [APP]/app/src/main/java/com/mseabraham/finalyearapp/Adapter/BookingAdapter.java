package com.mseabraham.finalyearapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mseabraham.finalyearapp.Models.Booking;
import com.mseabraham.finalyearapp.R;
import com.mseabraham.finalyearapp.TrainerFragment.SessionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Booking> bookings;
    private Booking object;

    private View view;

    private static final String BOOKING_KEY = "bookings";


    public BookingAdapter(Context context, ArrayList<Booking> bookings){
        this.context = context;
        this.bookings = bookings;
    }


    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
        return new BookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, final int position) {
        //SETUP OF BOOKED SESSIONS
        object = bookings.get(position);
        String strClient = object.getClient();

        //Booked
        holder.status.setText(object.getDate());
        holder.status.setTextColor(Color.BLACK);
        holder.clientName.setBackground(ContextCompat.getDrawable(context, R.drawable.bdr_inbox));

        //VIEW CLICK
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sDF = new SimpleDateFormat("dd/MM/YYYY");
                Booking sessionPick = bookings.get(position);

                if (sDF.format(currentTime).equals(sessionPick.getDate())){
                    SessionFragment frag = new SessionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BOOKING_KEY, bookings.get(position));
                    frag.setArguments(bundle);
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.frame_layout, frag).commit();
                    ft.addToBackStack("BookingAdapt");
                }
                else{
                    Toast.makeText(context, "This Session Is Not Book For Today", Toast.LENGTH_SHORT).show();
                }



            }
        });

        holder.time.setText(String.format("%02d:00", object.getTime()));
        holder.clientName.setText(strClient);

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView time, status, clientName;
        public ViewHolder(View itemView){
            super(itemView);
            time = itemView.findViewById(R.id.txtTime);
            status = itemView.findViewById(R.id.txtStatus);
            clientName = itemView.findViewById(R.id.txtClientName);
        }
    }
}
