package com.spellofplay.dsp.view;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Preferences;

class MovementMapView {
	private int[][] m_movementMap;
	private ICharacter m_soldierForWhomMapIsValid;
	private boolean m_mustUpdate;
	
	public MovementMapView() {
		m_movementMap  = new int[Preferences.WIDTH][];
		for (int x = 0; x< Preferences.WIDTH; x++) {
			m_movementMap[x] = new int[Preferences.HEIGHT];
		}
		
		resetMap();
	}
	
	
	
	public void update() {
		m_mustUpdate = true;
	}
	
	private void updateMoveMap(IMoveAndVisibility a_checker, ICharacter selected) {
		m_soldierForWhomMapIsValid = selected;
		m_mustUpdate = false;
		
		resetMap();
		
		if (selected != null) {
			m_movementMap[selected.getPosition().x][selected.getPosition().y] = 0;
			boolean hasAddedNewNodes = true;
			while (hasAddedNewNodes) {
				hasAddedNewNodes = addNewNodes(a_checker, selected);
			}
		}
	}



	private boolean addNewNodes(IMoveAndVisibility a_checker, ICharacter selected) {
		boolean hasAddedNewNodes;
		hasAddedNewNodes = false;
		
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
			
				boolean canBeExpanded = m_movementMap[x][y] < selected.getTimeUnits();
				if (canBeExpanded) {
					hasAddedNewNodes = checkTheNeighbours(a_checker, hasAddedNewNodes, x, y);
				}
			}
		}
		return hasAddedNewNodes;
	}



	private void resetMap() {
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				m_movementMap[x][y] = 1000;
			}
		}
	}
	
	private boolean checkTheNeighbours(IMoveAndVisibility a_checker,
			boolean hasAddedNewNodes, int x, int y) {
		
		int travelCost = m_movementMap[x][y] + 1;
		for (int dx = -1; dx < 2; dx++) {
			for (int dy = -1; dy < 2; dy++) { 
				if (validNeighbour(a_checker, x, y, dx, dy) == false)
					continue;
				
				//har vi redan en h�gre movementcost d�r ?
				if (m_movementMap[x+dx][y+dy] > travelCost ) {
					m_movementMap[x+dx][y+dy] = travelCost;
					hasAddedNewNodes = true;
				}
			}
		}
		return hasAddedNewNodes;
	}
	
	private boolean validNeighbour(IMoveAndVisibility a_checker, int x, int y, int dx, int dy) {
		if (dx == 0 && dy == 0) {
			return false;
		}
		//Stay inside level
		if (x+dx >= Preferences.WIDTH || y+dy >= Preferences.HEIGHT) {
			return false;
		}
		if (x+dx < 0 || y+dy < 0) {
			return false;
		}
		

		if (a_checker.isMovePossible(new ModelPosition(x+dx, y+dy)) == false) {
			return false;
		}
		
		//Diagonala moves
		if (dx == dy || dx == -dy) {
	        if (a_checker.isMovePossible(new ModelPosition(x + dx, y)) == false)
	        	return false;
	        if (a_checker.isMovePossible(new ModelPosition(x, y + dy)) == false)
	        	return false;
        }
		return true;
	}
	
	
	void drawPossibleMoveArea(IMoveAndVisibility a_checker, AndroidDraw drawable, Camera camera, ICharacter selected) {
		if (selected != m_soldierForWhomMapIsValid) {
			m_mustUpdate = true;
		}
		
		if (m_mustUpdate)
			updateMoveMap(a_checker, selected);
		
		if (selected == null)
			return;
		
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				drawTileMoveHint(drawable, camera, selected, x, y);
			}	
		}
	}
	private void drawTileMoveHint(AndroidDraw drawable, Camera camera, ICharacter selected, int x, int y) {
		if (m_movementMap[x][y] <= selected.getTimeUnits()) {
			ViewPosition vp = camera.toViewPos(x, y);
			Rect dst = camera.toRect(vp);
		
			if (m_movementMap[x][y] <= selected.getTimeUnits() - selected.getFireCost())	
				drawable.drawRect(dst, Color.argb(48, 0, 255, 0));
			else
				drawable.drawRect(dst, Color.argb(32, 0, 255, 128));
			
			drawable.drawText("" + m_movementMap[x][y], (int)vp.m_x, (int)vp.m_y);
		}
	}

	
}
