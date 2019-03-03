package com.example.sihtry1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.sihtry1.models.RCR;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NrcListAdapter extends FirestoreRecyclerAdapter<RCR, NrcListAdapter.NrcHolder> {
    private OnItemClickListener listener;


    public NrcListAdapter(@NonNull FirestoreRecyclerOptions<RCR> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull NrcListAdapter.NrcHolder holder, int position, @NonNull RCR model) {
        holder.textViewnrctitle.setText(model.getTitle());
        holder.textviewaddress.setText(model.getAddress());
    }

    @NonNull
    @Override
    public NrcHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nrclistitem, parent, false);
        return new NrcHolder(v);


    }


    class NrcHolder extends RecyclerView.ViewHolder{
        TextView textViewnrctitle;
        TextView textviewaddress;

        public NrcHolder(@NonNull View itemView) {
            super(itemView);
            textViewnrctitle = itemView.findViewById(R.id.textviewnrctitle);
            textviewaddress = itemView.findViewById(R.id.textviewaddress);

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
