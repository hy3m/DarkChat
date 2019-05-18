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
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://dark-chat-c351c.firebaseio.com";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
	ListView listView1;

	private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
		{
            setupDrawerContent(navigationView);
        }

		final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("Dark Chat");

		setupUsername();

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
				{
					if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
					{
						sendMessage();
					}
					return true;
				}
			});
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					sendMessage();
				}
			});

    }

    @Override
    public void onStart()
	{
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.list);
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
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
						Toast.makeText(MainActivity.this, "Connected to Dark Chat", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(MainActivity.this, "Disconnected from Dark Chat", Toast.LENGTH_SHORT).show();
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

	private void setupUsername()
	{
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null)
		{
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
			mUsername = "Hacker" + r.nextInt(100000);
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage()
	{
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals(""))
		{
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{

		switch (item.getItemId())
		{
			case R.id.join:
				Intent a = new
					Intent(getApplicationContext(), Join.class);
				startActivity(a);
				return true;
				
			case R.id.action_about:
				Intent i = new
					Intent(getApplicationContext(), Tentang.class);
				startActivity(i);
				return true;

		    case R.id.profil:
				Intent o = new
					Intent(getApplicationContext(), Info.class);
				startActivity(o);
				return true;

			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
        int i = item.getItemId();
        if (i == R.id.action_logout) 
		{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } 
		else 
		{		
            return super.onOptionsItemSelected(item);
        }

	}

	private void setupDrawerContent(NavigationView navigationView)
	{
        navigationView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener() 
			{
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem)
				{
					
					switch (menuItem.getItemId())
					{
						case R.id.nav_normal:
							Intent e = new
								Intent(getApplicationContext(), Normal.class);
							startActivity(e);
							return true;
							
						case R.id.nav_hacker:
							Intent i = new
								Intent(getApplicationContext(), Hacker.class);
							startActivity(i);
							return true;
							
						case R.id.nav_misi:
							Intent o = new
								Intent(getApplicationContext(), Tutorial.class);
							startActivity(o);
							return true;
							
						case R.id.nav_mission:
							Intent m = new
								Intent(getApplicationContext(), Mission.class);
							startActivity(m);
							return true;
							
						case R.id.nav_member:
							Intent b = new
								Intent(getApplicationContext(), Member.class);
							startActivity(b);
							return true;
					}
					
					menuItem.setChecked(true);
					mDrawerLayout.closeDrawers();
					return true;
				}
			}
		);

	}
}
