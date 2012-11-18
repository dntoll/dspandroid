package com.spellofplay.dsp.view;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;

public class VisualCharacter {
	float m_rotation = 0;
	static final Rect soldier = new Rect(0, 0, 255, 255);	
	
	ModelPosition m_lastPosition = new ModelPosition();
	
	public VisualCharacter(com.spellofplay.dsp.model.Character dude) {
		m_lastPosition.m_x = dude.getPosition().m_x;
		m_lastPosition.m_y = dude.getPosition().m_y;
	}

	void drawSoldier(AndroidDraw drawable, Soldier s, Camera camera, ITexture player, Enemy target ) {
		if (m_lastPosition.equals(s.getPosition()) == false) {
			m_rotation = m_lastPosition.sub(s.getPosition()).getRotation();
		} else if (target != null) {
			m_rotation = s.getPosition().sub(target.getPosition()).getRotation();
		}
		
		m_lastPosition = s.getPosition();
		
		ViewPosition vpos = camera.toViewPos(s.getPosition());
		
		Rect dst = new Rect((int)vpos.m_x -camera.getHalfScale(), 
				(int)vpos.m_y -camera.getHalfScale(),
				(int)vpos.m_x + camera.getHalfScale(), 
				(int)vpos.m_y + camera.getHalfScale());
		
		
		drawable.drawBitmap(player, soldier, dst, Color.WHITE, m_rotation);
		
		//DRAW SOLDIER INFO
		drawable.drawText("" + s.getTimeUnits(), dst.left, dst.top, drawable.m_guiText);
		drawable.drawText("" + s.getHitpoints(), dst.right, dst.top, drawable.m_guiText);
	}
}
