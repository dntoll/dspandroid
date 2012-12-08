package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.common.view.Mesh;
import com.spellofplay.common.view.RotatedTile;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.inner.Level;


public class LevelDrawer {
		
	private ITexture m_theTextureMap;
	
	
	
	public LevelDrawer(ITexture a_theTexture) {
		m_theTextureMap = a_theTexture;
		
	}

	
	public void drawToBuffer(IModel model, AndroidDraw drawable, Camera camera) {
		Mesh backgroundMeshBlocked = new Mesh(Level.Width, Level.Height);
		Mesh crates = new Mesh(Level.Width, Level.Height);
		Mesh pits = new Mesh(Level.Width, Level.Height);
		
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
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
		
		drawable.drawMeshToBackground(backgroundMeshBlocked, Level.Width, Level.Height, camera.getScale(), m_theTextureMap, true);
		drawable.drawMeshToBackground(crates, Level.Width, Level.Height, camera.getScale(), m_theTextureMap, false);
		drawable.drawMeshToBackground(pits, Level.Width, Level.Height, camera.getScale(), m_theTextureMap, false);
	}
	
	
	
	
	

}
