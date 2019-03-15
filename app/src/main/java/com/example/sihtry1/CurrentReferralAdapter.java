package com.example.sihtry1;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.sihtry1.CurrentReferralsActivity;

public class CurrentReferralAdapter extends FirestoreRecyclerAdapter<Referral, CurrentReferralAdapter.CurrentReferralHolder> {
    private OnItemClickListener listener;


    public CurrentReferralAdapter(@NonNull FirestoreRecyclerOptions<Referral> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull CurrentReferralAdapter.CurrentReferralHolder holder, int position, @NonNull Referral model) {
        holder.textViewchildname.setText(model.getChild_first_name());
        holder.textviewdob.setText("DOB: " + model.getDay_of_birth() + "/" + model.getMonth_of_birth() + "/" + model.getYear_of_birth());
        holder.textrcrid.setText("Guardian Name: " + model.getGuadian_name());
        int seen = model.getSeen();
        switch (seen) {
            case (1):
                Log.v("Statusw", "yes inside");
                holder.ritem.setBackgroundColor(Color.parseColor("#E2FBFB"));
                break;
            default:
                System.out.print("not Acknowledged state");
        }
    }

    @NonNull
    @Override
    public CurrentReferralHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileitem, parent, false);
        return new CurrentReferralHolder(v);
    }


    class CurrentReferralHolder extends RecyclerView.ViewHolder {
        TextView textViewchildname;
        TextView textviewdob;
        TextView textrcrid;
        LinearLayout ritem;

        public CurrentReferralHolder(@NonNull View itemView) {
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
