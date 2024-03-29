package com.spellofplay.dsp.model.inner.levelgenerator;

import java.util.Random;

import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.Level;

public class LevelGenerator {

	private int minimumRoomSize = 3;
	private int maxRoomSize = 9;
	private Random random;
	
	public LevelGenerator(int a_level) {
		random = new Random(a_level);
	}

	public void generate(Level level) {
		level.clear();
		
		Room room = createStartRoom(level);
		
		createConnectedRoom(room, level);
		
		
		createWindows(level);
	}

	private void createWindows(Level level) {
		
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				if (level.isWallAndHasTwoClearSides(x,y) ) {
					addExtraDoorsAndWindows(level, x, y);
				}
			}
		}
	}

	private void addExtraDoorsAndWindows(Level level, int x, int y) {
		switch (random.nextInt(6)) 
		{
			case 0:
			case 1:
				level.m_tiles[x][y] = TileType.TileCover;
				break;
			case 2:
				level.m_tiles[x][y] = TileType.TileDoor;
				break;
			
		}
	}
	
	private void createConnectedRoom(Room parent, Level level) {
		switch (random.nextInt(4)) {
			case 0 : tryCreateRoomNorth(parent, level);
					 tryCreateRoomEast(parent, level);
					 tryCreateRoomSouth(parent, level);
					 tryCreateRoomWest(parent, level); 
					 break;
			case 1 : tryCreateRoomSouth(parent, level); 
					 tryCreateRoomWest(parent, level);
					 tryCreateRoomNorth(parent, level);
					 tryCreateRoomEast(parent, level);
					 break;
			case 2 : tryCreateRoomWest(parent, level);
					 tryCreateRoomNorth(parent, level);
					 tryCreateRoomEast(parent, level);
					 tryCreateRoomSouth(parent, level);
					 break;
			case 3 : tryCreateRoomEast(parent, level);
					 tryCreateRoomSouth(parent, level);
					 tryCreateRoomWest(parent, level); 
					 tryCreateRoomNorth(parent, level);
					 break;
			
		}
		
	}
	
	private void tryCreateRoomNorth(Room parent, Level level) {
		int sizex = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		int sizey = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		
		int areaAboveParent = parent.upperLeftCorner.y - 1;
		int top = parent.upperLeftCorner.y - 1 - sizey;
		
		if (areaAboveParent < sizey) {
			return;
		}
		
		createYroom(parent, level, top, sizex, sizey);
	}
	
	private void tryCreateRoomSouth(Room parent, Level level) {
		int sizex = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		int sizey = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		
		int areaToTheBottomOfParent = (int) (Preferences.HEIGHT - (parent.upperLeftCorner.y + parent.sizes.y+1));
		int top = (int) (parent.upperLeftCorner.y + parent.sizes.y + 1);
		
		if (areaToTheBottomOfParent < sizey) {
			return;
		}
		
		createYroom(parent, level, top, sizex, sizey);
	}

	private void tryCreateRoomWest(Room parent, Level level) {
		int sizex = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		int sizey = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		
		int areaToTheLeftOfParent = parent.upperLeftCorner.x - 1;
		int left = parent.upperLeftCorner.x - 1 - sizex;
		
		if (areaToTheLeftOfParent < sizex) {
			return;
		}
				
		createXroom(parent, level, left, sizex, sizey);
	}
	
	private void tryCreateRoomEast(Room parent, Level level) {
		int sizex = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		int sizey = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		
		int areaToTheRightOfParent = (int) (Preferences.WIDTH - (parent.upperLeftCorner.x + parent.sizes.x+1));
		int left = (int) (parent.upperLeftCorner.x + parent.sizes.x + 1);
				
		if (areaToTheRightOfParent < sizex) {
			return;
		}

		createXroom(parent, level, left, sizex, sizey);
	}
	
	private void createYroom(Room parent, Level level, int top, int sizex,
			int sizey) {
		int minLeftLocation = parent.upperLeftCorner.x - sizex+1;
		
		if (minLeftLocation < 1) {
			minLeftLocation = 1;
		}
		
		int maxLeftLocation = parent.getRight();
		
		if (maxLeftLocation + sizex >= Preferences.WIDTH)
			maxLeftLocation = Preferences.WIDTH - sizex -1;
		
		int left = minLeftLocation + random.nextInt(maxLeftLocation - minLeftLocation);
		
		CreateRoom(parent, level, top, sizex, sizey, left);
	}

	

	private void createXroom(Room parent, Level level, int left, int sizex,
			int sizey) {
		int minTopLocation = parent.upperLeftCorner.y - sizey+1;
		
		if (minTopLocation < 1) {
			minTopLocation = 1;
		}
		
		int maxTopLocation = parent.getBottom();
		
		if (maxTopLocation + sizey >= Preferences.HEIGHT)
			maxTopLocation = Preferences.HEIGHT - sizey -1;
		
		int top = minTopLocation + random.nextInt(maxTopLocation - minTopLocation);
		
		CreateRoom(parent, level, top, sizex, sizey, left);
	}
	
	private void CreateRoom(Room parent, Level level, int top, int sizex,
			int sizey, int left) {
		ModelPosition upperLeftCorner = new ModelPosition(left, top);
		Vector2 size = new Vector2(sizex, sizey);
		
		Room ret = new Room(upperLeftCorner, size, random);
		if (ret.addRoom(level)) {
			
			ret.addDecorations(level);
			ret.addDoor(parent, level);
			
			ret.addEnemies(level);
			
			createConnectedRoom(ret, level);
		}
	}
	

	private Room createStartRoom(Level level) {
		int roomSizeX = 5;
		int roomSizeY = 5;
		
		ModelPosition upperLeftCorner = new ModelPosition(2, 2);
		Vector2 size = new Vector2(roomSizeX, roomSizeY);
		
		Room ret = new Room(upperLeftCorner, size, random);
		
		ret.addRoom(level);
		
		ret.addPlayers(level);
		
		return ret;
	}

}
