package com.lahacks.sardines;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.*;

public class StartGame extends Activity {
	
	public int connectedFriends;
	
	TextView contentView;
	String pin; //player's identification string

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		connectedFriends = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);
		// Get random game code
		int min = 1000;
		int max = 9999;
		int randomCode = min + (int)(Math.random() * ((max - min) + 1));
		// Get code text from activity_start_game.xml and change it to the random code
		contentView = (TextView) findViewById(R.id.codeText);
		contentView.setText(Integer.toString(randomCode));
		// Show the Up button in the action bar.
		
		//set up database for game
		Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
		database.child("GAME ID " + randomCode).setValue(randomCode);
		Firebase dataRef = database.child("GAME ID " + randomCode);
		
		//add hideout coordinates
		dataRef.child("hideout").child("latitude").setValue(0);
		dataRef.child("hideout").child("longitude").setValue(0);
		
		//get name of player
		String playerName = "";
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			playerName = (String)extras.get("name");
			System.out.println(playerName);
		}
		
		//set up player database
		Firebase newPlayerRef = dataRef.child("players").push();
		newPlayerRef.child("id").setValue(newPlayerRef.getName());
		pin = newPlayerRef.getName();
		newPlayerRef.child("name").setValue(playerName);
		newPlayerRef.child("latitude").setValue(0);
		newPlayerRef.child("longitude").setValue(0);
		newPlayerRef.child("state").setValue("hiding");
		newPlayerRef.child("hider").setValue("true"); //the original hider
		
		
		dataRef.child("numbers").child("hiding").setValue(1);
		
		ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
		
		
		setupActionBar();
		
		Firebase players = dataRef.child("players");
		players.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snap) {
				connectedFriends = (int) (snap.getChildrenCount()-1);
				onConnected();
			}
			
			@Override
			public void onCancelled(FirebaseError error) {}
		});
		
		Button beginGameBtn = (Button) findViewById(R.id.beginGameBtn);
		beginGameBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Hider.class);
				i.putExtra("gameCode", contentView.getText());
				i.putExtra("pin", pin);
				startActivity(i);
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_game, menu);
		return true;
	}
	
	public void onConnected() {
		//connectedFriends++;
		TextView view = (TextView) findViewById(R.id.friendsConnected);
		view.setText(Integer.toString(connectedFriends) + " friends have entered the room");
	}
	
	public void onDisconnect() {
		connectedFriends--;
		TextView view = (TextView) findViewById(R.id.friendsConnected);
		view.setText(Integer.toString(connectedFriends) + " friends have entered the room");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
