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
	
	 private boolean isBlocked(float a_x, float a_y, float a_dx, float a_dy)
     {
         if (isClear(new ModelPosition((int)a_x, (int)a_y)) == false)
         {
             return true;
         }
         if (isClear(new ModelPosition((int)((int)a_x - a_dx), (int)((int)a_y - a_dy))) == false)
         {
             return true;
         }

         return false;
     }

	public boolean lineOfSight(Vector2 a_from, Vector2 a_to) {
		Vector2 dir = a_to.sub(a_from);
        dir.normalize();

        if (dir.m_x > 0.0f) {
	        for (int x = (int)a_from.m_x+1; (float)x < a_to.m_x; x++) {
		        float u = ((float)x - a_from.m_x) / dir.m_x;
		        float y = a_from.m_y + dir.m_y * u;
                if (isBlocked(x, y, 0.01f, 0.0f)) {
                    return false;
                }
		
	        }
        } else if (dir.m_x < 0.0f) {
	        for (int x = (int)a_from.m_x; (float)x > a_to.m_x; x--) {
		        float u = ((float)x - a_from.m_x) / dir.m_x;
		        float y = a_from.m_y + dir.m_y * u;
                if (isBlocked(x, y, 0.01f, 0.0f)) {
                    return false;
                }
	        }
        }

        //y
        if (dir.m_y > 0.0f) {
	        for (int y = (int)a_from.m_y+1; (float)y < a_to.m_y; y++) {
		        float u = ((float)y - a_from.m_y) / dir.m_y;
		        float x = a_from.m_x + dir.m_x * u;
                if (isBlocked(x, y, 0.0f, 0.01f)) {
                    return false;
                }
    			
	        }
        } else if (dir.m_y < 0.0f) {
	        for (int y = (int)a_from.m_y; (float)y > a_to.m_y; y--) {
		        float u = ((float)y - a_from.m_y) / dir.m_y;
		        float x = a_from.m_x + dir.m_x * u;
                if (isBlocked(x, y, 0.0f, 0.01f)) {
                    return false;
                }
	        }
        }

        return true;
    }

	
		

	
	

}
