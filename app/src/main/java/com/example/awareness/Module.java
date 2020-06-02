package com.example.awareness;

import androidx.annotation.Nullable;

import java.util.Map;

public class Module {

    private int mModuleNumber;
    private String mTopic;
    private Map<String,Object> mAttachments;

   public Module(int moduleNumber, String topic, @Nullable Map<String,Object> attachments){
        this.mModuleNumber = moduleNumber;
        this.mTopic = topic;
        this.mAttachments = attachments;
    }

   public int getModuleNumber(){
        return mModuleNumber;
    }

    public String getTopic(){
        return mTopic;
    }
    public Map<String,Object> getAttachments(){
       return mAttachments;
    }
}
