package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.common.view.Mesh;
import com.spellofplay.common.view.RotatedTile;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;


class LevelDrawer {
		
	private ITexture m_theTextureMap;
	
	
	
	LevelDrawer(ITexture a_theTexture) {
		m_theTextureMap = a_theTexture;
		
	}

	
	void drawToBuffer(IModel model, AndroidDraw drawable, Camera camera) {
		Mesh backgroundMeshBlocked = new Mesh(Preferences.Width, Preferences.Height);
		Mesh crates = new Mesh(Preferences.Width, Preferences.Height);
		Mesh pits = new Mesh(Preferences.Width, Preferences.Height);
		
		for (int x = 0; x < Preferences.Width; x++) {
			for (int y = 0; y < Preferences.Height; y++) {
				//ViewPosition vp = camera.toViewPos(x, y);
				
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
		
		drawable.drawMeshToBackground(backgroundMeshBlocked, Preferences.Width, Preferences.Height, camera.getScale(), m_theTextureMap, true);
		drawable.drawMeshToBackground(crates, Preferences.Width, Preferences.Height, camera.getScale(), m_theTextureMap, false);
		drawable.drawMeshToBackground(pits, Preferences.Width, Preferences.Height, camera.getScale(), m_theTextureMap, false);
	}
	
	
	
	
	

}
