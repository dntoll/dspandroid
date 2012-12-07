package com.spellofplay.dsp.model.levelgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Rect;

import com.spellofplay.dsp.model.Game;
import com.spellofplay.dsp.model.Level;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.Vector2;


public class Room {

	ModelPosition upperLeftCorner;
	Vector2 sizes;
	Random random;
	public Room(ModelPosition upperLeftCorner, Vector2 size, Random random) {
		this.upperLeftCorner = upperLeftCorner;
		this.sizes = size;
		this.random = random;
	}

	public boolean addRoom(Level level) {
		
		for (int x = upperLeftCorner.m_x-1; x <= upperLeftCorner.m_x + sizes.m_x; x++) {
			for (int y = upperLeftCorner.m_y-1; y <= getBottom()+1; y++) {
				if (level.GetTile(x, y) != TileType.TileWall) {
					return false;
				}
			}	
		}
		
		for (int x = upperLeftCorner.m_x; x < upperLeftCorner.m_x + sizes.m_x; x++) {
			for (int y = upperLeftCorner.m_y; y <= getBottom(); y++) {
				level.m_tiles[x][y] = TileType.TileEmpty;
			}	
		}
		return true;
	}

	public void addPlayers(Level level) {
		for (int i = 0; i< Game.MAX_SOLDIERS; i++) {
			level.m_playerStartPositions[i] = new ModelPosition(upperLeftCorner.m_x + i+1, upperLeftCorner.m_y+1); 
		}
	}

	public int getBottom() {
		return (int) (upperLeftCorner.m_y + sizes.m_y - 1);
	}
	public int getRight() {
		return (int) (upperLeftCorner.m_x + sizes.m_x - 1);
	}

	public void addEnemies(Level level) {
		
		int numEnemies = random.nextInt(3);
		for (int i = 0; i< numEnemies; i++) {
			
			level.addEnemy(new ModelPosition(upperLeftCorner.m_x + i+1, upperLeftCorner.m_y+1));
			
		}
	}

	public void addDoor(Room parent, Level level) {
		List<ModelPosition> connections = getConnections(parent);
		
		if (connections.size() == 0)
			return;
		int doorat = random.nextInt(connections.size());
		ModelPosition door = connections.get(doorat);
		level.m_tiles[door.m_x][door.m_y] = TileType.TileEmpty;
		
		
	}

	private List<ModelPosition> getConnections(Room parent) {
		
		List<ModelPosition> connections = new ArrayList<ModelPosition>();
		
		Rect parentRect = new Rect(parent.upperLeftCorner.m_x-1, 
								   parent.upperLeftCorner.m_y-1, 
								   parent.upperLeftCorner.m_x + (int)parent.sizes.m_x + 1, 
								   parent.upperLeftCorner.m_y + (int)parent.sizes.m_y+1);
		Rect myRect = new Rect(upperLeftCorner.m_x-1, 
							   upperLeftCorner.m_y-1, 
							   upperLeftCorner.m_x + (int)sizes.m_x+1, 
							   upperLeftCorner.m_y + (int)sizes.m_y+1);
		
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
		return y >= upperLeftCorner.m_y && y < upperLeftCorner.m_y + sizes.m_y; 
	}

	private boolean insideX(int x) {
		return x >= upperLeftCorner.m_x && x < upperLeftCorner.m_x + sizes.m_x;
	}

	public void addDecorations(Level level) {
		TileType type = TileType.TileEmpty;
		switch (random.nextInt(3)) {
			case 0 : type = TileType.TileCover; break;
			case 1 : type = TileType.TileWall; break;
			case 2 : type = TileType.TilePit; break;
			
		}
		
		
		for (int x = upperLeftCorner.m_x+2; x < upperLeftCorner.m_x + sizes.m_x-2; x++) {
			for (int y = upperLeftCorner.m_y+2; y <= getBottom()-2; y++) {
				level.m_tiles[x][y] = type;
			}	
		}
	}

	
	
}
