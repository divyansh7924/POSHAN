package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sihtry1.models.PastRecord;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PastRecordsNrcAdapter extends FirestoreRecyclerAdapter<PastRecord, PastRecordsNrcAdapter.PastRecordsNrcHolder> {
    private OnItemClickListener listener;


    public PastRecordsNrcAdapter(FirestoreRecyclerOptions<PastRecord> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PastRecordsNrcAdapter.PastRecordsNrcHolder holder, int position, @NonNull PastRecord model) {
        holder.textViewchildname.setText("Child Name: " + model.getChild_first_name());
        holder.textrcrid.setText("Guardian Name: " + model.getGuardian_name());
        holder.textviewdob.setText(model.getDay_of_birth() + "/" + model.getMonth_of_birth() + "/" + model.getYear_of_birth());
    }

    @NonNull
    @Override
    public PastRecordsNrcHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileitem, parent, false);
        return new PastRecordsNrcHolder(v);
    }


    class PastRecordsNrcHolder extends RecyclerView.ViewHolder {
        TextView textViewchildname;
        TextView textviewdob;
        TextView textrcrid;

        public PastRecordsNrcHolder(@NonNull View itemView) {
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