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
import android.widget.ProgressBar;

import com.firebase.client.*;

public class JoinGame extends Activity {

	Button enterGameBtn;
	
	//globabl variables are bad?
	boolean gameExists = false;
	String inputCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_game);
		// Show the Up button in the action bar.
		setupActionBar();

		enterGameBtn = (Button) findViewById(R.id.enterGameBtn);
		enterGameBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				enterGameBtnOnClick(v);
			}
		});
	}

	private void enterGameBtnOnClick(View v){
		Intent i = new Intent(this, Seeker.class);
		
		
		
		

		/* Verify that entered code exists in database */
		

		EditText text = (EditText) v;
		//int inputCode = Integer.parseInt(text.getText().toString());
		inputCode = text.getText().toString();
		
		/*Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/testGameCode");
		database.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot data) {
				gameExists = data.hasChild("GAME ID " + (String)inputCode);
				System.out.println(gameExists);
			}
			
			@Override
			public void onCancelled(FirebaseError error) {
				System.out.println("error:" + error);
			}
		});
		if(gameExists) {
			startActivity(i);	
		}
		else
		{
			System.out.println("Game doesn't exist!");
		}*/

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
