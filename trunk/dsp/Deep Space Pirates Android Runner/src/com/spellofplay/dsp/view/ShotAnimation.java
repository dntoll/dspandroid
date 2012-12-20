package com.spellofplay.dsp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.spellofplay.dsp.model.Vector2;

class ShotAnimation {

	private List<Shot> activeShots = new ArrayList<Shot>();
	
	
	public void draw(AndroidDraw drawable, Camera cam) {
		for(Shot s : activeShots) {
			if (s.isActive()) {
				s.draw(drawable, cam);
			}
		}
	}

	void addHit(Vector2 attacker, VisualCharacter targetPos, Random rand, float delay) {
		activeShots.add(new Shot(attacker, targetPos, true, rand, delay) );
	}

	void addMiss(Vector2 attacker, VisualCharacter fireTarget, Random rand, float delay) {
		activeShots.add(new Shot(attacker, fireTarget, false, rand, delay) );
	}

	public void update(float a_elapsedTime) {
		
		for(Shot s : activeShots) {
			s.update(a_elapsedTime);
		}
	}
	
	public boolean isActive() {
		for(Shot s : activeShots) {
			if (s.isActive()) {
				return true;
			}
		}
		
		activeShots.clear();
		return false;
	}

	void removeAnimations() {
		activeShots.clear();
	}

}
