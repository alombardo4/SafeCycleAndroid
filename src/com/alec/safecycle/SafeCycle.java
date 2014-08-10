package com.alec.safecycle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import com.alec.safecycle.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.ads.*;

public class SafeCycle extends Activity {
	private final String fileName = "Settings.txt";
	private VolumeSetup setup = new VolumeSetup();
	private String units = "MPH";
	private boolean isStarted = false;
	private LocationManager lm;
	private MyLocationListener ll;
	private AudioManager audioManager;
	private final int GPSUPDATETIME = 1000;
	private Location oldLocation;
	private double distance;
	private boolean firstLoc = true;
	private boolean isPaused = false;
	
	Button startButton;
	TextView speed;
	TextView unitsText;
	TextView distanceText;
	TextView currentSpeedText;
	MenuItem save;
	TextView distanceUnitsText;
	TextView currentDistanceText;
	Button pauseButton;
	Button stopButton;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_cycle);
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

		
		try {
			FileInputStream fIn = openFileInput(fileName);
			SettingsFileHandler set = new SettingsFileHandler();
			set.readItems(fIn);
			units = set.getUnits();
			setup = set.getSetup();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			FileOutputStream fOut;
			try {
				fOut = openFileOutput(fileName, MODE_PRIVATE);
				SettingsFileHandler set = new SettingsFileHandler();
				set.writeSettings(fOut, units, setup);
			} catch (FileNotFoundException e) {
			}

		}
		
		//create the adView
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice("0443585F2697FDC6314C5B42E7F23EDF");
		adView.loadAd(adRequest);
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();
		
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showDialog(0);
		}

		startButton = (Button) findViewById(R.id.start_button);
		speed = (TextView) findViewById(R.id.speed);
		unitsText = (TextView) findViewById(R.id.main_units);
		currentSpeedText = (TextView) findViewById(R.id.current_speed_text);
		distanceText = (TextView) findViewById(R.id.distance_text);
		distanceUnitsText = (TextView) findViewById(R.id.distance_units);
		currentDistanceText = (TextView) findViewById(R.id.distance);
		pauseButton = (Button) findViewById(R.id.pause_button);
		stopButton = (Button) findViewById(R.id.stop_button);
		
		currentDistanceText.setVisibility(View.INVISIBLE);
		distanceText.setVisibility(View.INVISIBLE);
		distanceUnitsText.setVisibility(View.INVISIBLE);
		currentSpeedText.setVisibility(View.INVISIBLE);
		unitsText.setVisibility(View.INVISIBLE);
		pauseButton.setVisibility(View.INVISIBLE);
		stopButton.setVisibility(View.INVISIBLE);
		
		
		if (units.equals("MPH")) {
			unitsText.setText(R.string.mph);
			distanceUnitsText.setText(R.string.miles);
		}
		else {
			unitsText.setText(R.string.kph);
			distanceUnitsText.setText(R.string.kilometers);
		}

		if(isStarted) {
			startButton.setText(R.string.stop_button);
		}
		else {
			startButton.setText(R.string.start_button);
			speed.setText("Press Start");
		}
		
		startButton.setTextColor(Color.WHITE);
		pauseButton.setTextColor(Color.WHITE);
		stopButton.setTextColor(Color.WHITE);
		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isStarted) {
					isStarted = false;
					startButton.setVisibility(View.VISIBLE);
					stopButton.setVisibility(View.INVISIBLE);
					pauseButton.setVisibility(View.INVISIBLE);
					save.setVisible(true);
					startButton.setText(R.string.start_button);
					lm.removeUpdates((LocationListener) ll);
					speed.setText("Press Start");
					currentSpeedText.setVisibility(View.INVISIBLE);
					unitsText.setVisibility(View.INVISIBLE);
					currentDistanceText.setVisibility(View.INVISIBLE);
					distanceText.setVisibility(View.INVISIBLE);
					distanceUnitsText.setVisibility(View.INVISIBLE);
					firstLoc = true;
				}
				else {
					startButton.setVisibility(View.INVISIBLE);
					pauseButton.setVisibility(View.VISIBLE);
					stopButton.setVisibility(View.VISIBLE);
					pauseButton.setText(R.string.pause);
					isStarted = true;
					save.setVisible(false);
					startButton.setText(R.string.stop_button);
					speed.setText("No GPS");
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSUPDATETIME, 2, (LocationListener)ll);
					currentSpeedText.setVisibility(View.VISIBLE);
					unitsText.setVisibility(View.VISIBLE);
					currentDistanceText.setVisibility(View.VISIBLE);
					distanceText.setVisibility(View.VISIBLE);
					distanceUnitsText.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
		
		stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isStarted = false;
				startButton.setVisibility(View.VISIBLE);
				stopButton.setVisibility(View.INVISIBLE);
				pauseButton.setVisibility(View.INVISIBLE);
				save.setVisible(true);
				startButton.setText(R.string.start_button);
				lm.removeUpdates((LocationListener) ll);
				speed.setText("Press Start");
				currentSpeedText.setVisibility(View.INVISIBLE);
				unitsText.setVisibility(View.INVISIBLE);
				currentDistanceText.setVisibility(View.INVISIBLE);
				distanceText.setVisibility(View.INVISIBLE);
				distanceUnitsText.setVisibility(View.INVISIBLE);
				firstLoc = true;				
			}
		});
		
		pauseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPaused) {
					isPaused = false;
					pauseButton.setText(R.string.pause);
					speed.setText("No GPS");
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSUPDATETIME, 2, (LocationListener)ll);
				}
				else {
					isPaused = true;
					pauseButton.setText(R.string.resume);
					speed.setText("Paused");
					lm.removeUpdates(ll);
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(!isStarted) {
			CreateMenu(menu);
		}
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChoice(item);
	}
	
	private void CreateMenu(Menu menu) {
		save = menu.add(0,0,0, "Settings");
		save.setIcon(R.drawable.ic_settings);
		save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}
	
	
	//when button for add folder is pressed
	private boolean MenuChoice(MenuItem item) {
		switch (item.getItemId()){
		case R.id.action_settings:
			Intent i = new Intent(this, SafeCycleSettings.class);
			startActivity(i);
			finish();
			return true;
			
		case 0:
			Intent i2 = new Intent(this, SafeCycleSettings.class);
			startActivity(i2);
			finish();
			return true;

		}
		return false;
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_gps_dialog);
			builder.setTitle("You Must Enable GPS");
			builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
							
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(i);					
				}
			});
			builder.setNegativeButton("Cancel", null);
			return builder.create();
		}
		return null;
	}
	
	private class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				if(loc.hasSpeed()) {
					if(units.equals("MPH")) {
						double sp = (double) (loc.getSpeed()*(float)2.236936);
						DecimalFormat f = new DecimalFormat();
						f.setMaximumFractionDigits(2);
						String fm = f.format(sp);
						speed.setText(fm);
						if (firstLoc == false) {
							distance += loc.distanceTo(oldLocation);
							currentDistanceText.setText(f.format(distance*.000621371192));
						}
						else {
							distance = 0;
							firstLoc = false;
							currentDistanceText.setText("0.00");
							oldLocation = loc;
						}
						
						this.updateVolumeMiles(sp);
						
					}
					else {
						double sp = (double) (loc.getSpeed()*(float)3.6);
						DecimalFormat f = new DecimalFormat();
						f.setMaximumFractionDigits(2);
						String fm = f.format(sp);
						speed.setText(fm);
						
						if (firstLoc == false) {
							distance += loc.distanceTo(oldLocation);
							currentDistanceText.setText(f.format(distance*.001));
						}
						else {
							distance = 0;
							firstLoc = false;
							currentDistanceText.setText("0.00");
							oldLocation = loc;
						}
						

						
						this.updateVolumeKilometers(sp);
						
					}
				}
				else {
					speed.setText(R.string.speed);
				}
				oldLocation = loc;
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
		}
		

		
		private void updateVolumeMiles(double newSpeed) {
			int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int newVolume = 0;
			
			if (newSpeed > 30) {
				newVolume = max*setup.getS6()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 25 && newSpeed <= 30) {
				newVolume = max*setup.getS5()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 20 && newSpeed <= 25) {
				newVolume = max*setup.getS4()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 15 && newSpeed <=20) {
				newVolume = max*setup.getS3()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 10 && newSpeed <=15) {
				newVolume = max*setup.getS2()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 5 && newSpeed <=10) {
				newVolume = max*setup.getS1()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if (newSpeed >=0 && newSpeed <=5) {
				newVolume = max*setup.getS0()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}

		}
		
		private void updateVolumeKilometers(double newSpeed) {
			int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int newVolume = 0;
			if (newSpeed > 48) {
				newVolume = max*setup.getS6()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 40 && newSpeed <= 48) {
				newVolume = max*setup.getS5()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 32 && newSpeed <= 40) {
				newVolume = max*setup.getS4()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 24 && newSpeed <=32) {
				newVolume = max*setup.getS3()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 16 && newSpeed <=24) {
				newVolume = max*setup.getS2()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if(newSpeed > 8 && newSpeed <=16) {
				newVolume = max*setup.getS1()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
			else if (newSpeed >=0 && newSpeed <=8) {
				newVolume = max*setup.getS0()/100;
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
			}
		}		
	}
}



