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
		
		int top = 0;
		Rect dst = new Rect((int)((float)drawable.getWindowWidth() * 0.8f), top, drawable.getWindowWidth(), (int)((float)drawable.getWindowHeight()*0.6f));
		drawable.drawRect(dst, Color.argb(128, 0, 0, 0));
		
		drawable.drawText("Log " + m_log.size() + " " + m_logMessage, 20, top + 32, Color.WHITE);
		
		int item = 0;
		for (String s : m_log) {
			int place = m_log.size() - item;
			drawable.drawText(s, dst.left+8, top + 32 +place*24 , Color.WHITE);	
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
		m_log.add(string + enemy.getPosition().x + ":" + enemy.getPosition().y );
	}

	@Override
	public void enemySpotsNewSoldier() {
		m_log.add("Enemy spotted soldier");
	}

	@Override
	public void soldierSpotsNewEnemy() {
		m_log.add("Soldier spotted enemy");
	}

	@Override
	public void takeDamage(ICharacter character) {
		m_log.add("character took damage");
	}
}
