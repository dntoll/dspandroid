package com.spellofplay.dsp.controller;

import android.graphics.Color;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IEventTarget;
import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ISkill;
import com.spellofplay.dsp.model.ISkillSet;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.Input;
import com.spellofplay.dsp.view.SimpleGui;

class LevelUpController {

	private IModel model;
	private IEventTarget eventTarget;
	private Input input;
	private SimpleGui gui;
	
	
	
	LevelUpController(IModel model, IEventTarget eventTarget,
			Input input, SimpleGui gui) {
		this.model = model;
		this.eventTarget = eventTarget;
		this.input = input;
		this.gui = gui;
	
		
	}

	boolean doLevelUp(AndroidDraw drawable) {
		
		for ( ICharacter soldier : model.getAliveSoldiers()) {
			if (soldier.canSpendExperience()) {
				doSpendExperience(soldier, drawable);
				return false;
			}
		}
		
		return true;
	}

	private void doSpendExperience(ICharacter soldier, AndroidDraw drawable) {
		int halfWidth = drawable.getWindowWidth()/2;
		int halfHeight = drawable.getWindowHeight()/2;
		
		
		drawable.drawText("Level up", 16, halfHeight - 32, Color.WHITE);

		ISkillSet skills = soldier.getSkills();
		
		for (ISkillSet.SkillType type : ISkillSet.SkillType.values()) {
		
			ISkill skill = skills.getSkill(type);
			drawable.drawText(type.toString() + skill.getValue(), 96, halfHeight , Color.BLACK);
			
			if (skill.canImprove(soldier.getExperience()))
			if (gui.DoButtonCentered(halfWidth, halfHeight, "+", input)) {
				eventTarget.spendExperience(soldier, type);
			}
			
			halfHeight += 32;
		}
	}

}
