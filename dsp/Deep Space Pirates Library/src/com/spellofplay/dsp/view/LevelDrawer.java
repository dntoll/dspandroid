package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.common.view.Mesh;
import com.spellofplay.dsp.model.Level;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.common.view.RotatedTile;


public class LevelDrawer {
		
	private ITexture m_theTextureMap;
	
	public LevelDrawer(ITexture a_theTexture) {
		m_theTextureMap = a_theTexture;
	}

	public void draw(Level level, IDraw drawable, int a_scale) {
		
		Mesh backgroundMeshBlocked = new Mesh(Level.Width, Level.Height);
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				
				
				Rect dst = new Rect(x * a_scale, 
						y * a_scale,
						x * a_scale + a_scale, 
						y * a_scale + a_scale);
				
				int variation = 2;
				RotatedTile rotTileBlocked = new RotatedTile(level.GetTile(x, y) != TileType.TileWall,
													 		 level.GetTile(x +1, y) != TileType.TileWall,
															 level.GetTile(x, y+1) != TileType.TileWall,
															 level.GetTile(x +1, y + 1) != TileType.TileWall);
													
				
				
				if (rotTileBlocked.isEmpty()) 
				{
					
					backgroundMeshBlocked.AddRectangle(rotTileBlocked.getSrcRect(variation, a_scale), dst, rotTileBlocked.rotation);
					
				}
	
			}	
		}
		drawable.drawMeshToBackground(backgroundMeshBlocked, Level.Width, Level.Height, a_scale, m_theTextureMap);
		
		
	}
	
	

}
