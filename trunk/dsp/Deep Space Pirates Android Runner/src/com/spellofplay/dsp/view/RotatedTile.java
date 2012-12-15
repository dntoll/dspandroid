package com.spellofplay.dsp.view;

import android.graphics.Rect;

public class RotatedTile{
	
	boolean m_b[] = new boolean[4];
	
	
	public RotatedTile(boolean a_topLeft, boolean a_topRight, boolean a_bottomLeft, boolean a_bottomRight) {
		m_b[0] = a_topRight;//a_map.GetTile(a_x +1,     a_y) == a_type; //1
		m_b[1] = a_bottomRight;//a_map.GetTile(a_x +1, a_y + 1) == a_type; //2
		m_b[2] = a_bottomLeft;//a_map.GetTile(a_x,    a_y + 1) == a_type;//4
		m_b[3] = a_topLeft; //a_map.GetTile(a_x,        a_y) == a_type;//8

		int mask = 0;
	
		if (m_b[0] == true) {
			mask += 1;
		}
		if (m_b[1] == true) {
			mask += 2;
		}
		if (m_b[2] == true) {
			mask += 4;
		}
		if (m_b[3] == true) {
			mask += 8;
		}
		
		GetTileNumber(mask);
	}
	
	
	private static final int EMPTY = 0;
	private static final int FULL = 1;
	private static final int CORNER = 2;
	private static final int EDGE = 3;
	private static final int THREE = 4;
	private static final int DIAGONAL = 5;
	
	private  void GetTileNumber(int a_mask) {
		
		switch (a_mask) { //x = rotation y = tile
			case 0 : rotation = Rotation.NONE; tile = EMPTY; break; //none
			case 1 : rotation = Rotation.TWOSEVENTY; tile = CORNER; break; //lower right
			case 2 : rotation = Rotation.HUNDREDEIGHTY; tile = CORNER; break; //lower left
			case 3 : rotation = Rotation.TWOSEVENTY; tile = EDGE; break; //right edge
			case 4 : rotation = Rotation.NINTY; tile = CORNER; break; //lower right
			case 5 : rotation = Rotation.NONE; tile = 5; break; //diagonal
			case 6 : rotation = Rotation.HUNDREDEIGHTY; tile = EDGE; break;  
			case 7 : rotation = Rotation.HUNDREDEIGHTY; tile = THREE; break; 
			case 8 : rotation = Rotation.NONE; tile = CORNER; break; //upper right
			case 9 : rotation = Rotation.NONE; tile = EDGE; break; 
			case 10 : rotation = Rotation.NINTY; tile = DIAGONAL; break;  //diagonal
			case 11 : rotation = Rotation.TWOSEVENTY; tile = THREE; break;
			case 12 : rotation = Rotation.NINTY; tile = EDGE; break;
			case 13 : rotation = Rotation.NONE; tile = THREE; break;
			case 14 : rotation = Rotation.NINTY; tile = THREE; break;
			case 15 : rotation = Rotation.NONE; tile = FULL; break; //all 
		}

		
	}
	public Rect getSrcRect(int variation, int a_tileScale) {
		int shrink = 1;
		Rect src = new Rect(tile * a_tileScale + shrink, 			   variation * a_tileScale + shrink, 
							tile * a_tileScale + a_tileScale - shrink, a_tileScale + variation *a_tileScale - shrink);
		return src;
	}

	public boolean isEmpty() {
		return tile != 0;
	}
	
	int tile;
	public Rotation rotation;
	
}