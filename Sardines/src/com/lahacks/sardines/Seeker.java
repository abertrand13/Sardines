package com.lahacks.sardines;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.*;
import com.lahacks.sardines.Hider.NavigationHiderFragment;

public class Seeker extends FragmentActivity implements ActionBar.TabListener{

	private final static String LOG_TAG = "Seeker";
	private static String gameCode;
	private static String pin;
	
	ValueEventListener listener;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seeker);


		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		//get the game code we're dealing with
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gameCode = (String) extras.get("gameCode");
			pin = (String) extras.get("pin");
		}
		
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seeker, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f;
			
			Log.v(LOG_TAG, "Position: " + position);
			
			switch(position){
			case 0:
				f = new NavigationFragment();
				break;
			case 1:
				f = new StreamFragment();
				break;
			default:
				f = new DummySectionFragment();
				break;
			}
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			f.setArguments(args);
			return f;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_seeker_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	public static class NavigationFragment extends Fragment implements SensorEventListener{

		CompassView compass;
		

		LocationManager locationManager;
		SensorManager sensorManager;
		
		Location currentLocation = new Location("");
		Location targetLocation = new Location("");
		

		private float[] mMagneticValues;
		private float[] mAccelerometerValues;

		private float mAzimuth = 0;
		private float mPitch = 0;
		private float mRoll = 0;
		
		private TextView notification;
		
		private TextView hiding;
		private TextView seeking;
		
		public NavigationFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_seeker_navigation,
					container, false);
			compass = (CompassView) rootView.findViewById(R.id.compassView1);
			
			notification = (TextView) rootView.findViewById(R.id.latestNotification);
			
			hiding = (TextView) rootView.findViewById(R.id.numHiders);
			seeking = (TextView) rootView.findViewById(R.id.numSeekers);
			
			// GPS 
			// Acquire a reference to the system Location Manager
			locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			
			// COMPASS
			sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
			
			return rootView;
		}
		
		@Override
		public void onResume(){
			super.onResume();
			// Register the listener with the Location Manager to receive location updates
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			// Register for compass updates
			if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
				sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_GAME);
			} else {
				// Failure! No magnetometer.
				Log.e(LOG_TAG, "No magnetomter found...");
			}
			
			// Connect to firebase
			//set up database reference
			Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
			Firebase GameRef = database.child("GAME ID " + gameCode);
			Firebase hideOutLatitude = GameRef.child("hideout").child("latitude");
			Firebase hideOutLongitude = GameRef.child("hideout").child("longitude");
			
			
			
			//async calls to get hideout's location
			hideOutLatitude.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snap) {
					//fuckery.
					targetLocation.setLatitude(Double.parseDouble(snap.getValue().toString()));
					updateHideLocation();
				}
				
				@Override
				public void onCancelled(FirebaseError error) {
					System.out.println("error:" + error);
				}
			});
			
			hideOutLongitude.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snap) {
					//more fuckery.
					targetLocation.setLongitude(Double.parseDouble(snap.getValue().toString()));
					updateHideLocation();
				}
				
				@Override
				public void onCancelled(FirebaseError error) {
					System.out.println("error:" + error);
				}
			});
			
			GameRef.child("players").child(pin).child("state").addValueEventListener(new ValueEventListener() {
				
				@Override
				public void onDataChange(DataSnapshot arg0) {
					if(arg0.getValue().equals("hiding")){
						Intent i = new Intent(getActivity(), HidingFinish.class);
						startActivity(i);
					}
				}
				
				@Override
				public void onCancelled(FirebaseError arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			GameRef.child("notifications").addChildEventListener(new ChildEventListener() {
				
				@Override
				public void onChildRemoved(DataSnapshot arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChildMoved(DataSnapshot arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChildChanged(DataSnapshot arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChildAdded(DataSnapshot arg0, String arg1) {
					notification.setText("Latest Notification\n"+arg0.getValue());
				}
				
				@Override
				public void onCancelled(FirebaseError arg0) {
					// Abort
				}
			});
			
			GameRef.child("numbers").child("hiding").addValueEventListener(new ValueEventListener() {
				
				@Override
				public void onDataChange(DataSnapshot arg0) {
					hiding.setText(arg0.getValue().toString());
				}
				
				@Override
				public void onCancelled(FirebaseError arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			GameRef.child("numbers").child("seeking").addValueEventListener(new ValueEventListener() {
				
				@Override
				public void onDataChange(DataSnapshot arg0) {
					seeking.setText(arg0.getValue().toString());
				}
				
				@Override
				public void onCancelled(FirebaseError arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		}
		
		@Override 
		public void onPause(){
			super.onPause();			
			
			
		}
		
		@Override
		public void onDestroy(){
			super.onDestroy();
			locationManager.removeUpdates(locationListener);
		}
		
		private void updateHideLocation(){
			double angle = currentLocation.bearingTo(targetLocation);
			double dist = currentLocation.distanceTo(targetLocation);
			int deg = (int)(3000.0*(1.0/dist));
			angle = (int) (angle - (mAzimuth*(180.0/Math.PI)));
			if(deg < 20) deg = 20;
			if(deg > 180) deg = 180;
			Log.d(LOG_TAG, "TARGET] Angle: "+angle + " | Range: "+deg + " | Azimuth: "+ (mAzimuth*(180.0/Math.PI)));
			
			angle = angle - 90;
			if(angle < 0) angle += 360;
			if(angle > 360) angle -= 360;
			
			compass.setAngleTarget(angle, deg);
		
		}
		
		/*
		 * COMPASS
		 */
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		
		@Override
		public void onSensorChanged(SensorEvent event) {
			synchronized (NavigationFragment.this) {
				switch (event.sensor.getType()) {
				case Sensor.TYPE_MAGNETIC_FIELD:
					mMagneticValues = event.values.clone();
					break;
				case Sensor.TYPE_ACCELEROMETER:
					mAccelerometerValues = event.values.clone();
					break;
				}

				if (mMagneticValues != null && mAccelerometerValues != null) {
					float[] R = new float[16];
					SensorManager.getRotationMatrix(R, null,
							mAccelerometerValues, mMagneticValues);
					float[] orientation = new float[3];
					SensorManager.getOrientation(R, orientation);
					mAzimuth = orientation[0];
					mPitch = orientation[1];
					mRoll = orientation[2];
					//Log.d(LOG_TAG, "Azimuth: " + (mAzimuth*(180.0/Math.PI)));
					updateHideLocation();
				}
			}

		}
		
		/*
		 * GPS
		 */

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		      locationUpdate(location);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};
		  
		private void locationUpdate(Location l){
			System.out.println("getting location...");
			Log.v(LOG_TAG, "New Location: " + l); // TODO
			//System.out.println(l);
			currentLocation.setLatitude(l.getLatitude());
			currentLocation.setLongitude(l.getLongitude());

			// update to database
			Firebase database = new Firebase(
					"https://intense-fire-7136.firebaseio.com/");
			Firebase gameRef = database.child("GAME ID " + gameCode);
			Firebase playerRef = gameRef.child("players").child(pin);
			playerRef.child("latitude").setValue(currentLocation.getLatitude());
			playerRef.child("longitude").setValue(currentLocation.getLongitude());
			
			
		}
		
		
	}
	
	public static class StreamFragment extends Fragment {

		ArrayList<String> notifications = new ArrayList<String>();
		ListView listView;
		
		public StreamFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			// Update view with notifications
			View rootView = inflater.inflate(R.layout.fragment_seeker_stream,
					container, false);

			// Access database and pull most recent notifications
			Firebase database = new Firebase("https://intense-fire-7136.firebaseio.com/");
			Firebase GameRef = database.child("GAME ID " + gameCode);
			
			listView = (ListView) rootView.findViewById(R.id.notificationsList);
			
			GameRef.child("notifications").addChildEventListener(new ChildEventListener() {
				
				@Override
				public void onChildRemoved(DataSnapshot arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChildMoved(DataSnapshot arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChildChanged(DataSnapshot arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChildAdded(DataSnapshot arg0, String arg1) {
					notifications.add(arg0.getValue().toString());
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getActivity(),
							android.R.layout.simple_list_item_1,
							android.R.id.text1, notifications);

					listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

					// Assign adapter to ListView
					listView.setAdapter(adapter);
				}
				
				@Override
				public void onCancelled(FirebaseError arg0) {
				}
			});
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this.getActivity(),
					android.R.layout.simple_list_item_1,
					android.R.id.text1, new String[]{"No notifications yet."});

			// Assign adapter to ListView
			listView.setAdapter(adapter);
			
			/*
			ValueEventListener listener = GameRef.addValueEventListener(new ValueEventListener() {
			    @Override
			    public void onDataChange(DataSnapshot snapshot) {
			    	
			      DataSnapshot notifications = snapshot.child("Notifications");
			      System.out.println("Notifications found");
			      DataSnapshot newNotification = getMostRecent(notifications);
			      System.out.println("Most recent found = " + newNotification.getValue());
			      Object update = newNotification.getValue(); 
			      System.out.println("Update = " + update);
			   
			      TextView latestNotification = (TextView) getActivity().findViewById(R.id.latestNotification);
			      
			      String s = update.toString();
			      latestNotification.setText(s);
			      
			    }			    
			    private DataSnapshot getMostRecent(DataSnapshot notifications) {
			    	int smallestID = Integer.MAX_VALUE;
			    	String name = "";
			    	for(DataSnapshot child : notifications.getChildren()) {
			    		if(Integer.parseInt(child.getName()) < smallestID) {
			    			name = child.getName();
			    		}
			    	}			    	
			    	return notifications.child(name);
			    }

				@Override
				public void onCancelled(FirebaseError arg0) {
					System.err.println("Listener was cancelled");
					
				}
			});*/
			

			
			return rootView;
		}
	}

	

}
