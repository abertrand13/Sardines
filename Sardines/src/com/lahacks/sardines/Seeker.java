package com.lahacks.sardines;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.TextView;

import com.firebase.client.*;
import com.lahacks.sardines.Hider.NavigationHiderFragment;

public class Seeker extends FragmentActivity implements ActionBar.TabListener{

	private final static String LOG_TAG = "Seeker";
	private static String gameCode;
	
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
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

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
		if(extras != null) {
			gameCode = (String)extras.get("gameCode");
			System.out.println(gameCode);
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
		

		private float[] mMagneticValues;
		private float[] mAccelerometerValues;

		private float mAzimuth = 0;
		private float mPitch = 0;
		private float mRoll = 0;
		
		public NavigationFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_seeker_navigation,
					container, false);
			compass = (CompassView) rootView.findViewById(R.id.compassView1);
			
			// GPS 
			// Acquire a reference to the system Location Manager
			locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			
			// COMPASS
			sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
			
			return rootView;
		}
		
		double latitude;
		double longitude;
		
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
					latitude = Double.parseDouble(snap.getValue().toString());
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
					longitude = Double.parseDouble(snap.getValue().toString());
					updateHideLocation();
				}
				
				@Override
				public void onCancelled(FirebaseError error) {
					System.out.println("error:" + error);
				}
			});
			
			
		}
		
		@Override 
		public void onPause(){
			super.onPause();
			locationManager.removeUpdates(locationListener);
			
			
		}
		
		private void updateHideLocation(){
			Location l = new Location("");
			l.setLatitude(latitude);
			l.setLongitude(longitude);
			double angle = currentLocation.bearingTo(l);
			double dist = currentLocation.distanceTo(l);
			int deg = (int)(3000.0*(1.0/dist));
			//deg = deg - (mAzimuth*(180.0/Math.PI));
			if(deg < 10) deg = 10;
			if(deg > 180) deg = 180;
			Log.d(LOG_TAG, "TARGET] Angle: "+angle + " | Degrees: "+deg);
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
					Log.d(LOG_TAG, "Azimuth: " + (mAzimuth*(180.0/Math.PI)));
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
			Log.d(LOG_TAG, "Location: Lat="+l.getLatitude()+" \tLng="+l.getLongitude());
			currentLocation = l;
		}
	}
	
	public static class StreamFragment extends Fragment {

		public StreamFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_seeker_stream,
					container, false);
			return rootView;
		}
	}

	

}
