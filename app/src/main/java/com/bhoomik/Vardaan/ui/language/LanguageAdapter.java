package com.bhoomik.Vardaan.ui.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhoomik.Vardaan.R;
import com.bhoomik.Vardaan.model.Language;
import com.bhoomik.Vardaan.utils.Constants;
import com.bhoomik.Vardaan.utils.TranslateUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;

import java.util.List;
import java.util.Set;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private Context mContext;
    private List<Language> mLanguages;

    public LanguageAdapter(Context context, List<Language> languages) {
        this.mContext = context;
        this.mLanguages = languages;

    }


    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LanguageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mLanguages.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final LanguageViewHolder holder, final int position) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        String languagePreference = sharedPreferences.getString(Constants.QUIZ_LANGUAGE_PREFERENCE, TranslateLanguage.HINDI);

        holder.language.setText(mLanguages.get(position).getLanguage());

        if (mLanguages.get(position).getTranslateLanguageId().equals(languagePreference)) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.INVISIBLE);
        }

        RemoteModelManager modelManager = RemoteModelManager.getInstance();

        // Get translation models stored on the device.
        modelManager.getDownloadedModels(TranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<TranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<TranslateRemoteModel> models) {
//                        Iterator<TranslateRemoteModel> iterator = models.iterator();
//                        while (iterator.hasNext()){
//                            if(mLanguagesiterator.next().getLanguage()
//                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.QUIZ_LANGUAGE_PREFERENCE, mLanguages.get(position).getTranslateLanguageId());
                editor.apply();

                notifyDataSetChanged();
            }
        });

        SharedPreferences.Editor editor = sharedPreferences.edit();
        holder.downloadDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateUtils.deleteLanguage(mContext, mLanguages.get(position).getTranslateLanguageId(), v);
            }
        });


    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {

        TextView language;
        ImageView check, downloadDelete;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);

            language = itemView.findViewById(R.id.language);
            check = itemView.findViewById(R.id.check);
            downloadDelete = itemView.findViewById(R.id.download_delete);

        }
    }


}
