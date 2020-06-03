package com.example.awareness.ui.learningactivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awareness.Constants;
import com.example.awareness.Module;
import com.example.awareness.PdfViewActivity;
import com.example.awareness.QuizUtils;
import com.example.awareness.R;
import com.example.awareness.ui.Form;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import static com.example.awareness.Constants.User;
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
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        final String userId = preferences.getString(User.USER_CONTACT_NUMBER, null);

        if (MODULE_NUMBER <= Constants.User.accessModule) {
            holder.lock.setVisibility(View.GONE);

            holder.attachedLink.setVisibility(View.GONE);
            holder.attachedFile.setVisibility(View.VISIBLE);
            holder.attachedLecture.setVisibility(View.VISIBLE);
            holder.attachedFile.setEnabled(true);
            holder.attachedLink.setEnabled(true);
            holder.attachedLecture.setEnabled(true);


            if (MODULE_NUMBER != Constants.User.accessModule) {
                holder.test.setEnabled(true);
                holder.test.setTextColor(Color.parseColor("#64DD17"));
                holder.attachedFileText.setTextColor(Color.parseColor("#64DD17"));
                holder.attachedLinkText.setTextColor(Color.parseColor("#64DD17"));
                holder.attachedLectureText.setTextColor(Color.parseColor("#64DD17"));

            } else {
                if (User.progressPdf) {
                    holder.attachedFileText.setTextColor(Color.parseColor("#64DD17"));
                }
                if (User.progressLink) {
                    holder.attachedLinkText.setTextColor(Color.parseColor("#64DD17"));
                }
                if (User.progressLecture) {
                    holder.attachedLectureText.setTextColor(Color.parseColor("#64DD17"));
                }
                if (User.progressPdf && User.progressLink && User.progressLecture) {
                    holder.test.setEnabled(true);
                }
            }
        } else {
            holder.attachedFile.setEnabled(false);
            holder.attachedLink.setEnabled(false);
            holder.attachedLecture.setEnabled(false);
            holder.test.setEnabled(false);
        }

        holder.moduleNumber.setText("Module: " + MODULE_NUMBER);
        holder.topicName.setText(mModules.get(position).getTopic());
        holder.attachedLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODULE_NUMBER == Constants.User.accessModule) {
                    User.progressLecture = true;
                    learningAdapter.notifyDataSetChanged();
                    if (userId != null) {
                        FirebaseFirestore.getInstance().collection("users").document(userId).update(User.PROGRESS_LECTURE, User.progressLecture);
                    }
                }
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mContext, Uri.parse(Objects.requireNonNull(mModules.get(position).getAttachments().get("Lecture")).toString()));
            }
        });
        holder.attachedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODULE_NUMBER == Constants.User.accessModule) {
                    User.progressPdf = true;
                    learningAdapter.notifyDataSetChanged();
                    if (userId != null) {
                        FirebaseFirestore.getInstance().collection("users").document(userId).update(User.PROGRESS_PDF, User.progressPdf);
                    }
                }
                Intent intent = new Intent(mContext, PdfViewActivity.class);
                intent.putExtra("mode1", MODULE_NUMBER);
                mContext.startActivity(intent);
              /*  File pdfFile = new File("res/raw/ppt1.pdf");
                Uri path = Uri.fromFile(pdfFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                   mContext.startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext,
                            "No Application available to viewPDF",
                            Toast.LENGTH_SHORT).show();
                }*/
               /*AssetManager assetManager = getAssets();
                Uri path = Uri.fromFile(A);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path , "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    mContext.startActivity(pdfIntent);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext,
                            "No Application available to viewPDF",
                            Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        holder.attachedLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODULE_NUMBER == Constants.User.accessModule) {
                    User.progressLink = true;
                    learningAdapter.notifyDataSetChanged();
                    if (userId != null) {
                        FirebaseFirestore.getInstance().collection("users").document(userId).update(User.PROGRESS_LINK, User.progressLink);
                    }
                }
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mContext, Uri.parse(Objects.requireNonNull(mModules.get(position).getAttachments().get("Link")).toString()));
            }
        });
        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizBottomSheetDialog.show();
                quizBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                QuizUtils.createQuiz(mContext, MODULE_NUMBER);
            }
        });
    }


    /*private void CopyReadAssets() {
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "abc.pdf");
        try {
            in = assetManager.open("abc.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getFilesDir() + "/abc.pdf"),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
*/

    @Override
    public int getItemCount() {
        return mModules.size();
    }

    public class LearningViewHolder extends RecyclerView.ViewHolder {

        TextView moduleNumber, topicName, test, attachedFileText, attachedLinkText, attachedLectureText;
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
            attachedLectureText = itemView.findViewById(R.id.attached_lecture_text);

        }
    }
}




