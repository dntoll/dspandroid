package com.spellofplay.dsp.controller;

import android.graphics.Paint;

import com.spellofplay.common.view.Input;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IEventTarget;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.SimpleGui;

public class LevelUpController {

	IModel model;
	IEventTarget eventTarget;
	Input input;
	SimpleGui gui;
	Paint guiText;
	
	
	public LevelUpController(IModel model, IEventTarget eventTarget,
			Input input, SimpleGui gui) {
		this.model = model;
		this.eventTarget = eventTarget;
		this.input = input;
		this.gui = gui;
		guiText = new Paint();
		
	}

	public boolean doLevelUp(AndroidDraw drawable) {
		
		for ( ICharacter soldier : model.getAliveSoldiers()) {
			if (soldier.hasExperience()) {
				
				doSpendExperience(soldier, drawable);
				
				return false;
			}
		}
		
		return true;
	}

	private void doSpendExperience(ICharacter soldier, AndroidDraw drawable) {
		int halfWidth = drawable.getWindowWidth()/2;
		int halfHeight = drawable.getWindowHeight()/2;
		
		
		drawable.drawText("Level up", 16, halfHeight - 32, guiText);
		
		drawable.drawText("Time Units " + soldier.getMaxTimeUnits(), 16, halfHeight, guiText);
		if (gui.DoButtonCentered(halfWidth, halfHeight, "Time Units", input)) {
			eventTarget.addTimeUnits(soldier);
		}
		drawable.drawText("Fire Skill " + soldier.getFireSkill(), 16, halfHeight += SimpleGui.BUTTON_HEIGHT, guiText);
		if (gui.DoButtonCentered(halfWidth, halfHeight , "shoot skill", input)) {
			eventTarget.addShootSkill(soldier);
		}
		drawable.drawText("Dodge Skill " + soldier.getDodgeSkill(), 16, halfHeight += SimpleGui.BUTTON_HEIGHT, guiText);
		if (gui.DoButtonCentered(halfWidth, halfHeight , "dodge skill", input)) {
			eventTarget.addDodgeSkill(soldier);
		}
		
		
	}

}
