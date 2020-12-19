package com.example.chatspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.HolderGroupChat> {
    private Context context;
    private ArrayList<GroupMessage> groupMessageArrayList;
    private FirebaseAuth firebaseAuth;

    public GroupChatAdapter(Context context, ArrayList<GroupMessage> groupMessageArrayList){
        this.context = context;
        this.groupMessageArrayList = groupMessageArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderGroupChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_group_chat,parent,false);
        return new HolderGroupChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupChat holder, int position) {

        // get Data
        GroupMessage model = groupMessageArrayList.get(position);
        String message = model.getMessage();
        String name = model.getName();
        String time = model.getTime();


        // set Data
        holder.messageTv.setText(message);
        holder.nameTv.setText(name);
        holder.timeTv.setText(time);

    }

    @Override
    public int getItemCount() {
        return groupMessageArrayList.size();
    }

    class HolderGroupChat extends RecyclerView.ViewHolder{
        private TextView nameTv, messageTv, timeTv;

        public HolderGroupChat(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }
}
