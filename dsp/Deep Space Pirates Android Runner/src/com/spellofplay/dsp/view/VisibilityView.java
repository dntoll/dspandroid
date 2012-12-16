package com.spellofplay.dsp.view;

import android.graphics.Rect;

import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;
import com.spellofplay.dsp.model.TileType;
import com.spellofplay.dsp.model.inner.IPersistance;

class VisibilityView {
	enum Visibility {
		NeverSeen,
		NotSeen,
		SeenByAll;

		public boolean hasSameExploredVisibility(Visibility visibility) {
			if (this == NeverSeen) {
				if (visibility != NeverSeen) 
					return false;
				else
					return true;
			}
			if (visibility == NeverSeen) {
				return false;
			}
			return true;
		}	
	}
	
	private Visibility[][] visibilityMap;
	private Visibility[][] calculationMap;
	private boolean shouldUpdateVisibility = true;
	private int visibilityProgressX = 0;
	
	public VisibilityView() {
		visibilityMap  = new Visibility[Preferences.WIDTH][];
		calculationMap = new Visibility[Preferences.WIDTH][];
		clear();
	}

	public void clear() {
		for (int x = 0; x< Preferences.WIDTH; x++) {
			visibilityMap[x] = new Visibility[Preferences.HEIGHT];
			calculationMap[x] = new Visibility[Preferences.HEIGHT];
			for (int y = 0; y< Preferences.HEIGHT; y++) {
				visibilityMap[x][y] = Visibility.NeverSeen;
				calculationMap[x][y] = Visibility.NeverSeen;
			}
		}
	}
	
	void drawNotVisible(IModel a_model, AndroidDraw drawable, Camera camera) {
		updateVisibility(a_model);
		
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				
				if (canSee(x,y) == false) {
					
					Rect dst = camera.toViewRect(x,y);
					
					if (neverSeen(x,y))
						drawable.drawBlack(dst);
					else
						drawable.drawFog(dst);
					
				}
			}	
		}
	}
	void updateVisibility(IModel a_model) {
		if (shouldUpdateVisibility ) {
			CharacterIterable soldiers = a_model.getAliveSoldiers();
			
			int steps = Preferences.VISIBILITY_STEPS;
			for (int dx = 0; dx < steps && visibilityProgressX + dx < Preferences.WIDTH; dx++) 
			{
				for (int y = 0; y < Preferences.HEIGHT; y++) {
					isTileVisible(a_model, soldiers, visibilityProgressX + dx, y);
				}
			}
			
			visibilityProgressX += steps;
			
			if (doneUpdating()) {
				for (int x = 0; x < Preferences.WIDTH; x++) {
					for (int y = 0; y < Preferences.HEIGHT; y++) {
						if (isWallOrDoor(a_model, x, y)) {
							ifNeighbourVisible(x, y, a_model);
						}
					}
				}
				
				for (int x = 0; x < Preferences.WIDTH; x++) {
					for (int y = 0; y < Preferences.HEIGHT; y++) {
						visibilityMap[x][y] = calculationMap[x][y];
					}
				}
				shouldUpdateVisibility = false;
			}
		}
	}

	boolean doneUpdating() {
		return visibilityProgressX >= Preferences.WIDTH;
	}
	
	void recalculateVisibility(IModel a_model) {
		shouldUpdateVisibility = true;
		visibilityProgressX = 0;
	}

	private void isTileVisible(IModel a_model, CharacterIterable soldiers,
			int x, int y) {
		
		if (isWallOrDoor(a_model, x, y) == false) {
			if (calculationMap[x][y] != Visibility.NeverSeen)
				calculationMap[x][y] = Visibility.NotSeen;
		
			for (ICharacter s : soldiers) {
				if (calculationMap[x][y] != Visibility.SeenByAll) {
					if (a_model.canSeeMapPosition(s, new ModelPosition(x, y))) {
						calculationMap[x][y] = Visibility.SeenByAll;
						break;
					}
				}
			}
		}
	}

	private boolean isWallOrDoor(IModel level, int x, int y) {
		return level.getTile(x, y) == TileType.TileWall || level.getTile(x, y) == TileType.TileDoor;
	}

	private void ifNeighbourVisible(int x, int y, IModel level) {
		for (int dx = -1; dx < 2; dx++) {
			for (int dy = -1; dy < 2; dy++) {
				if (isWallOrDoor(level, x +dx, y + dy) == false) {
					if (getVisibility(x + dx, y + dy) == Visibility.SeenByAll) {
						calculationMap[x][y] = Visibility.SeenByAll;
					}
				}
			}
		}
	}
	
	private Visibility getVisibility(int x, int y) {
		if (x < 0 || y < 0 || x >= Preferences.WIDTH || y >= Preferences.HEIGHT) {
			return Visibility.NeverSeen;
		}
		return calculationMap[x][y];
	}

	

	private boolean neverSeen(int x, int y) {
		return visibilityMap[x][y] == Visibility.NeverSeen;
	}

	private boolean canSee(int x, int y) {
		return visibilityMap[x][y] == Visibility.SeenByAll;
	}

	//TODO: Copied from Level.load
	public void Load(IPersistance persistence) throws Exception {
		String level = persistence.getString("Visibility");
		int index = 0;
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				 int enumIndex = level.charAt(index)-'0';
				 calculationMap[x][y] = Visibility.values()[enumIndex];
				 index++;
			}
		}		
	}

	public void Save(IPersistance persistence) {
		StringBuilder strbuff = new StringBuilder();
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				int ordinal = calculationMap[x][y].ordinal();
				strbuff.append(ordinal);
			}
		}
		String data = strbuff.toString();
		persistence.putString("Visibility", data);
	}

// TODO from UCDetector: Method "VisibilityView.hasSameExploredVisibility(VisibilityView)" is only called from tests
	public boolean hasSameExploredVisibility(VisibilityView pre) { // NO_UCD (test only)
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				if (calculationMap[x][y].hasSameExploredVisibility(pre.calculationMap[x][y]) == false) {
					return false;
				}
			}
		}
		return true;
	}
	
}

