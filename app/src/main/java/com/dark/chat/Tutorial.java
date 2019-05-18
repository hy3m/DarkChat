package com.dark.chat;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Tutorial extends AppCompatActivity
{

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://dark-chat-c351c.firebaseio.com";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
	ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

		final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("Tutorial");
		
        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("tutorial");

    }

    @Override
    public void onStart()
	{
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.list);
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_tutorial, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged()
				{
					super.onChanged();
					listView.setSelection(mChatListAdapter.getCount() - 1);
				}
			});

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					boolean connected = (Boolean) dataSnapshot.getValue();
					if (connected)
					{
						Toast.makeText(Tutorial.this, "Connected to Tutorial", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(Tutorial.this, "Disconnected from Tutorial", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onCancelled(FirebaseError firebaseError)
				{
					// No-op
				}
			});
    }

    @Override
    public void onStop()
	{
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }
}
