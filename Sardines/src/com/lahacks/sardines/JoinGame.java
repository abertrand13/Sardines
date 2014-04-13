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
import android.widget.EditText;
import android.util.Log;
import android.text.TextWatcher;
import android.text.Editable;

import com.firebase.client.*;

public class JoinGame extends Activity {

	private final static String LOG_TAG = "JoinGame";
	
	Button enterGameBtn;
	EditText enterGameTxt;
	
	//global variables are bad?
	boolean gameExists = false;
	String inputCode;
	
	//Intent to start the seeker.
	Intent i;
	String playerName;
	ValueEventListener listener;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_game);
		// Show the Up button in the action bar.
		//setupActionBar();

		enterGameTxt = (EditText) findViewById(R.id.enterGameTxt);
		enterGameBtn = (Button) findViewById(R.id.enterGameBtn);
		Log.v(LOG_TAG, "setting things up.");
		
		enterGameBtn.setEnabled(false);
		
		enterGameTxt.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
           	 if (s.length() == 4) {
	                enterGameBtn.setEnabled(true);
           	 }
           	 else {
           		 enterGameBtn.setEnabled(false);
           	 }
            } 
			
		});
		
		//get name of player
		playerName = "";
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			playerName = (String)extras.get("name");
			System.out.println(playerName);
		}
		
		enterGameBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Don't keep adding people!
				//enterGameBtn.setOnClickListener(null);
				enterGameBtn.setEnabled(false);
				enterGameBtnOnClick(v);
			}
		});
	}

	private void enterGameBtnOnClick(View v){
		i = new Intent(this, Seeker.class);
		
		/* Verify that entered code exists in database */
		

		EditText text = (EditText) enterGameTxt;
		inputCode = text.getText().toString();
		i.putExtra("gameCode", inputCode);
		
		Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
		
		listener = database.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot data) {
				//pesky async...
				gameExists = data.hasChild("GAME ID " + inputCode);
				if(gameExists) {
					addPlayerToDatabase();
					
					startActivity(i);	
				}
				else
				{
					System.out.println("Game doesn't exist!");
					enterGameBtn.setEnabled(true);
					//reset click listener
					/*enterGameBtn.setOnClickListener(null);
					enterGameBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// Don't keep adding people!
							enterGameBtn.setOnClickListener(null);
							enterGameBtnOnClick(v);
						}
					});*/
				}
			}
			
			@Override
			public void onCancelled(FirebaseError error) {
				System.out.println("error: " + error);
			}
		});
	}
	
	private void addPlayerToDatabase() {
		Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
		database.removeEventListener(listener);
		Firebase newPlayerRef = database.child("GAME ID " + inputCode).child("players").push();
		newPlayerRef.child("id").setValue(newPlayerRef.getName());
		i.putExtra("pin", newPlayerRef.getName());
		newPlayerRef.child("name").setValue(playerName);
		newPlayerRef.child("latitude").setValue(0);
		newPlayerRef.child("longitude").setValue(0);
		newPlayerRef.child("state").setValue("seeking");
		
		//bullshit:
		database.child("GAME ID " + inputCode).child("numbers").child("seeking").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snap) {
				int value = (Integer) snap.getValue();
				snap.getRef().setValue(value+1);
			}
			
			@Override
			public void onCancelled(FirebaseError error) {
				System.out.println("error: " + error);
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
		getMenuInflater().inflate(R.menu.join_game, menu);
		return true;
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
