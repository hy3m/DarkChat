package com.dark.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Info extends AppCompatActivity 
{

	private Firebase ref;
	private TextView mAuthorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

		Firebase.setAndroidContext(this);

		mAuthorView = (TextView) findViewById(R.id.post_author);

		final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

		ref = new Firebase("https://dark-chat-c351c.firebaseio.com/info");
		ref.addValueEventListener(new ValueEventListener() 
			{
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) 
				{
					String superData =(String) dataSnapshot.getValue();
					mAuthorView.setText(superData);
				}
				@Override
				public
				void onCancelled(FirebaseError firebaseError)
				{
				}
			}
		);
	}
}
