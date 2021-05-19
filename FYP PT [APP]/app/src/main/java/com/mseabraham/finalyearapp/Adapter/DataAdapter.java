package com.mseabraham.finalyearapp.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mseabraham.finalyearapp.Models.Set;
import com.mseabraham.finalyearapp.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Set> set;

    public DataAdapter(Context context, ArrayList<Set> set) {
        this.context = context;
        this.set = set;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Set obj = set.get(position);

        holder.set = obj;

        holder.setNumber.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return set.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText reps, weight;
        public TextView setNumber;
        public Set set;

        ViewHolder(View itemView) {
            super(itemView);
            reps = itemView.findViewById(R.id.etReps);
            weight = itemView.findViewById(R.id.etWeight);

            reps.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        set.reps = Long.parseLong(s.toString());
                    } catch (Exception e) {

                    }
                }
            });

            weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        set.weight = Long.parseLong(s.toString());
                    } catch (Exception e) {

                    }
                }
            });
            setNumber = itemView.findViewById(R.id.setCount);
        }
    }
}
