package com.spellofplay.dsp.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.common.view.Mesh;
import com.spellofplay.common.view.RotatedTile;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.Level;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.CharacterCollection;
import com.spellofplay.dsp.model.TileType;


public class LevelDrawer {
		
	private ITexture m_theTextureMap;
	
	private int[][] m_movementMap;
	
	public LevelDrawer(ITexture a_theTexture) {
		m_theTextureMap = a_theTexture;
		
		m_visibilityMap  = new boolean[Level.Width][];
		for (int x = 0; x< Level.Width; x++) {
			m_visibilityMap[x] = new boolean[Level.Height];
			for (int y = 0; y< Level.Height; y++) {
				m_visibilityMap[x][y] = false;
			}
		}
		
		m_movementMap  = new int[Level.Width][];
		for (int x = 0; x< Level.Width; x++) {
			m_movementMap[x] = new int[Level.Height];
			for (int y = 0; y< Level.Height; y++) {
				m_movementMap[x][y] = 1000;
			}
		}
		
	}
	/*Draws to buffer
	 * */
	public void draw(Level level, AndroidDraw drawable, Camera camera) {
		
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
				RotatedTile rotTileBlocked = new RotatedTile(level.GetTile(x, y) != TileType.TileWall,
													 		 level.GetTile(x +1, y) != TileType.TileWall,
															 level.GetTile(x, y+1) != TileType.TileWall,
															 level.GetTile(x +1, y + 1) != TileType.TileWall);
													
				
				
				if (rotTileBlocked.isEmpty()) 
				{
					
					backgroundMeshBlocked.AddRectangle(rotTileBlocked.getSrcRect(variation, 32), dst, rotTileBlocked.rotation);
					
				}
				
				variation = 2;
				rotTileBlocked = new RotatedTile(level.GetTile(x, y) == TileType.TileCover,
													 		 level.GetTile(x +1, y) == TileType.TileCover,
															 level.GetTile(x, y+1) == TileType.TileCover,
															 level.GetTile(x +1, y + 1) == TileType.TileCover);
				
				if (rotTileBlocked.isEmpty()) 
				{
					
					crates.AddRectangle(rotTileBlocked.getSrcRect(variation, 32), dst, rotTileBlocked.rotation);
					
				}
				
				variation = 2;
				rotTileBlocked = new RotatedTile(level.GetTile(x, y) == TileType.TilePit,
													 		 level.GetTile(x +1, y) == TileType.TilePit,
															 level.GetTile(x, y+1) == TileType.TilePit,
															 level.GetTile(x +1, y + 1) == TileType.TilePit);
				
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

	//Visibility map
	private int m_updateX = 0;
	private boolean[][] m_visibilityMap;
	
	public void startUpdateVisibility() {
		m_updateX = 0;
	}
	
	private void updateVisibility(ModelFacade a_model) {

		CharacterCollection<Soldier> soldiers = a_model.getAliveSoldiers();
		//for (int x = 0; x < Level.Width; x++) {
		int x = m_updateX;
		
		if (x < Level.Width) {
			for (int y = 0; y < Level.Height; y++) {
				for (Soldier s : soldiers) {
					
					if (a_model.getLevel().m_tiles[x][y] == TileType.TileEmpty) {
						m_visibilityMap[x][y] = false;
						
						if (a_model.canSeeMapPosition(s, new ModelPosition(x, y))) {
							m_visibilityMap[x][y] = true;
							break;
						}
					} else {
						m_visibilityMap[x][y] = true;
					}
				}
			}
			m_updateX++;
		}
	}
	
	public void drawNotVisible(ModelFacade a_model, AndroidDraw drawable, Camera camera) {
		updateVisibility(a_model);
		updateVisibility(a_model);
		
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				
				
				if (m_visibilityMap[x][y] == false) {
					ViewPosition vp = camera.toViewPos(x, y);
					Rect dst = new Rect((int)vp.m_x - camera.getHalfScale(), 
										(int)vp.m_y - camera.getHalfScale(),
										(int)vp.m_x + camera.getHalfScale(), 
										(int)vp.m_y + camera.getHalfScale());
				
				
							
					drawable.drawFog(dst);
				}
			}	
		}
	}
	
	private void updateMoveMap(IMoveAndVisibility a_checker, Soldier selected) {
		
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				m_movementMap[x][y] = 1000;
			}
			
		}
		
		if (selected != null) {
			
			
			m_movementMap[selected.getPosition().m_x][selected.getPosition().m_y] = 0;
			
			boolean hasAddedNewNodes = true;
			
			while (hasAddedNewNodes) {
				hasAddedNewNodes = false;
				for (int x = 0; x < Level.Width; x++) {
					for (int y = 0; y < Level.Height; y++) {
					
						if (m_movementMap[x][y] < selected.getTimeUnits()) {
							
							int travelCost = m_movementMap[x][y] + 1;
							
							//check the neighbours
							for (int dx = -1; dx < 2; dx++) {
								for (int dy = -1; dy < 2; dy++) { 
									if (dx == 0 && dy == 0) {
										continue;
									}
									//Stay inside level
									if (x+dx >= Level.Width || y+dy >= Level.Height) {
										continue;
									}
									if (x+dx < 0 || y+dy < 0) {
										continue;
									}
									

									if (a_checker.isMovePossible(new ModelPosition(x+dx, y+dy), false) == false) {
										continue;
									}
									
									//Diagonala moves
									if (dx == dy || dx == -dy) {
								        if (a_checker.isMovePossible(new ModelPosition(x + dx, y), false) == false)
									        continue;
								        if (a_checker.isMovePossible(new ModelPosition(x, y + dy), false) == false)
									        continue;
							        }
									
									//har vi redan en h�gre movementcost d�r ?
									if (m_movementMap[x+dx][y+dy] > travelCost ) {
										m_movementMap[x+dx][y+dy] = travelCost;
										hasAddedNewNodes = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	public void drawPossibleMoveArea(IMoveAndVisibility a_checker, AndroidDraw drawable, Camera camera, Soldier selected) {
		updateMoveMap(a_checker, selected);
		if (selected == null)
			return;
		
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				
				
				if (m_movementMap[x][y] <= selected.getTimeUnits()) {
					ViewPosition vp = camera.toViewPos(x, y);
					Rect dst = new Rect((int)vp.m_x - camera.getHalfScale(), 
										(int)vp.m_y - camera.getHalfScale(),
										(int)vp.m_x + camera.getHalfScale(), 
										(int)vp.m_y + camera.getHalfScale());
				
				
					if (m_movementMap[x][y] <= selected.getTimeUnits() - selected.getFireCost())	
						drawable.drawRect(dst, Color.argb(48, 0, 255, 0));
					else
						drawable.drawRect(dst, Color.argb(32, 0, 255, 128));
					
					drawable.drawText("" + m_movementMap[x][y], (int)vp.m_x, (int)vp.m_y);
				}
			}	
		}
	}
	
	
	

}
