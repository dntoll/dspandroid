package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Rect;

public class LevelGenerator {

	class Room {

		private ModelPosition upperLeftCorner;
		private Vector2 sizes;
		
		public Room(ModelPosition upperLeftCorner, Vector2 size) {
			this.upperLeftCorner = upperLeftCorner;
			this.sizes = size;
		}

		public void addRoom(Level level) {
			for (int x = upperLeftCorner.m_x; x < upperLeftCorner.m_x + sizes.m_x; x++) {
				for (int y = upperLeftCorner.m_y; y <= getBottom(); y++) {
					level.m_tiles[x][y] = TileType.TileEmpty;
				}	
			}
		}

		public void addPlayers(Level level) {
			for (int i = 0; i< Game.MAX_SOLDIERS; i++) {
				level.m_playerStartPositions[i] = new ModelPosition(upperLeftCorner.m_x + i+1, upperLeftCorner.m_y+1); 
			}
		}

		public int getBottom() {
			return (int) (upperLeftCorner.m_y + sizes.m_y - 1);
		}

		public void addEnemies(Level level) {
			
			int numEnemies = random.nextInt(3);
			for (int i = 0; i< numEnemies; i++) {
				
				level.addEnemy(new ModelPosition(upperLeftCorner.m_x + i+1, upperLeftCorner.m_y+1));
				
			}
		}

		public void addDoor(Room parent, Level level) {
			List<ModelPosition> connections = getConnections(parent);
			
			ModelPosition door = connections.get(0);
			level.m_tiles[door.m_x][door.m_y] = TileType.TileEmpty;
		}

		private List<ModelPosition> getConnections(Room parent) {
			
			List<ModelPosition> connections = new ArrayList<ModelPosition>();
			
			Rect parentRect = new Rect(parent.upperLeftCorner.m_x-1, parent.upperLeftCorner.m_y-1, parent.upperLeftCorner.m_x + (int)parent.sizes.m_x + 1, parent.upperLeftCorner.m_y + (int)parent.sizes.m_y+1);
			Rect myRect = new Rect(upperLeftCorner.m_x+1, upperLeftCorner.m_y+1, upperLeftCorner.m_x + (int)sizes.m_x+1, upperLeftCorner.m_y + (int)sizes.m_y+1);
			
			Rect intersection = new Rect();
			intersection.setIntersect(parentRect, myRect);
			
			for (int x = intersection.left+1; x < intersection.right-1; x++) {
				connections.add(new ModelPosition(x, intersection.top));
			}
			for (int y = intersection.top+1; y < intersection.bottom-1; y++) {
				connections.add(new ModelPosition(intersection.left, y));
			}
			
			return connections;
		}
		
	}
	
	Random random;
	public LevelGenerator(int a_level) {
		random = new Random();
	}

	public void generate(Level level) {
		level.clear();
		
		Room room = createStartRoom(level);
		
		createConnectedRoom(room, level);
	}

	private void createConnectedRoom(Room parent, Level level) {
		
		tryCreateRoomWest(parent, level);
	/*	tryCreateRoomEast(parent, level);
		tryCreateRoomNorth(parent, level);
		tryCreateRoomSouth(parent, level);*/
		
	}
	
	int minimumRoomSize = 4;
	int maxRoomSize = 10;

	private void tryCreateRoomWest(Room parent, Level level) {
		
		int areaToTheLeftOfParent = parent.upperLeftCorner.m_x - 1;
		
		int sizex = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		int sizey = minimumRoomSize + random.nextInt(maxRoomSize - minimumRoomSize);
		
		if (areaToTheLeftOfParent < sizex) {
			return;
		}
		
		int left = parent.upperLeftCorner.m_x - 1 - sizex;
				
		int minTopLocation = parent.upperLeftCorner.m_y - sizey;
		
		if (minTopLocation < 0) {
			minTopLocation = 0;
		}
		
		int maxTopLocation = parent.getBottom();
		
		if (maxTopLocation + sizey >= Level.Height)
			maxTopLocation = Level.Height - sizey -1;
		
		int top = minTopLocation + random.nextInt(maxTopLocation - minTopLocation);
		
		ModelPosition upperLeftCorner = new ModelPosition(left, top);
		Vector2 size = new Vector2(sizex, sizey);
		
		Room ret = new Room(upperLeftCorner, size);
		ret.addRoom(level);
		ret.addEnemies(level);
		ret.addDoor(parent, level);
		
		createConnectedRoom(ret, level);
	}

	private Room createStartRoom(Level level) {
		

		int roomSizeX = minimumRoomSize + random.nextInt(maxRoomSize-minimumRoomSize);
		int roomSizeY = minimumRoomSize + random.nextInt(maxRoomSize-minimumRoomSize);
		
		int areaX = Level.Width - maxRoomSize *  2 - roomSizeX;
		int areaY = Level.Height - maxRoomSize *  2 - roomSizeY;
		ModelPosition upperLeftCorner = new ModelPosition(maxRoomSize  + random.nextInt(areaX), maxRoomSize  + random.nextInt(areaY));
		Vector2 size = new Vector2(roomSizeX, roomSizeY);
		
		Room ret = new Room(upperLeftCorner, size);
		
		ret.addRoom(level);
		
		ret.addPlayers(level);
		
		return ret;
	}

}
