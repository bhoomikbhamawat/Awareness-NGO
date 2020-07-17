package com.bhoomik.Vardaan.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import static android.content.ContentValues.TAG;

public class TranslateUtils {

    public static void setTranslatedText(final ProgressDialog progressDialog, String sourceLanguage, final String targetLanguage, @Nullable final TextView textView, @Nullable final RadioButton radioButton, final String text) {


        if (sourceLanguage.equals(targetLanguage)) {
            if (textView != null) {
                textView.setText(text);
            } else if (radioButton != null) {
                radioButton.setText(text);
            }
        } else {
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage)
                    .setTargetLanguage(targetLanguage)
                    .build();

            final Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();


            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Setting up language");
            progressDialog.setMessage("Downloading language");
            progressDialog.setCancelable(false);
            progressDialog.show();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void v) {
                    progressDialog.dismiss();
                    Log.e(TAG, "Translator downloaded");

                    translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(@NonNull String translatedText) {
                            if (textView != null) {
                                textView.setText(translatedText);
                            } else if (radioButton != null) {
                                radioButton.setText(translatedText);
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.e(TAG, e.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.setMessage("Unable to download language!\n\nMake sure you are connected to internet");
                    progressDialog.setCancelable(true);
                    Log.e(TAG, "Translator download failed : " + e);

                }
            });
        }

    }

    public static void deleteLanguage(final Context context, String targetLanguage, final View view) {
        RemoteModelManager modelManager = RemoteModelManager.getInstance();

        // Delete the German model if it's on the device.
        TranslateRemoteModel germanModel = new TranslateRemoteModel.Builder(targetLanguage).build();
        modelManager.deleteDownloadedModel(germanModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                view.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "Language Deleted", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Something went wrong! Try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
