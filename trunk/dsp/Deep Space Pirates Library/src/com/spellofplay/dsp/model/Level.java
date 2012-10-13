package com.spellofplay.dsp.model;

import java.util.Random;

public class Level {

	public static final int Height = 24;
	public static final int Width = 16;
	
	public TileType GetTile(int a_x, int a_y) {
		
		Random r = new Random(a_x + a_y * Width);
		if (r.nextInt() % 5 == 0)
			return TileType.TileWall;
		//walls around the level
		if (a_x > 0 && a_x < Width-1 && a_y > 0 && a_y < Height)
			return TileType.TileEmpty;
		else
			return TileType.TileWall;
		
	}

	public void loadLevel(int a_level) {
		
	}

	public ModelPosition getStartLocation(int i) {
		return new ModelPosition(1,1);
	}
	
	public Enemy getEnemy(int i) {
		return new Enemy(new ModelPosition(i%10, Height-i/5));
	}

	public boolean canMove(ModelPosition a_to) {
		
		if (GetTile(a_to.m_x, a_to.m_y) == TileType.TileEmpty) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isClear(ModelPosition a_end) {
		if (GetTile(a_end.m_x, a_end.m_y) == TileType.TileEmpty) {
			return true;
		}
		
		return false;
	}

	
	

}
