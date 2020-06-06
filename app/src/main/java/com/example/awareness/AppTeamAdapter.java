package com.example.awareness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awareness.ui.AboutIndividual;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppTeamAdapter extends RecyclerView.Adapter<AppTeamAdapter.TeamViewHolder> {

    Context context;
    List<AboutIndividual> developers;

    public AppTeamAdapter(Context context, List<AboutIndividual> developers) {
        this.context = context;
        this.developers = developers;
    }

    @NonNull
    @Override
    public AppTeamAdapter.TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.about_individual_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppTeamAdapter.TeamViewHolder holder, int position) {

        holder.pictureImageView.setImageResource(developers.get(position).getImageName());
        holder.nameTextView.setText(developers.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return developers.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private CircleImageView pictureImageView;
        private TextView nameTextView;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_individual_about);
            pictureImageView = itemView.findViewById(R.id.picture_about);
            nameTextView = itemView.findViewById(R.id.name_individual_about);
        }
    }
}
