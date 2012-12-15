package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.levelgenerator.LevelHasToFewEnemiesException;
import com.spellofplay.dsp.model.inner.levelgenerator.LevelHasToFewSoldierPositions;

public class Level {

	public TileType m_tiles[][] = new TileType[Preferences.WIDTH][Preferences.HEIGHT];
	public ModelPosition m_playerStartPositions[]= new ModelPosition[Preferences.MAX_SOLDIERS];
	private ModelPosition m_enemyPositions[]= new ModelPosition[Preferences.MAX_ENEMIES];
	private int m_numEnemies;
	
	public Level() {
		clear();
	}
	
	public TileType GetTile(int a_x, int a_y) {
		//walls around the level
		if (a_x >= 0 && a_x < Preferences.WIDTH && a_y >= 0 && a_y < Preferences.HEIGHT)
			return m_tiles[a_x][a_y];
		else
			return TileType.TileWall;
		
	}
	
	public void addEnemy(ModelPosition modelPosition) {
		if (m_numEnemies < Preferences.MAX_ENEMIES) 
			m_enemyPositions[m_numEnemies] = modelPosition;
		m_numEnemies++;
	}
	

	ModelPosition getStartLocation(int i) throws LevelHasToFewSoldierPositions {
		if (m_playerStartPositions[i] != null)
			return m_playerStartPositions[i];
		throw new LevelHasToFewSoldierPositions("Level has not room for soldier number " + i);
	}
	
	ModelPosition getEnemyStartLocation(int i) throws LevelHasToFewEnemiesException {
		if (m_enemyPositions[i] != null)
			return m_enemyPositions[i];
		
		
		throw new LevelHasToFewEnemiesException("Level has not room for enemy number " + i);
	}

	boolean canMove(ModelPosition a_to) {
		if (GetTile(a_to.x, a_to.y) == TileType.TileEmpty) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isClear(ModelPosition a_end) {
		if (GetTile(a_end.x, a_end.y) == TileType.TileEmpty) {
			return true;
		}
		if (GetTile(a_end.x, a_end.y) == TileType.TilePit) {
			return true;
		}
		if (GetTile(a_end.x, a_end.y) == TileType.TileCover) {
			return true;
		}
		return false;
	}
	
	 private boolean isLosBlocked(float a_x, float a_y, float a_dx, float a_dy) {
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
	 
	 boolean lineOfSight(ModelPosition pos1, ModelPosition pos2) {
		 return _lineOfSight(pos1, pos2);// || _lineOfSight(pos2, pos1);
	 }
	 
	 private boolean _lineOfSight(ModelPosition pos1, ModelPosition pos2) {
		Vector2 fromPos = pos1.toCenterTileVector();
		Vector2 targetPosition = pos2.toCenterTileVector();
		if ( lineOfSight(fromPos, targetPosition) ) {
			return true;
		}
		/*if ( lineOfSight(fromPos.sub(0.3f, 0), targetPosition ) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(-0.3f, 0), targetPosition ) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(0, -0.3f), targetPosition ) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(0, -0.3f), targetPosition ) ) {
			return true;
		}*/
		/*if ( lineOfSight(fromPos, targetPosition.sub(0.3f, 0.3f) ) ) {
			return true;
		}
		if ( lineOfSight(fromPos, targetPosition.sub(-0.3f, 0.3f) ) ) {
			return true;
		}
		if ( lineOfSight(fromPos, targetPosition.sub(-0.3f, -0.3f) ) ) {
			return true;
		}
		if ( lineOfSight(fromPos, targetPosition.sub(0.3f, -0.3f) ) ) {
			return true;
		}*/
		return false;
	}

	private boolean lineOfSight(Vector2 a_from, Vector2 a_to) {
		Vector2 dir = a_to.sub(a_from);
        dir.normalize();

        
        if (dir.x > 0.0f) {
        	
	        if (stepNodesPositiveXDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
        } else if (dir.x < 0.0f) {
        	if (stepNodesNegativeXDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
	        
        }

        if (dir.y > 0.0f) {
        	if (stepNodesPositiveYDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
        } else if (dir.y < 0.0f) {
        	if (stepNodesNegativeYDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
        }

        return true;
    }
	

	private boolean stepNodesNegativeYDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int y = (int)a_from.y; y > a_to.y; y--) {
		    float u = ((float)y - a_from.y) / dir.y;
		    float x = a_from.x + dir.x * u;
		    if (isLosBlocked(x, y, 0.0f, 0.01f)) {
		        return false;
		    }
		}
		return true;
	}

	private boolean stepNodesPositiveYDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int y = (int)a_from.y+1; (float)y < a_to.y; y++) {
		    float u = ((float)y - a_from.y) / dir.y;
		    float x = a_from.x + dir.x * u;
		    if (isLosBlocked(x, y, 0.0f, 0.01f)) {
		        return false;
		    }
		}
		return true;
	}

	private boolean stepNodesNegativeXDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int x = (int)a_from.x; (float)x > a_to.x; x--) {
		    float u = ((float)x - a_from.x) / dir.x;
		    float y = a_from.y + dir.y * u;
		    if (isLosBlocked(x, y, 0.01f, 0.0f)) {
		        return false;
		    }
		}
		return true;
	}

