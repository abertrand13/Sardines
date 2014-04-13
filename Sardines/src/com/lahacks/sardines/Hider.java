package com.lahacks.sardines;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lahacks.sardines.Seeker.DummySectionFragment;
import com.lahacks.sardines.Seeker.StreamFragment;

public class Hider extends FragmentActivity implements ActionBar.TabListener {

	public static final String LOG_TAG = "Hider";

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

	// game variables
	static String gameCode;
	static String pin;
	
	Location currentLocation = new Location("");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hider);

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

		// Set up location manager
		// locationManager =
		// (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

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

		// Set references for database interaction
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gameCode = (String) extras.get("gameCode");
			pin = (String) extras.get("pin");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hider, menu);
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

			switch (position) {
			case 1:
				f = new StreamFragment();
				break;
			case 2:
				f = new PlayersFragment();
				break;
			default:
				f = new NavigationHiderFragment();
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
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section_navigation)
						.toUpperCase(l);
			case 1:
				return getString(R.string.title_section_stream).toUpperCase(l);
			case 2:
				return getString(R.string.title_section_players).toUpperCase(l);
			}
			return null;
		}
	}

	public static class NavigationHiderFragment extends Fragment implements
			SensorEventListener {

		CompassView compass;

		LocationManager locationManager;
		SensorManager sensorManager;

		Location currentLocation = new Location("");

		ArrayList<Integer> angles;

		private float[] mMagneticValues;
		private float[] mAccelerometerValues;

		private float mAzimuth = 0;
		private float mPitch = 0;
		private float mRoll = 0;

		public NavigationHiderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_hider_navigation, container, false);
			compass = (CompassView) rootView.findViewById(R.id.compassView1);
			compass.setSeeking(false);

			angles = new ArrayList<Integer>();

			compass.setSeekerAngles(angles);

			// GPS
			// Acquire a reference to the system Location Manager
			locationManager = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);

			// COMPASS
			sensorManager = (SensorManager) getActivity().getSystemService(
					Context.SENSOR_SERVICE);

			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();
			// Register the listener with the Location Manager to receive
			// location updates
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			// Register for compass updates
			if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
						SensorManager.SENSOR_DELAY_GAME);
				sensorManager.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_GAME);
			} else {
				// Failure! No magnetometer.
				Log.e(LOG_TAG, "No magnetomter found...");
			}
			
			Firebase database = new Firebase(
					"https://intense-fire-7136.firebaseio.com/");
			Firebase gameRef = database.child("GAME ID " + gameCode);
			gameRef.child("players").addValueEventListener(new ValueEventListener(){

				@Override
				public void onCancelled(FirebaseError arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onDataChange(DataSnapshot snap) {
					angles.clear();
					for(DataSnapshot d : snap.getChildren()){
						if(d.child("id").getValue().equals(pin)) continue; // skip myself
						try{
							double lat = Double.parseDouble((d.child("latitude").getValue()).toString());
							double lng = Double.parseDouble((d.child("longitude").getValue()).toString());
							Location l = new Location("");
							l.setLatitude(lat);
							l.setLongitude(lng);
							Log.d(LOG_TAG, "SeekerLoc: Lat="+lat+" Lng="+lng);
							double bearing = currentLocation.bearingTo(l);
						angles.add((int)bearing);
						}catch(Exception e){
							Log.d(LOG_TAG, "You done fucked up, Firebase.");
						}
					}
				}
				
			});

		}

		@Override
		public void onPause() {
			super.onPause();

		}
		
		@Override
		public void onDestroy(){
			super.onDestroy();
			locationManager.removeUpdates(locationListener);
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
			synchronized (NavigationHiderFragment.this) {
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

					ArrayList<Integer> rotated = new ArrayList<Integer>();
					for (int a : angles) {
						Log.d(LOG_TAG, "a="+a);
						int r = (int) (a - 90 - (mAzimuth * (180.0 / Math.PI)));
						if (r < 0)
							r += 360;
						if (r >= 360)
							r -= 360;
						rotated.add(r);
						// Log.d(LOG_TAG, "r=" + r);
					}
					compass.setSeekerAngles(rotated);
				}
			}

		}

		/*
		 * GPS
		 */

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				locationUpdate(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		private void locationUpdate(Location l) {
			System.out.println("getting location...");
			Log.v(LOG_TAG, "New Location: " + l); // TODO
			System.out.println(l);
			currentLocation = l;

			updateHideoutLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
			
			/*
			// update to database
			Firebase database = new Firebase(
					"https://intense-fire-7136.firebaseio.com/");
			Firebase gameRef = database.child("GAME ID " + gameCode);
			Firebase playerRef = gameRef.child("players").child(pin);
			playerRef.child("latitude").setValue(currentLocation.getLatitude());
			playerRef.child("longitude").setValue(currentLocation.getLongitude());
*/
			
			/*
			// update hideout part of database if this is the original hider
			// NOM NOM NOM NOM NOM NOM NOM NOM NOM
			playerRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snap) {
					Object value = snap.getValue();
					// cast to map to check values
					String hider = (String) ((Map) value).get("hider");
					if (hider.equals("true")) {
						// too many goddamn parentheses
						Object lat = ((Map) value).get("latitude");
						double latitude = (Double) lat;
						Object longi = ((Map) value).get("longitude");
						double longitude = (Double) longi;
						// double longitude =
						// Double.parseDouble((String)((Map)value).get("longitude"));
						updateHideoutLocation(latitude, longitude);
					}
				}

				@Override
				public void onCancelled(FirebaseError error) {
					System.out.println("error: " + error);
				}
			});*/
			
			

		}

		private void updateHideoutLocation(double latitude, double longitude) {
			Firebase database = new Firebase(
					"https://intense-fire-7136.firebaseio.com/");
			Firebase gameRef = database.child("GAME ID " + gameCode);
			gameRef.child("hideout").child("latitude").setValue(latitude);
			gameRef.child("hideout").child("longitude").setValue(longitude);
		}
	}

	public static class PlayersFragment extends Fragment {

		ListView playersList;

		private final static String LOG_TAG = "PlayersFragment";

		Firebase GameRef;
		
		ArrayList<String> idsList = new ArrayList<String>();
		
		public PlayersFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_hider_players,
					container, false);

			// Connect to firebase
			// set up database reference
			Firebase database = new Firebase(
					"https://intense-fire-7136.firebaseio.com/");
			GameRef = database.child("GAME ID " + gameCode);
			

			playersList = (ListView) rootView
					.findViewById(R.id.playersListView);

			String[] names = new String[] { "Loading..."};

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this.getActivity(),
					android.R.layout.simple_list_item_checked,
					android.R.id.text1, names);

			playersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			// Assign adapter to ListView
			playersList.setAdapter(adapter);

			// ListView Item Click Listener
			playersList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.d(LOG_TAG, "#"+arg3+" was clicked: "+idsList.get((int)arg3));
					GameRef.child("players").child(idsList.get((int)arg3)).child("state").setValue("hiding");
					Firebase notifs = GameRef.child("notifications");
					Firebase notifPush = notifs.push();
					
					String name = getActivity().getSharedPreferences("splash", 0).getString("name", "Someone");
					
					notifPush.setValue(name+" has finished.");
					playersList.getChildAt((int)arg3).setEnabled(false);
					playersList.getChildAt((int)arg3).setClickable(false);
				}
			});

			return rootView;
		}
		
		@Override
		public void onResume(){
			super.onResume();
			GameRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
				
				@Override
				public void onDataChange(DataSnapshot arg0) {
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<String> ids = new ArrayList<String>();
					for(DataSnapshot ds : arg0.getChildren()){
						String name = ds.child("name").getValue().toString();
						String id = ds.child("id").getValue().toString();
						if(!ds.child("id").getValue().equals(pin)){
							names.add(name);
							ids.add(id);
						}
					}
					ArrayAdapter<String> adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, names);
					playersList.setAdapter(adapt);
					idsList = ids;
				}
				
				@Override
				public void onCancelled(FirebaseError arg0) {
					Log.e(LOG_TAG, "Firebase error");
				}
			});
		}
	}

}
