package com.lahacks.sardines;

import android.widget.EditText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.text.TextWatcher;
import android.text.Editable;

public class Splash extends Activity {

	EditText nameTxt;
	Button startGameBtn;
	Button joinGameBtn;
	EditText et;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        nameTxt = (EditText) findViewById(R.id.nameEntry);
        startGameBtn = (Button) findViewById(R.id.startGameBtn);
        joinGameBtn = (Button) findViewById(R.id.joinGameBtn);
        
        startGameBtn.setEnabled(false);
        joinGameBtn.setEnabled(false);
        
        et = (EditText) findViewById(R.id.nameEntry);
        et.addTextChangedListener(new TextWatcher() {
        	 @Override
             public void afterTextChanged(Editable s) {
                 // TODO Auto-generated method stub

             }

             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 // TODO Auto-generated method stub

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

                 startGameBtn = (Button) findViewById(R.id.startGameBtn);
                 joinGameBtn = (Button) findViewById(R.id.joinGameBtn);
                 
                 startGameBtn.setEnabled(true);
                 joinGameBtn.setEnabled(true);

             } 
        });
        
        joinGameBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				joinGameOnClick(v);
			}
		});
        
        startGameBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGameOnClick(v);
			}
		});
        
    }
    
    public void joinGameOnClick(View v){
    	Intent i = new Intent(this, JoinGame.class);
    	String name = nameTxt.getText().toString();
    	i.putExtra("name", name);
    	startActivity(i);
    }
    
    public void startGameOnClick(View v){
    	Intent i = new Intent(this, StartGame.class);
    	String name = nameTxt.getText().toString();
    	i.putExtra("name", name);
    	startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
    
}
