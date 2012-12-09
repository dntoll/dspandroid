package com.spellofplay.dsp.model;

public class ModelPosition {
	public int x;
	public int y;
	
	public ModelPosition() {
		x = 0;
		y = 0;
	}
	
	
	public ModelPosition(int a_x, int a_y) {
		x = a_x;
		y = a_y;
	}


	public Vector2 sub(ModelPosition other) {
		return new Vector2(x - other.x, y - other.y);
	}


		
	@Override 
	public boolean equals(Object object) {
		ModelPosition other = (ModelPosition) object;
		return other.x == x && other.y == y;
		
	}


	public Vector2 toCenterTileVector() {
		return new Vector2(x + 0.5f, y + 0.5f);
	}


	

}
