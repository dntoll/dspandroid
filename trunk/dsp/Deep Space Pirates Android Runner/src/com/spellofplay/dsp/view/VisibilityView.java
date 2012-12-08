package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;

class VisibilityView {
	private enum Visibility {
		NeverSeen,
		NotSeen,
		//SeenBySelected,
		SeenByAll	
	}
	
	private Visibility[][] visibilityMap;
	
	public VisibilityView() {
		visibilityMap  = new Visibility[Preferences.Width][];
		clear();
	}

	public void clear() {
		for (int x = 0; x< Preferences.Width; x++) {
			visibilityMap[x] = new Visibility[Preferences.Height];
			for (int y = 0; y< Preferences.Height; y++) {
				visibilityMap[x][y] = Visibility.NeverSeen;
			}
		}
	}
	
	void drawNotVisible(IModel a_model, AndroidDraw drawable, Camera camera) {
		for (int x = 0; x < Preferences.Width; x++) {
			for (int y = 0; y < Preferences.Height; y++) {
				
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
	
	void updateVisibility(IModel a_model) {
		CharacterIterable soldiers = a_model.getAliveSoldiers();
		
		for (int x = 0; x < Preferences.Width; x++) {
			for (int y = 0; y < Preferences.Height; y++) {
				for (ICharacter s : soldiers) {
					if (a_model.getTile(x, y) != TileType.TileWall) {
						
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
		
		for (int x = 0; x < Preferences.Width; x++) {
			for (int y = 0; y < Preferences.Height; y++) {
				if (a_model.getTile(x, y) == TileType.TileWall) {
					ifNeighbourVisible(x, y, a_model);
				}
			}
		}
		
	}

	private void ifNeighbourVisible(int x, int y, IModel level) {
		for (int dx = -1; dx < 2; dx++) {
			for (int dy = -1; dy < 2; dy++) {
				if (level.getTile(x +dx ,y +dy) != TileType.TileWall) {
					if (getVisibility(x +dx, y+dy) == Visibility.SeenByAll) {
						visibilityMap[x][y] = Visibility.SeenByAll;
					}
				}
			}
		}
	}
	
	private Visibility getVisibility(int x, int y) {
		if (x < 0 || y < 0 || x >= Preferences.Width || y >= Preferences.Height) {
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