	private boolean stepNodesPositiveXDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int x = (int)a_from.x+1; (float)x < a_to.x; x++) {
		    float u = ((float)x - a_from.x) / dir.x;
		    float y = a_from.y + dir.y * u;
		    if (isLosBlocked(x, y, 0.01f, 0.0f)) {
		        return false;
		    }

		}
		
		return true;
	}

	boolean hasCoverFrom(ModelPosition position, Vector2 sub) {
		
		
		sub.normalize();
		
		int dx = sub.x > 0 ? 1 : (sub.x < 0 ? -1 : 0);
		int dy = sub.y > 0 ? 1 : (sub.y < 0 ? -1 : 0);
		
		if (GetTile(position.x + dx, position.y) == TileType.TileCover) {
			return true;
		}
		if (GetTile(position.x, position.y + dy) == TileType.TileCover) {
			return true;
		}
		
		return false;
	}

	public void clear() {
		for (int i= 0; i< Preferences.MAX_SOLDIERS; i++) {
			m_playerStartPositions[i] = null;
		}
		for (int i= 0; i< Preferences.MAX_ENEMIES; i++) {
			m_enemyPositions[i] = null;
		}
		m_numEnemies = 0;
		
		for (int y = 0; y < Preferences.HEIGHT; y++) {
			for (int x = 0; x < Preferences.WIDTH; x++) {
				m_tiles[x][y] = TileType.TileWall;
			}
		}
	}

	public boolean isWallAndHasTwoClearSides(int x, int y) {
		if (GetTile(x, y) != TileType.TileWall) {
			return false;
		}
		int clearTilesX = 0;
		int clearTilesY = 0;
		if (GetTile(x+1, y) == TileType.TileEmpty) {
			clearTilesX++;
		}
		if (GetTile(x-1, y) == TileType.TileEmpty) {
			clearTilesX++;
		}
		if (GetTile(x, y+1) == TileType.TileEmpty) {
			clearTilesY++;
		}
		if (GetTile(x, y-1) == TileType.TileEmpty) {
			clearTilesY++;
		}
		
		
		
		return (clearTilesX == 2 && clearTilesY == 0)|| (clearTilesX == 0 && clearTilesY == 2);
	}

	boolean hasDoorCloseToIt(ModelPosition position) {
		int x = position.x;
		int y = position.y;
		if (GetTile(x+1, y) == TileType.TileDoor) {
			return true;
		}
		if (GetTile(x-1, y) == TileType.TileDoor) {
			return true;
		}
		if (GetTile(x, y+1) == TileType.TileDoor) {
			return true;
		}
		if (GetTile(x, y-1) == TileType.TileDoor) {
			return true;
		}
		return false;
	}

	void open(ModelPosition position) {
		int x = position.x;
		int y = position.y;
		if (GetTile(x+1, y) == TileType.TileDoor) {
			openAt(x+1,y);
		}
		if (GetTile(x-1, y) == TileType.TileDoor) {
			openAt(x-1,y);
		}
		if (GetTile(x, y+1) == TileType.TileDoor) {
			openAt(x,y+1);
		}
		if (GetTile(x, y-1) == TileType.TileDoor) {
			openAt(x,y-1);
		}
	}

	boolean isDoor(ModelPosition pos) {
		return  GetTile(pos.x, pos.y) == TileType.TileDoor;
	}

	void openAt(int x, int y) {
		m_tiles[x][y] = TileType.TileEmpty;
	}

	public void LoadFromString(String level) {
		
		
		if (level != "") {
			int index = 0;
			for (int x = 0; x < Preferences.WIDTH; x++) {
				for (int y = 0; y < Preferences.HEIGHT; y++) {
					 int enumIndex = level.charAt(index)-'0';
					 m_tiles[x][y] = TileType.values()[enumIndex];
					 index++;
				}
			}
		}
		
	}

	public String SaveToString() {
		StringBuilder strbuff = new StringBuilder();
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				int ordinal = m_tiles[x][y].ordinal();
				strbuff.append(ordinal);
			}
		}
		return strbuff.toString();
		
	}
}
