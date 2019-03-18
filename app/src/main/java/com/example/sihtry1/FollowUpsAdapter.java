package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sihtry1.models.Followup;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FollowUpsAdapter extends FirestoreRecyclerAdapter<Followup, FollowUpsAdapter.FollowUpsHolder> {
    private OnItemClickListener listener;


    public FollowUpsAdapter(FirestoreRecyclerOptions<Followup> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull FollowUpsAdapter.FollowUpsHolder holder, int position, @NonNull Followup model) {
        holder.textViewchildname.setText("Child Name: "+model.getChild_first_name()+" "+model.getChild_last_name());
        holder.textviewdob.setText("Next Followup on: " + model.getNext_date());
        holder.textrcrid.setText("Guardian Name: " + model.getGuardian_name());
    }

    @NonNull
    @Override
    public FollowUpsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileitem, parent, false);
        return new FollowUpsHolder(v);
    }


    class FollowUpsHolder extends RecyclerView.ViewHolder {
        TextView textViewchildname;
        TextView textviewdob;
        TextView textrcrid;

        public FollowUpsHolder(@NonNull View itemView) {
            super(itemView);
            textViewchildname = itemView.findViewById(R.id.textviewname);
            textviewdob = itemView.findViewById(R.id.textviewage);
            textrcrid = itemView.findViewById(R.id.textviewguardian);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}