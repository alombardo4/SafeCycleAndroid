package com.alec.safecycle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SettingsFileHandler {
			
	private String[] readItems;
	public void writeSettings (FileOutputStream fOut, String units, VolumeSetup setup) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(units + "\n");
			osw.write(setup.toString());
			osw.flush();
			osw.close();
			
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void readItems(FileInputStream fIn) {
		try {
			InputStreamReader isr = new InputStreamReader(fIn);
			char[] inputBuffer = new char[100];
			String s = "";
			
			int charRead;
			while((charRead = isr.read(inputBuffer)) > 0) {
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				s+= readString;
				inputBuffer = new char[100];
			}
			readItems = s.split("\n");
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String getUnits() {
		String units="";

		units = readItems[0];
		
		
		return units;
	}
	
	public VolumeSetup getSetup () {
		VolumeSetup setup = new VolumeSetup();
		
		for (int i = 1; i < readItems.length; i++) {
			setup.setItemNumber(i-1, Integer.parseInt(readItems[i]));
		}
		
		
		return setup;
	}
}
