package com.spellofplay.dsp.model;

public class Vector2 {
	private static final float STARTING_DIRECTION_LEFT = 90.0f;
	public float x;
	public float y;
	
	public Vector2() {
		x = 0;
		y = 0;
	}
	
	public Vector2(float a_x, float a_y) {
		x = a_x;
		y = a_y;
	}

	public Vector2 sub(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}
	
	public Vector2 add(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}
	
	public Vector2 mul(float a) {
		return new Vector2(x * a, y  * a);
	}
	
	public void normalize() {
		float len = length();
		if (len > 0.0f) {
			x /= len;
			y /= len;
		}
	}

	public float length() {
		return android.util.FloatMath.sqrt(x * x + y * y);
	}
	
	@Override 
	public boolean equals(Object aThat) {
		Vector2 other = (Vector2) aThat;
		return other.x == x && other.y == y;
	}

	public float getRotation() {
		return STARTING_DIRECTION_LEFT +(float)(Math.atan2(y, x) * 180.0 / Math.PI);
	}


	

}
