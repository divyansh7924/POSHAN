package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.sihtry1.models.Referral;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RcrPastRecordsAdapter extends FirestoreRecyclerAdapter<Referral, RcrPastRecordsAdapter.RcrPastRecordsHolder> {
    private OnItemClickListener listener;


    public RcrPastRecordsAdapter(@NonNull FirestoreRecyclerOptions<Referral> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull RcrPastRecordsAdapter.RcrPastRecordsHolder holder, int position, @NonNull Referral model) {
        holder.ritem.setClickable(false);
        holder.textViewchildname.setText(model.getChild_first_name());
        holder.textviewdob.setText("Status: " + model.getStatus());
        holder.textrcrid.setText("Guardian Name: " + model.getGuadian_name());
    }

    @NonNull
    @Override
    public RcrPastRecordsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileitem, parent, false);
        return new RcrPastRecordsHolder(v);
    }


    class RcrPastRecordsHolder extends RecyclerView.ViewHolder {
        TextView textViewchildname;
        TextView textviewdob;
        TextView textrcrid;
        LinearLayout ritem;

        public RcrPastRecordsHolder(@NonNull View itemView) {
            super(itemView);
            textViewchildname = itemView.findViewById(R.id.textviewname);
            textviewdob = itemView.findViewById(R.id.textviewage);
            textrcrid = itemView.findViewById(R.id.textviewguardian);
            ritem = itemView.findViewById(R.id.ritem);

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
