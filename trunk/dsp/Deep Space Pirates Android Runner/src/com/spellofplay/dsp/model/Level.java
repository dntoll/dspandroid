package com.spellofplay.dsp.model;

public class Level {

	public static final int Height = 24;
	public static final int Width = 24;
	
	public TileType m_tiles[][] = new TileType[Width][Height];
	public ModelPosition m_playerStartPositions[]= new ModelPosition[Game.MAX_SOLDIERS];
	ModelPosition m_enemyPositions[]= new ModelPosition[Game.MAX_ENEMIES];
	int m_numEnemies;
	
	public Level() {
		clear();
	}
	
	public TileType GetTile(int a_x, int a_y) {
		//walls around the level
		if (a_x >= 0 && a_x < Width && a_y >= 0 && a_y < Height)
			return m_tiles[a_x][a_y];
		else
			return TileType.TileWall;
		
	}
	
	public void addEnemy(ModelPosition modelPosition) {
		if (m_numEnemies < Game.MAX_ENEMIES) 
			m_enemyPositions[m_numEnemies] = modelPosition;
		m_numEnemies++;
	}
	

	public ModelPosition getStartLocation(int i) throws LevelHasToFewSoldierPositions {
		if (m_playerStartPositions[i] != null)
			return m_playerStartPositions[i];
		throw new LevelHasToFewSoldierPositions("Level has not room for soldier number " + i);
	}
	
	public ModelPosition getEnemyStartLocation(int i) throws LevelHasToFewEnemiesException {
		if (m_enemyPositions[i] != null)
			return m_enemyPositions[i];
		
		
		throw new LevelHasToFewEnemiesException("Level has not room for enemy number " + i);
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
		if (GetTile(a_end.m_x, a_end.m_y) == TileType.TilePit) {
			return true;
		}
		if (GetTile(a_end.m_x, a_end.m_y) == TileType.TileCover) {
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
	 
	 public boolean lineOfSight(ModelPosition pos1, ModelPosition pos2) {
		Vector2 fromPos = pos1.toCenterTileVector();
		Vector2 targetPosition = pos2.toCenterTileVector();
		if ( lineOfSight(fromPos, targetPosition) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(0.3f, 0.3f), targetPosition ) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(-0.3f, 0.3f), targetPosition ) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(-0.3f, -0.3f), targetPosition ) ) {
			return true;
		}
		if ( lineOfSight(fromPos.sub(0.3f, -0.3f), targetPosition ) ) {
			return true;
		}
		return false;
	}

	private boolean lineOfSight(Vector2 a_from, Vector2 a_to) {
		Vector2 dir = a_to.sub(a_from);
        dir.normalize();

        
      /*  if (stepNodesInDirection(true, a_from, a_to, dir)) {
        	return false;
        }
        if (stepNodesInDirection(false, a_from, a_to, dir)) {
        	return false;
        }*/
        if (dir.m_x > 0.0f) {
        	
	        if (stepNodesPositiveXDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
        } else if (dir.m_x < 0.0f) {
        	if (stepNodesNegativeXDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
	        
        }

        if (dir.m_y > 0.0f) {
        	if (stepNodesPositiveYDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
        } else if (dir.m_y < 0.0f) {
        	if (stepNodesNegativeYDirection(a_from, a_to, dir) == false) {
	        	return false;
	        }
        }

        return true;
    }
	
	/*private boolean stepNodesInDirection(boolean xIsMainDirection, Vector2 from, Vector2 to, Vector2 dir) {
		float mainDirection; 
		float mainDirectionFrom;
		float mainDirectionTo;
		float theOtherDirection;
		float theOtherDirectionFrom;
		
		if (xIsMainDirection == false) {
			mainDirection = dir.m_y;
			mainDirectionFrom =from.m_y;
			mainDirectionTo = to.m_y;
			theOtherDirection = dir.m_x;
			theOtherDirectionFrom = from.m_x;
		} else {
			mainDirection = dir.m_x;
			mainDirectionFrom =from.m_x;
			mainDirectionTo = to.m_x;
			theOtherDirection = dir.m_y;
			theOtherDirectionFrom = from.m_y;
		}
		
		
		int stepDirection;
		if (mainDirection > 0.0f) {
			stepDirection = 1;
		} else if ( mainDirection < 0.0f) {
			stepDirection = -1;
		} else {
			return true;
		}
		
		
		for (int position = (int)mainDirectionFrom; position > mainDirectionTo; position += stepDirection) {
		    float u = ((float)position - theOtherDirection) / mainDirection;
		    float theOtherPosition = theOtherDirectionFrom + theOtherDirection * u;
		    
		    if (xIsMainDirection) {
		    	if (isLosBlocked(position, theOtherPosition, 0.0f, 0.01f)) {
			        return false;
			    }	
		    } else {
		    	if (isLosBlocked(position, theOtherPosition, 0.0f, 0.01f)) {
			        return false;
			    }
		    }
		}
		return true;
	}*/

	private boolean stepNodesNegativeYDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int y = (int)a_from.m_y; y > a_to.m_y; y--) {
		    float u = ((float)y - a_from.m_y) / dir.m_y;
		    float x = a_from.m_x + dir.m_x * u;
		    if (isLosBlocked(x, y, 0.0f, 0.01f)) {
		        return false;
		    }
		}
		return true;
	}

	private boolean stepNodesPositiveYDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int y = (int)a_from.m_y+1; (float)y < a_to.m_y; y++) {
		    float u = ((float)y - a_from.m_y) / dir.m_y;
		    float x = a_from.m_x + dir.m_x * u;
		    if (isLosBlocked(x, y, 0.0f, 0.01f)) {
		        return false;
		    }
		}
		return true;
	}

	private boolean stepNodesNegativeXDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int x = (int)a_from.m_x; (float)x > a_to.m_x; x--) {
		    float u = ((float)x - a_from.m_x) / dir.m_x;
		    float y = a_from.m_y + dir.m_y * u;
		    if (isLosBlocked(x, y, 0.01f, 0.0f)) {
		        return false;
		    }
		}
		return true;
	}

	private boolean stepNodesPositiveXDirection(Vector2 a_from, Vector2 a_to,
			Vector2 dir) {
		for (int x = (int)a_from.m_x+1; (float)x < a_to.m_x; x++) {
		    float u = ((float)x - a_from.m_x) / dir.m_x;
		    float y = a_from.m_y + dir.m_y * u;
		    if (isLosBlocked(x, y, 0.01f, 0.0f)) {
		        return false;
		    }

		}
		
		return true;
	}

	public boolean hasCoverFrom(ModelPosition position, Vector2 sub) {
		
		
		sub.normalize();
		
		int dx = sub.m_x > 0 ? 1 : (sub.m_x < 0 ? -1 : 0);
		int dy = sub.m_y > 0 ? 1 : (sub.m_y < 0 ? -1 : 0);
		
		if (GetTile(position.m_x + dx, position.m_y) == TileType.TileCover) {
			return true;
		}
		if (GetTile(position.m_x, position.m_y + dy) == TileType.TileCover) {
			return true;
		}
		
		return false;
	}

	public void clear() {
		for (int i= 0; i< Game.MAX_SOLDIERS; i++) {
			m_playerStartPositions[i] = null;
		}
		for (int i= 0; i< Game.MAX_SOLDIERS; i++) {
			m_enemyPositions[i] = null;
		}
		m_numEnemies = 0;
		
		for (int y = 0; y < Height; y++) {
			for (int x = 0; x < Width; x++) {
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
		
		
		
		return clearTilesX == 2 || clearTilesY == 2;
	}

	
}
