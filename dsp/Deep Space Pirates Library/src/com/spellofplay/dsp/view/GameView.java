package com.spellofplay.dsp.view;


import java.util.List;

import android.graphics.PointF;
import android.graphics.Rect;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;

public class GameView implements IGameView {

	
	
	LevelDrawer m_level;// = new LevelDrawer();
	boolean hasInitatedBuffer = false;
	Camera m_camera = new Camera();
	Soldier m_selectedSoldier;
	
	ITexture m_texture;
	public GameView(ITexture a_texture) {
		m_level = new LevelDrawer(a_texture);
		m_texture = a_texture;
	}
	
	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.view.IGameView#drawGame(com.spellofplay.dsp.view.IDraw, com.spellofplay.dsp.model.IModel)
	 */
	@Override
	public void drawGame(IDraw drawable, IModel a_model) {
		if (hasInitatedBuffer == false) {
			m_level.draw(a_model.getLevel(), drawable);
			hasInitatedBuffer = true;
		}
		drawable.drawBackground();
		
		Rect src = new Rect(0, 
				128,
				0 + 32, 
				128 + 32);
		
		for (Soldier s : a_model.getAliveSoldiers()) {
			ViewPosition vpos = m_camera.toViewPos(s.getPosition());
			
			Rect dst = new Rect((int)vpos.m_x, 
					(int)vpos.m_y,
					(int)vpos.m_x + 32, 
					(int)vpos.m_y + 32);
			
			drawable.drawBitmap(m_texture, src, dst);
		}
		
		drawable.drawText(a_model.getGameTitle(), 10, 10);
	}
	
	
	enum Action {
		None,
		SelectedSoldier,
		ClickedOnGround
	}
	
	Action m_action = Action.None;
	
	@Override
	public void setupInput(IInput a_input, IModel a_model) {
		
		m_action = Action.None;
		
		//On soldier?
		Soldier mouseOverSoldier = onSoldier(a_input, a_model);
		
		if (mouseOverSoldier != null) {
			if (a_input.IsMouseClicked()) {
				m_selectedSoldier = mouseOverSoldier;
				m_action = Action.SelectedSoldier;
			}
		}
		
		if (m_action == Action.None && a_input.IsMouseClicked()) {
			 m_action = Action.ClickedOnGround;
		}
		
		
	}
	
	private Soldier onSoldier(IInput a_input, IModel a_model) {
		List<Soldier> soldiers = a_model.getAliveSoldiers();
		ViewPosition clickpos = a_input.getClickPosition();
		for (Soldier s : soldiers) {
			ModelPosition soldierModelPos = s.getPosition();
			
			ViewPosition viewPosition = m_camera.toViewPos(soldierModelPos);
			
			float soldierViewRadius = m_camera.toViewScale(s.getRadius());
			
			
			
			if (viewPosition.sub(clickpos).length() < soldierViewRadius) {
				
				if (a_input.IsMouseClicked()) {
					return s;
				}
			}
		}
		return null;
	}
	
	

	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.view.IGameView#getSelectedSoldier()
	 */
	@Override
	public Soldier getSelectedSoldier(IInput a_input, IModel a_model) {
		return m_selectedSoldier;
	}

	/* (non-Javadoc)
	 * @see com.spellofplay.dsp.view.IGameView#getDestination()
	 */
	@Override
	public ModelPosition getDestination(IInput a_input) {
		if (m_action == Action.ClickedOnGround) {
			
			ViewPosition mousePos = a_input.getClickPosition();
			
			return m_camera.toModelPos(mousePos);
		}
		return null;
	}

	

}
