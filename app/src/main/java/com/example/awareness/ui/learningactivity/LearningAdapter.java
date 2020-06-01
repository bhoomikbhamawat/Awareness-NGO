package com.example.awareness.ui.learningactivity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awareness.Constants;
import com.example.awareness.Module;
import com.example.awareness.QuizUtils;
import com.example.awareness.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static com.example.awareness.Constants.User.PROGRESS_LINK;
import static com.example.awareness.Constants.User.PROGRESS_PDF;
import static com.example.awareness.Constants.User.progressLink;
import static com.example.awareness.Constants.User.progressPdf;
import static com.example.awareness.ui.learningactivity.LearningActivity.learningAdapter;
import static com.example.awareness.ui.learningactivity.LearningActivity.quizBottomSheetDialog;

public class LearningAdapter extends RecyclerView.Adapter<LearningAdapter.LearningViewHolder> {

    private Context mContext;
    private List<Module> mModules;

    public LearningAdapter(Context context, List<Module> modules) {
        this.mContext = context;
        this.mModules = modules;
    }

    @NonNull
    @Override
    public LearningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LearningViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.childview_learning_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LearningViewHolder holder, final int position) {
        final int MODULE_NUMBER = mModules.get(position).getModuleNumber();

        if (MODULE_NUMBER <= Constants.User.accessModule) {
            holder.lock.setVisibility(View.GONE);

            holder.attachedFile.setEnabled(true);
            holder.attachedLink.setEnabled(true);


            if (MODULE_NUMBER != Constants.User.accessModule) {
                holder.test.setEnabled(true);
                holder.test.setTextColor(Color.parseColor("#64DD17"));
                holder.attachedFileText.setTextColor(Color.parseColor("#64DD17"));
                holder.attachedLinkText.setTextColor(Color.parseColor("#64DD17"));

            } else {
                if (progressPdf) {
                    holder.attachedFileText.setTextColor(Color.parseColor("#64DD17"));
                }
                if (progressLink) {
                    holder.attachedLinkText.setTextColor(Color.parseColor("#64DD17"));
                }
                if (progressPdf && progressLink) {
                    holder.test.setEnabled(true);
                }
            }
        } else {
            holder.attachedFile.setEnabled(false);
            holder.attachedLink.setEnabled(false);
            holder.test.setEnabled(false);
        }

        holder.moduleNumber.setText("Module: " + MODULE_NUMBER);
        holder.topicName.setText(mModules.get(position).getTopic());
        holder.attachedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODULE_NUMBER == Constants.User.accessModule) {
                    progressPdf = true;
                    learningAdapter.notifyDataSetChanged();
                    String userID = "0123456789";
                    FirebaseFirestore.getInstance().collection("users").document(userID).update(PROGRESS_PDF, progressPdf);
                }
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mContext, Uri.parse(mModules.get(position).getAttachments().get("Pdf")));
            }
        });
        holder.attachedLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODULE_NUMBER == Constants.User.accessModule) {
                    progressLink = true;
                    learningAdapter.notifyDataSetChanged();
                    String userID = "0123456789";
                    FirebaseFirestore.getInstance().collection("users").document(userID).update(PROGRESS_LINK, progressLink);
                }
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mContext, Uri.parse(mModules.get(position).getAttachments().get("Link")));
            }
        });
        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizBottomSheetDialog.show();
                quizBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                QuizUtils.createQuiz(mContext,MODULE_NUMBER);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModules.size();
    }

    public class LearningViewHolder extends RecyclerView.ViewHolder {

        TextView moduleNumber, topicName, test, attachedFileText, attachedLinkText;
        LinearLayout attachedFile, attachedLecture, attachedLink;
        ImageView lock;

        public LearningViewHolder(@NonNull View itemView) {
            super(itemView);

            moduleNumber = itemView.findViewById(R.id.module_number);
            topicName = itemView.findViewById(R.id.topic_name);
            test = itemView.findViewById(R.id.test);
            attachedFile = itemView.findViewById(R.id.attached_file);
            attachedLecture = itemView.findViewById(R.id.attached_lecture);
            attachedLink = itemView.findViewById(R.id.attached_link);
            lock = itemView.findViewById(R.id.lock);
            attachedFileText = itemView.findViewById(R.id.attached_file_text);
            attachedLinkText = itemView.findViewById(R.id.attached_link_text);

        }
    }

}
