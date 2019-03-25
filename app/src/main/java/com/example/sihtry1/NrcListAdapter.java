package com.example.sihtry1;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihtry1.models.NRC;
import com.example.sihtry1.models.RCR;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class NrcListAdapter extends FirestoreRecyclerAdapter<NRC, NrcListAdapter.NrcHolder> {
    private OnItemClickListener listener;
    FirebaseFirestore db;
    CollectionReference rcrRef;
    String userId;
    RCR rcr = null;
    DocumentReference docRef;


    public NrcListAdapter(@NonNull FirestoreRecyclerOptions<NRC> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final NrcListAdapter.NrcHolder holder, int position, @NonNull NRC model) {
        holder.textViewnrctitle.setText(model.getTitle());
        holder.textviewaddress.setText(model.getAddress());
        final Double lon = model.getLon();
        final Double lat = model.getLat();
        db = FirebaseFirestore.getInstance();
        rcrRef = db.collection("rcr");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = rcrRef.whereEqualTo("user_id", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        rcr = documentSnapshot.toObject(RCR.class);
                        docRef = documentSnapshot.getReference();
                    }
                    Double lata = rcr.getLat();
                    Double lona = rcr.getLon();
                    Location loc1 = new Location("");
                    loc1.setLatitude(lata);
                    loc1.setLongitude(lona);

                    Location loc2 = new Location("");
                    loc2.setLatitude(lat);
                    loc2.setLongitude(lon);
                    float distanceInMeters = loc1.distanceTo(loc2) / 1000;
                    holder.textviewdistance.setText(String.valueOf(distanceInMeters)+" Km");
                } else {
//                    Toast.makeText(getApplicationContext(), "NRC not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public NrcHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nrclistitem, parent, false);
        return new NrcHolder(v);


    }


    class NrcHolder extends RecyclerView.ViewHolder {
        TextView textViewnrctitle;
        TextView textviewaddress;
        TextView textviewdistance;

        public NrcHolder(@NonNull View itemView) {
            super(itemView);
            textViewnrctitle = itemView.findViewById(R.id.textviewnrctitle);
            textviewaddress = itemView.findViewById(R.id.textviewaddress);
            textviewdistance = itemView.findViewById(R.id.textviewdistance);

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
