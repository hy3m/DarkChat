package com.dark.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.widget.EditText;
import android.view.View;

public class Petunjuk extends AppCompatActivity 
{

    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petunjuk);

		final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
	}
}
