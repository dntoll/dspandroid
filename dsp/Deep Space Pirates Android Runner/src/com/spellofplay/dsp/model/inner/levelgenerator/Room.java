package com.spellofplay.dsp.model.inner.levelgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Rect;

import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.Level;


class Room {

	ModelPosition upperLeftCorner;
	Vector2 sizes;
	private Random random;
	public Room(ModelPosition upperLeftCorner, Vector2 size, Random random) {
		this.upperLeftCorner = upperLeftCorner;
		this.sizes = size;
		this.random = random;
	}

	boolean addRoom(Level level) {
		
		for (int x = upperLeftCorner.x-1; x <= upperLeftCorner.x + sizes.x; x++) {
			for (int y = upperLeftCorner.y-1; y <= getBottom()+1; y++) {
				if (level.GetTile(x, y) != TileType.TileWall) {
					return false;
				}
			}	
		}
		
		for (int x = upperLeftCorner.x; x < upperLeftCorner.x + sizes.x; x++) {
			for (int y = upperLeftCorner.y; y <= getBottom(); y++) {
				level.m_tiles[x][y] = TileType.TileEmpty;
			}	
		}
		return true;
	}

	void addPlayers(Level level) {
		for (int i = 0; i< Preferences.MAX_SOLDIERS; i++) {
			level.m_playerStartPositions[i] = new ModelPosition(upperLeftCorner.x + i+1, upperLeftCorner.y+1); 
		}
	}

	public int getBottom() {
		return (int) (upperLeftCorner.y + sizes.y - 1);
	}
	public int getRight() {
		return (int) (upperLeftCorner.x + sizes.x - 1);
	}

	void addEnemies(Level level) {
		
		int numEnemies = random.nextInt(3);
		for (int i = 0; i< numEnemies; i++) {

			int x = upperLeftCorner.x + i+1;
			int y = upperLeftCorner.y+1;
			
			if (level.GetTile(x, y) == TileType.TileEmpty)
				level.addEnemy(new ModelPosition(x, y));
			
		}
	}

	void addDoor(Room parent, Level level) {
		List<ModelPosition> connections = getConnections(parent);
		
		if (connections.size() == 0)
			return;
		int doorat = random.nextInt(connections.size());
		ModelPosition door = connections.get(doorat);
		level.m_tiles[door.x][door.y] = TileType.TileDoor;
		
		
	}

	private List<ModelPosition> getConnections(Room parent) {
		
		List<ModelPosition> connections = new ArrayList<ModelPosition>();
		
		Rect parentRect = new Rect(parent.upperLeftCorner.x-1, 
								   parent.upperLeftCorner.y-1, 
								   parent.upperLeftCorner.x + (int)parent.sizes.x + 1, 
								   parent.upperLeftCorner.y + (int)parent.sizes.y+1);
		Rect myRect = new Rect(upperLeftCorner.x-1, 
							   upperLeftCorner.y-1, 
							   upperLeftCorner.x + (int)sizes.x+1, 
							   upperLeftCorner.y + (int)sizes.y+1);
		
		Rect intersection = new Rect();
		intersection.setIntersect(parentRect, myRect);
		
		for (int x = intersection.left; x < intersection.right; x++) {
			if (insideX(x) && parent.insideX(x))
				connections.add(new ModelPosition(x, intersection.top));
		}
		for (int y = intersection.top; y < intersection.bottom; y++) {
			if (insideY(y) && parent.insideY(y))
				connections.add(new ModelPosition(intersection.left, y));
		}
		
		return connections;
	}

	private boolean insideY(int y) {
		return y >= upperLeftCorner.y && y < upperLeftCorner.y + sizes.y; 
	}

	private boolean insideX(int x) {
		return x >= upperLeftCorner.x && x < upperLeftCorner.x + sizes.x;
	}

	void addDecorations(Level level) {
		TileType type = TileType.TileEmpty;
		switch (random.nextInt(3)) {
			case 0 : type = TileType.TileCover; break;
			case 1 : type = TileType.TileWall; break;
			case 2 : type = TileType.TilePit; break;
			
		}
		
		
		for (int x = upperLeftCorner.x+2; x < upperLeftCorner.x + sizes.x-2; x++) {
			for (int y = upperLeftCorner.y+2; y <= getBottom()-2; y++) {
				level.m_tiles[x][y] = type;
			}	
		}
	}

	
	
}
