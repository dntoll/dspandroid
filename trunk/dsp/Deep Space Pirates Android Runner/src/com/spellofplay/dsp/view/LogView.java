package com.spellofplay.dsp.view;

import java.util.ArrayList;
import java.util.List;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;

import android.graphics.Color;
import android.graphics.Rect;

public class LogView implements ICharacterListener {

	private List<String> m_log = new ArrayList<String>();
	private String m_logMessage = "";
	
	public void draw(AndroidDraw drawable) {
		
		int top = (int)((float)drawable.getWindowHeight() * 3.0f / 4.0f);
		Rect dst = new Rect(0, top, drawable.getWindowWidth() / 2, drawable.getWindowHeight());
		drawable.drawRect(dst, Color.BLACK);
		drawable.m_guiText.setColor(Color.WHITE);
		drawable.drawText("Log " + m_log.size() + " " + m_logMessage, 20, top + 32 , drawable.m_guiText);
		
		int item = 0;
		for (String s : m_log) {
			int place = m_log.size() - item;
			drawable.drawText(s, 20, top + 32 +place*24 , drawable.m_guiText);	
			item++;
		}
	}

	@Override
	public void moveTo(ICharacter character) {
		m_log.add("character moved..." );
		
	}

	@Override
	public void fireAt(ICharacter attacker, ICharacter fireTarget, boolean didHit) {
		m_log.add("character attacked and " + (didHit ? "did hit" : "missed"));
		
		if (didHit && fireTarget.getHitPoints() == 0) {
			m_log.add("target eliminated");	
		}
	}

	public void doLog(String string) {
		m_log.add(string );
	}

	@Override
	public void cannotFireAt(ICharacter character, ICharacter fireTarget) {
		m_log.add("character could not fire at");
		
	}

	@Override
	public void enemyAILog(String string, ICharacter enemy) {
		m_logMessage = string;
		m_log.add(string + enemy.getPosition().m_x + ":" + enemy.getPosition().m_y );
	}
}
