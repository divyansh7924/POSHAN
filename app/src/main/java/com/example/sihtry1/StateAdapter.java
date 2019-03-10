package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sihtry1.models.RCR;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class StateAdapter extends FirestoreRecyclerAdapter<RCR, StateAdapter.StateHolder> {
    private OnItemClickListener listener;


    public StateAdapter(@NonNull FirestoreRecyclerOptions<RCR> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StateHolder holder, int position, @NonNull RCR model) {
        holder.textViewtitle.setText(model.getState());
    }

    @NonNull
    @Override
    public StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new StateHolder(v);
    }

    class StateHolder extends RecyclerView.ViewHolder{
        TextView textViewtitle;

        public StateHolder(@NonNull View itemView) {
            super(itemView);
            textViewtitle = itemView.findViewById(R.id.textviewtitle);

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
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
