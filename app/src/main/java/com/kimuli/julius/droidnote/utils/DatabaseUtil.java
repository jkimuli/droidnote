package com.kimuli.julius.droidnote.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Used to return a single instance of the Firebase through
 * the lifecycle of the application
 */

public class DatabaseUtil {

    private static FirebaseDatabase sDatabase;

    public static FirebaseDatabase getDatabase(){
        if(sDatabase == null){
            sDatabase = FirebaseDatabase.getInstance();
            sDatabase.setPersistenceEnabled(true);
        }

        return sDatabase;
    }

}
