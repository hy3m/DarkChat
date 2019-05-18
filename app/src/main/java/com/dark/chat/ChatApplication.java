package com.dark.chat;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

public class ChatApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
