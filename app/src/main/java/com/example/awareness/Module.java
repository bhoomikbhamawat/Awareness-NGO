package com.example.awareness;

public class Module {

    private int mModuleNumber;
    private String mTopic;

   public Module(int moduleNumber, String topic){
        this.mModuleNumber = moduleNumber;
        this.mTopic = topic;
    }

   public int getModuleNumber(){
        return mModuleNumber;
    }

    public String getTopic(){
        return mTopic;
    }
}
