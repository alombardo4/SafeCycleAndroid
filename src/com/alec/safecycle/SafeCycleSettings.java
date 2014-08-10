package com.alec.safecycle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.alec.safecycle.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SafeCycleSettings extends Activity {
	private final String fileName = "Settings.txt";
	private VolumeSetup setup = new VolumeSetup();
	private String units;
	
	Button changeButton, resetButton;
	SeekBar s0, s1, s2, s3, s4, s5, s6;
	TextView t0, t1, t2, t3, t4, t5, t6, mph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_cycle_settings);
		
		getActionBar().setTitle("Settings");
		
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
				units = "MPH";
				fOut = openFileOutput(fileName, MODE_PRIVATE);
				SettingsFileHandler set = new SettingsFileHandler();
				set.writeSettings(fOut, units, setup);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		changeButton = (Button) findViewById(R.id.change_button);
		resetButton = (Button) findViewById(R.id.reset_settings);
		s0 = (SeekBar) findViewById(R.id.seek_s0);
		s1 = (SeekBar) findViewById(R.id.seek_s1);
		s2 = (SeekBar) findViewById(R.id.seek_s2);
		s3 = (SeekBar) findViewById(R.id.seek_s3);
		s4 = (SeekBar) findViewById(R.id.seek_s4);
		s5 = (SeekBar) findViewById(R.id.seek_s5);
		s6 = (SeekBar) findViewById(R.id.seek_s6);
		t0 = (TextView) findViewById(R.id.s0_text);
		t1 = (TextView) findViewById(R.id.s1_text);
		t2 = (TextView) findViewById(R.id.s2_text);
		t3 = (TextView) findViewById(R.id.s3_text);
		t4 = (TextView) findViewById(R.id.s4_text);
		t5 = (TextView) findViewById(R.id.s5_text);
		t6 = (TextView) findViewById(R.id.s6_text);
		mph = (TextView) findViewById(R.id.selected_units);

		changeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(units.equals("MPH")) {
					units = "KPH";
					mph.setText(R.string.kilometers);
					t0.setText("0-8");
					t1.setText("8-16");
					t2.setText("16-24");
					t3.setText("24-32");
					t4.setText("32-40");
					t5.setText("40-48");
					t6.setText("48+");

				}
				else {
					units = "MPH";

					mph.setText(R.string.miles);
					t0.setText("0-5");
					t1.setText("5-10");
					t2.setText("10-15");
					t3.setText("15-20");
					t4.setText("20-25");
					t5.setText("25-30");
					t6.setText("30+");
				}
			}
		});
		
		resetButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				showDialog(0);
			}
		});
		
		s0.setProgress(setup.getS0());
		s1.setProgress(setup.getS1());
		s2.setProgress(setup.getS2());
		s3.setProgress(setup.getS3());
		s4.setProgress(setup.getS4());
		s5.setProgress(setup.getS5());
		s6.setProgress(setup.getS6());
		
		if (units.equals("MPH")) {
			mph.setText(R.string.miles);
			t0.setText("0-5");
			t1.setText("5-10");
			t2.setText("10-15");
			t3.setText("15-20");
			t4.setText("20-25");
			t5.setText("25-30");
			t6.setText("30+");

		}
		else {
			mph.setText(R.string.kilometers);
			t0.setText("0-8");
			t1.setText("8-16");
			t2.setText("16-24");
			t3.setText("24-32");
			t4.setText("32-40");
			t5.setText("40-48");
			t6.setText("48+");
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		CreateMenu(menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuChoice(item);
	}
	
	private void CreateMenu(Menu menu) {
		MenuItem save = menu.add(0,0,0, "Save");
		save.setIcon(R.drawable.ic_save);
		save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}
	private boolean MenuChoice(MenuItem item) {
		switch (item.getItemId()){
		case 0:
		this.onBackPressed();
		return true;
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		FileOutputStream fOut;
		try {
			fOut = openFileOutput(fileName, MODE_PRIVATE);
			SettingsFileHandler set = new SettingsFileHandler();
			setup.setS0(s0.getProgress());
			setup.setS1(s1.getProgress());
			setup.setS2(s2.getProgress());
			setup.setS3(s3.getProgress());
			setup.setS4(s4.getProgress());
			setup.setS5(s5.getProgress());
			setup.setS6(s6.getProgress());
			set.writeSettings(fOut, units, setup);
		} catch (FileNotFoundException e) {
		}
		Intent i = new Intent(this, SafeCycle.class);
		Toast.makeText(getBaseContext(), "Settings Saved!", Toast.LENGTH_SHORT).show();
		
		startActivity(i);
		finish();
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_reset_settings);
			builder.setTitle("Reset Settings?");
			builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
							
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					getBaseContext().deleteFile(fileName);
					Intent i = new Intent(getBaseContext(), SafeCycle.class);
					startActivity(i);
					finish();
					
				}
			});
			builder.setNegativeButton("Cancel", null);
			return builder.create();
		}
		return null;
	}

}
