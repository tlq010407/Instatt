package com.example.instatt.Class;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.Modules.AbsentForm;
import com.example.instatt.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ClassAdapter extends FirebaseRecyclerAdapter<ClassItem,ClassAdapter.myViewHolder> {
    private final Context context;
    private final String studentCode;
    private final String moduleName;
    private final String moduleCode;

    public ClassAdapter(@NonNull FirebaseRecyclerOptions<ClassItem> options, Context context, String studentCode, String moduleName, String moduleCode) {
        super(options);
        this.context = context;
        this.moduleName = moduleName;
        this.studentCode = studentCode;
        this.moduleCode = moduleCode;
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassAdapter.myViewHolder holder, int position, @NonNull ClassItem classItem) {
        try {
            holder.Date.setText(classItem.getDate());
            holder.EndTime.setText(classItem.getEndTime());
            holder.Room.setText(classItem.getRoom());
            holder.StartTime.setText(classItem.getStartTime());
            holder.Type.setText(classItem.getType().toUpperCase());
            if ("EXCUSED ABSENT".equalsIgnoreCase(classItem.getStatus())){
                holder.Status.setText("Reported");
                holder.status_icon.setImageResource(R.drawable.user_minus_alt_1_svgrepo_com);
                holder.status_icon.setClickable(false);
            }else if ("ABSENT".equalsIgnoreCase(classItem.getStatus())){
                holder.Status.setText(classItem.getStatus().toUpperCase());
                holder.status_icon.setClickable(true); // Enable the ImageButton for click actions
                holder.status_icon.setImageResource(R.drawable.user_times);
                holder.status_icon.setOnClickListener(v -> {
                    // Use the context passed to the adapter
                    Intent intent = new Intent(context, AbsentForm.class);
                    intent.putExtra("studentCode", studentCode);
                    intent.putExtra("moduleName", moduleName);
                    intent.putExtra("DateAbsent", classItem.getDate());
                    intent.putExtra("moduleCode", moduleCode);
                    context.startActivity(intent);
                });
            } else if ("SIGNED".equalsIgnoreCase(classItem.getStatus())) {
                holder.status_icon.setImageResource(R.drawable.user_check);
                holder.status_icon.setClickable(false);
                holder.Status.setText("SIGNED");
            } else if ("LOCK".equalsIgnoreCase(classItem.getStatus()) || "UNLOCK".equalsIgnoreCase(classItem.getStatus())) {
                holder.status_icon.setImageResource(R.drawable.baseline_lock_24);
                holder.Status.setText("LOCK");
                holder.status_icon.setClickable(false);
            }
        } catch (Exception e) {
            Log.e("ClassAdapter", "Error binding data to ViewHolder", e);
        }
    }

    @NonNull
    @Override
    public ClassAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_history, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView Type,EndTime, StartTime, Status, Room, Date;
        ImageButton status_icon;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.textViewDate);
            EndTime = itemView.findViewById(R.id.textViewEndTime);
            Room = itemView.findViewById(R.id.textViewRoom);
            StartTime = itemView.findViewById(R.id.textViewStartTime);
            Status = itemView.findViewById(R.id.statusView);
            Type = itemView.findViewById(R.id.textViewType);
            status_icon = itemView.findViewById(R.id.status_icon);
        }
    }


}
