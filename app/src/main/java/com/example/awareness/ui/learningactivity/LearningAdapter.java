package com.example.awareness.ui.learningactivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awareness.Module;
import com.example.awareness.R;
import com.example.awareness.ui.TestActivity;

import java.util.List;

public class LearningAdapter extends RecyclerView.Adapter<LearningAdapter.LearningViewHolder> {

    private Context mContext;
    private List<Module> mModules;

    public LearningAdapter(Context context, List<Module> modules){
        this.mContext = context;
        this.mModules = modules;
    }

    @NonNull
    @Override
    public LearningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new LearningViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.childview_learning_recyclerview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LearningViewHolder holder, final int position) {

        holder.moduleNumber.setText("Module: " + mModules.get(position).getModuleNumber());
        holder.topicName.setText(mModules.get(position).getTopic());
        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestActivity.class);
                intent.putExtra("ModuleNumber",mModules.get(position).getModuleNumber());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mModules.size();
    }

    class LearningViewHolder extends RecyclerView.ViewHolder {

        TextView moduleNumber,topicName,test;
        public LearningViewHolder(@NonNull View itemView) {
            super(itemView);

            moduleNumber = itemView.findViewById(R.id.module_number);
            topicName = itemView.findViewById(R.id.topic_name);
            test = itemView.findViewById(R.id.test);

        }
    }
}
