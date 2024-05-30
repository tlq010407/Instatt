package com.example.instatt.Modules;

import static com.example.instatt.LogIn.userEmail;
import static com.example.instatt.retrieveUser.getStringBeforeAt;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instatt.Class.ClassItem;
import com.example.instatt.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ModuleAdapter extends FirebaseRecyclerAdapter<Module,ModuleAdapter.myViewHolder> {
    private ModuleAdapter.OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(ModuleAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public ModuleAdapter(@NonNull FirebaseRecyclerOptions<Module> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Module model) {
        try {
            holder.ModuleCode.setText(model.getModuleCode());
            holder.Year.setText(model.getYear());
            holder.Name.setText(model.getName());
            String studentCode = getStringBeforeAt(userEmail);
            Query query = FirebaseDatabase.getInstance().getReference().child("students").child(studentCode).child("Class").child(model.getModuleCode());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int totalClasses = (int) dataSnapshot.getChildrenCount();
                    int locked = 0, signed = 0;
                    for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                        ClassItem classItem = classSnapshot.getValue(ClassItem.class);
                        if (classItem != null) {
                            String status = classItem.getStatus().toLowerCase(); // Convert to lowercase
                            switch (status) {
                                case "lock":
                                    locked++;
                                    break;
                                case "signed":
                                    signed++;
                                    break;
                                // Add more cases as needed, using lowercase status values
                            }
                        }
                    }
                    totalClasses = totalClasses - locked;
                    // Now you have the count of each status, you can update your UI accordingly
                    double progress = (double) signed / totalClasses * 100;
                    updateProgressBar(progress, holder);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e("ModuleAdapter", "Error binding data to ViewHolder", e);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_design,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView ModuleCode, Name, Year, textViewProgress;
        ProgressBar progressBar;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ModuleCode = itemView.findViewById(R.id.module_code);
            Name = itemView.findViewById(R.id.modulename);
            Year = itemView.findViewById(R.id.module_year);
            textViewProgress = itemView.findViewById(R.id.text_view_progress);
            progressBar = itemView.findViewById(R.id.progress_bar);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getLayoutPosition()));
        }
    }

    private void updateProgressBar(double progress, myViewHolder holder) {
        if (!Double.isNaN(progress)) {
            holder.progressBar.setProgress((int) progress);
            holder.textViewProgress.setText((int)progress + "%");
        } else {
            holder.progressBar.setProgress(0);
            holder.textViewProgress.setText("0%");
        }
    }
}
