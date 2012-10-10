package com.spellofplay.dsp.model;

public class Level {

	public static final int Height = 24;
	public static final int Width = 16;
	
	public TileType GetTile(int a_x, int a_y) {
		if (a_x > 4 && a_x < Width-5 && a_y > 5 && a_y < Height-8)
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
		
		return new Enemy();
	}

	public boolean canMove(ModelPosition a_from, ModelPosition a_to) {
		if (GetTile(a_to.m_x, a_to.m_y) == TileType.TileEmpty) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isClear(ModelPosition m_end) {
		// TODO Auto-generated method stub
		return true;
	}

	
	

}
