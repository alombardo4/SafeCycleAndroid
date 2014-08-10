package com.alec.safecycle;

public class VolumeSetup {
	private int[] s;

	public VolumeSetup(int s0, int s1, int s2, int s3, int s4, int s5, int s6) {
		super();
		s = new int[7];
		
		s[0] = s0;
		s[1] = s1;
		s[2] = s2;
		s[3] = s3;
		s[4] = s4;
		s[5] = s5;
		s[6] = s6;
	}
	
	public VolumeSetup () {
		this(10, 20, 30, 35, 45, 50, 60);
	}

	public int getS0() {
		return s[0];
	}

	public void setS0(int s0) {
		this.s[0] = s0;
	}

	public int getS1() {
		return s[1];
	}

	public void setS1(int s1) {
		this.s[1] = s1;
	}

	public int getS2() {
		return s[2];
	}

	public void setS2(int s2) {
		this.s[2] = s2;
	}

	public int getS3() {
		return s[3];
	}

	public void setS3(int s3) {
		this.s[3] = s3;
	}

	public int getS4() {
		return s[4];
	}

	public void setS4(int s4) {
		this.s[4] = s4;
	}

	public int getS5() {
		return s[5];
	}

	public void setS5(int s5) {
		this.s[5] = s5;
	}

	public int getS6() {
		return s[6];
	}

	public void setS6(int s6) {
		this.s[6] = s6;
	}
	
	public void setItemNumber(int num, int s) {
		this.s[num] = s;
	}
	public String toString() {
		String str = "";
		
		for (int i = 0; i < s.length; i++) {
			str += Integer.valueOf(s[i]).toString() + "\n";
		}
		
		
		return str;
	}
}
