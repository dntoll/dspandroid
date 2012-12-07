package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.dsp.model.CharacterCollection;
import com.spellofplay.dsp.model.Level;
import com.spellofplay.dsp.model.ModelFacade;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.model.TileType;

public class VisibilityView {
	private enum Visibility {
		NeverSeen,
		NotSeen,
		//SeenBySelected,
		SeenByAll	
	}
	
	private Visibility[][] visibilityMap;
	
	public VisibilityView() {
		visibilityMap  = new Visibility[Level.Width][];
		clear();
	}

	public void clear() {
		for (int x = 0; x< Level.Width; x++) {
			visibilityMap[x] = new Visibility[Level.Height];
			for (int y = 0; y< Level.Height; y++) {
				visibilityMap[x][y] = Visibility.NeverSeen;
			}
		}
	}
	
	void drawNotVisible(ModelFacade a_model, AndroidDraw drawable, Camera camera) {
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				
				if (canSee(x,y) == false) {
					ViewPosition vp = camera.toViewPos(x, y);
					Rect dst = new Rect((int)vp.m_x - camera.getHalfScale(), 
										(int)vp.m_y - camera.getHalfScale(),
										(int)vp.m_x + camera.getHalfScale(), 
										(int)vp.m_y + camera.getHalfScale());
					
					if (neverSeen(x,y))
						drawable.drawBlack(dst);
					else
						drawable.drawFog(dst);
					
				}
			}	
		}
	}
	
	void updateVisibility(ModelFacade a_model) {
		CharacterCollection<Soldier> soldiers = a_model.getAliveSoldiers();
		
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				for (Soldier s : soldiers) {
					if (a_model.getLevel().m_tiles[x][y] != TileType.TileWall) {
						
						if (visibilityMap[x][y] != Visibility.NeverSeen)
							visibilityMap[x][y] = Visibility.NotSeen;
						
						if (a_model.canSeeMapPosition(s, new ModelPosition(x, y))) {
							visibilityMap[x][y] = Visibility.SeenByAll;
							break;
						}
					}
				}
			}
		}
		
		for (int x = 0; x < Level.Width; x++) {
			for (int y = 0; y < Level.Height; y++) {
				if (a_model.getLevel().m_tiles[x][y] == TileType.TileWall) {
					ifNeighbourVisible(x, y, a_model.getLevel());
				}
			}
		}
		
	}

	private void ifNeighbourVisible(int x, int y, Level level) {
		for (int dx = -1; dx < 2; dx++) {
			for (int dy = -1; dy < 2; dy++) {
				if (level.GetTile(x +dx ,y +dy) != TileType.TileWall) {
					if (getVisibility(x +dx, y+dy) == Visibility.SeenByAll) {
						visibilityMap[x][y] = Visibility.SeenByAll;
					}
				}
			}
		}
	}
	
	private Visibility getVisibility(int x, int y) {
		if (x < 0 || y < 0 || x >= Level.Width || y >= Level.Height) {
			return Visibility.NotSeen;
		}
		return visibilityMap[x][y];
	}

	

	private boolean neverSeen(int x, int y) {
		return visibilityMap[x][y] == Visibility.NeverSeen;
	}

	private boolean canSee(int x, int y) {
		return visibilityMap[x][y] == Visibility.SeenByAll;
	}
}
