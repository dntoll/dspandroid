package com.spellofplay.dsp.view;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;


class LevelDrawer {
		
	private ITexture theTextureMap;
	private IModel model;
	
	
	LevelDrawer(ITexture a_theTexture, IModel model) {
		this.theTextureMap = a_theTexture;
		this.model = model;
		
	}

	
	void drawToBuffer(AndroidDraw drawable, Camera camera) {
		Mesh backgroundMeshBlocked = new Mesh(Preferences.WIDTH, Preferences.HEIGHT);
		Mesh crates = new Mesh(Preferences.WIDTH, Preferences.HEIGHT);
		Mesh pits = new Mesh(Preferences.WIDTH, Preferences.HEIGHT);
		
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				
				Rect dst = new Rect(x*camera.getScale(), 
						y*camera.getScale(),
						x*camera.getScale() + camera.getScale(), 
						y*camera.getScale() + camera.getScale());
				
				int variation = (x+y)%2 ;
				RotatedTile rotTileBlocked = new RotatedTile(model.getTile(x, y) != TileType.TileWall,
															 model.getTile(x +1, y) != TileType.TileWall,
															 model.getTile(x, y+1) != TileType.TileWall,
															 model.getTile(x +1, y + 1) != TileType.TileWall);
													
				
				
				if (rotTileBlocked.isEmpty()) 
				{
					
					backgroundMeshBlocked.AddRectangle(rotTileBlocked.getSrcRect(variation, 32), dst, rotTileBlocked.rotation);
					
				}
				
				variation = 2;
				rotTileBlocked = new RotatedTile(model.getTile(x, y) == TileType.TileCover,
													 		 model.getTile(x +1, y) == TileType.TileCover,
															 model.getTile(x, y+1) == TileType.TileCover,
															 model.getTile(x +1, y + 1) == TileType.TileCover);
				
				if (rotTileBlocked.isEmpty()) 
				{
					
					crates.AddRectangle(rotTileBlocked.getSrcRect(variation, 32), dst, rotTileBlocked.rotation);
					
				}
				
				variation = 2;
				rotTileBlocked = new RotatedTile(model.getTile(x, y) == TileType.TilePit,
													 		 model.getTile(x +1, y) == TileType.TilePit,
															 model.getTile(x, y+1) == TileType.TilePit,
															 model.getTile(x +1, y + 1) == TileType.TilePit);
				
				if (rotTileBlocked.isEmpty()) 
				{
					
					pits.AddRectangle(rotTileBlocked.getSrcRect(variation, 32), dst, rotTileBlocked.rotation);
					
				}
	
			}	
		}
		
		drawable.drawMeshToBackground(backgroundMeshBlocked, Preferences.WIDTH, Preferences.HEIGHT, camera.getScale(), theTextureMap, true);
		drawable.drawMeshToBackground(crates, Preferences.WIDTH, Preferences.HEIGHT, camera.getScale(), theTextureMap, false);
		drawable.drawMeshToBackground(pits, Preferences.WIDTH, Preferences.HEIGHT, camera.getScale(), theTextureMap, false);
	}


	public void drawDoors(AndroidDraw drawable, Camera camera) {
		Rect source = new Rect(0, 256-32, 32, 256);
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				
				if (model.getTile(x, y) == TileType.TileDoor) {
					
					Rect dst = camera.toViewRect(x, y);
					
					
					drawable.drawBitmap(theTextureMap, source, dst, Color.WHITE, 0);
				}
			}
		}
		
	}
	
	
	
	
	

}
