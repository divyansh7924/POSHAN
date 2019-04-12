package com.example.sihtry1.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sihtry1.R;
import com.example.sihtry1.SendReferralActivity;
import com.example.sihtry1.models.NRC;

import java.util.ArrayList;

public class NrcListAdapter extends RecyclerView.Adapter<NrcListAdapter.ViewHolder> {

    private static final String TAG = "NrcListAdapter";
    private ArrayList<NRC> nrcArrayList = new ArrayList<>();
    private Context context;

    public NrcListAdapter(Context context, ArrayList<NRC> nrcArrayList) {
        this.nrcArrayList = nrcArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nrclistitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.v(TAG, "onBindViewHolder called");
        viewHolder.tv_nrcTitle.setText(nrcArrayList.get(i).getTitle());
        viewHolder.tv_address.setText(nrcArrayList.get(i).getAddress());
        viewHolder.tv_distance.setText(String.valueOf(nrcArrayList.get(i).getDistance()));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendref(nrcArrayList.get(i).getUser_id(), i);
            }
        });
    }

    public void sendref(String id, int i) {
        Intent intent = new Intent(context, SendReferralActivity.class);
        intent.putExtra("NRC_ID", id);
        intent.putExtra("NRCobj", nrcArrayList.get(i));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return nrcArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nrcTitle, tv_address, tv_distance;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.textviewaddress);
            tv_distance = itemView.findViewById(R.id.textviewdistance);
            tv_nrcTitle = itemView.findViewById(R.id.textviewnrctitle);
            parentLayout = itemView.findViewById(R.id.nrclist_parent_layout);
        }
    }
}
