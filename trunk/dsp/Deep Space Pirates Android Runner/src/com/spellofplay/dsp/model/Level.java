package com.spellofplay.dsp.model;

public class Level {

	public static final int Height = 32;
	public static final int Width = 32;
	
	public TileType m_tiles[][] = new TileType[Width][Height];
	ModelPosition m_playerStartPositions[]= new ModelPosition[Game.MAX_SOLDIERS];
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
	

	public void loadLevel(int a_level) {
		
		LevelGenerator gen = new LevelGenerator(a_level);
		
		gen.generate(this);
		
		/*String level =
			        "XXXXXXXXXXXXXXXX" +
					"XO123XXXXXXXXXXX" + 
					"XOOOOXEOOOOOXXXX" +
					"XOOPOXOOOOOOXXXX" +
					"XOOOXXOXXXXOXXXX" +
					"XOOOOOOOXOOOOOOX" +
					"XOOOOOOECOOOOOOX" +
					"XOOOOOOOX4OOOOOX" + 
					"XXXXXXXXXXCOCXXX" +
					"XOEEEOOOOOOOOOXX" +
					"XOOOOOOOOOOOOOXX" +
					"XOOOOOOOOOOOOOXX" +
					"XOOOOOOOOOOOOOXX" +
					"XXXXXXOOOXXXXXXX" + 
					"XXXXXXOOOXXXXXXX" +
					"XXXXXXOOOXXXXXXX" +
					"XXXXXXOOOXXXXXXX" +
					"XOOOOOOOOOOOOOXX" +
					"XOOOOOOOOOOOOOXX" +
					"XOOOOOOOOOOOOOXX" +
					"XOOOOOOOOOOOOOXX" +
					"XXXXXXXXXXXXXXXX" +
					"XXXXXXXXXXXXXXXX" +
					"XXXXXXXXXXXXXXXX";
		
		int enemy = 0;
		
		for (int y = 0; y < Height; y++) {
			for (int x = 0; x < Width; x++) {
			
				int index = x + y * Width;
				try {
					char c = level.charAt(index);
					switch (c) {
						case 'X' : m_tiles[x][y] = TileType.TileWall; break;
						case 'O' : m_tiles[x][y] = TileType.TileEmpty; break;
						case 'P' : m_tiles[x][y] = TileType.TilePit; break;
						case 'C' : m_tiles[x][y] = TileType.TileCover; break;
						case 'E' : m_tiles[x][y] = TileType.TileEmpty;
								   if (enemy < Game.MAX_ENEMIES) 
									   m_enemyPositions[enemy] = new ModelPosition(x,y);
								   enemy++;
						default : m_tiles[x][y] = TileType.TileEmpty; break;
					}
					if (c >= '1' && c <= '0' + Game.MAX_SOLDIERS) {
						m_playerStartPositions[c - '1'] = new ModelPosition(x, y);
					}
				} catch (StringIndexOutOfBoundsException e) {
					
				}
			} 
		}*/
	}
	
	public void addEnemy(ModelPosition modelPosition) {
		
		// TODO Auto-generated method stub
		if (m_numEnemies < Game.MAX_ENEMIES) 
			m_enemyPositions[m_numEnemies] = modelPosition;
		m_numEnemies++;
	}
	

	public ModelPosition getStartLocation(int i) {
		return m_playerStartPositions[i];
	}
	
	public Enemy getEnemy(int i) {
		if (m_enemyPositions[i] != null)
			return new Enemy(m_enemyPositions[i]);
		return null;
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
	
	 private boolean isLosBlocked(float a_x, float a_y, float a_dx, float a_dy)
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

        if (dir.m_x > 0.0f) {
	        for (int x = (int)a_from.m_x+1; (float)x < a_to.m_x; x++) {
		        float u = ((float)x - a_from.m_x) / dir.m_x;
		        float y = a_from.m_y + dir.m_y * u;
                if (isLosBlocked(x, y, 0.01f, 0.0f)) {
                    return false;
                }
		
	        }
        } else if (dir.m_x < 0.0f) {
	        for (int x = (int)a_from.m_x; (float)x > a_to.m_x; x--) {
		        float u = ((float)x - a_from.m_x) / dir.m_x;
		        float y = a_from.m_y + dir.m_y * u;
                if (isLosBlocked(x, y, 0.01f, 0.0f)) {
                    return false;
                }
	        }
        }

        //y
        if (dir.m_y > 0.0f) {
	        for (int y = (int)a_from.m_y+1; (float)y < a_to.m_y; y++) {
		        float u = ((float)y - a_from.m_y) / dir.m_y;
		        float x = a_from.m_x + dir.m_x * u;
                if (isLosBlocked(x, y, 0.0f, 0.01f)) {
                    return false;
                }
    			
	        }
        } else if (dir.m_y < 0.0f) {
	        for (int y = (int)a_from.m_y; (float)y > a_to.m_y; y--) {
		        float u = ((float)y - a_from.m_y) / dir.m_y;
		        float x = a_from.m_x + dir.m_x * u;
                if (isLosBlocked(x, y, 0.0f, 0.01f)) {
                    return false;
                }
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

	
}
