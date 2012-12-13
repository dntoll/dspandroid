package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.RuleBook;

class Character implements ICharacter  {
	private ModelPosition position = new ModelPosition();
	
	private PathFinder pathFinder = new PathFinder();
	
	protected SkillSet skills;
	
	private int watchTimeUnits = 0;
	protected int timeUnits;
	protected int hitPoints;
	protected Experience experience = new Experience();
	
	Character(ModelPosition startPosition, SkillSet skillSet) {
		skills = skillSet;
		reset(startPosition);
	}
	
	void doWatch() {
		watchTimeUnits = timeUnits;
		timeUnits = 0;
	}
	
	void reset(ModelPosition startLocation) {
		position = startLocation;
		startNewRound();
		hitPoints = skills.getHitPoints();
	}
	
	void startNewRound() {
		timeUnits = skills.getTimeUnits();
		watchTimeUnits = 0;
		pathFinder.stopAllSearches();
	}
	
	public ModelPosition getPosition() {
		return position;
	}
	
	public boolean hasExperience() {
		return experience.experience > 0;
	}

	public float getRadius() {
		return 0.5f;
	}
	
	public int getFireCost() {
		return 3;
	}
	
	public int getDamage() {
		return 1;
	}
	
	public SkillSet getSkills() {
		return skills;
	}
	
	@Override
	public float getRange() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getHitPoints() {
		return hitPoints;
	}

	public int getTimeUnits() {
		return timeUnits;
	}
	public int getWatchTimeUnits() {
		return watchTimeUnits;
	}
	
	public boolean hasWatch() {
		return watchTimeUnits >= getFireCost();
	}
	
	void setDestination(ModelPosition destination, IMoveAndVisibility map, float distance) {
		pathFinder.setDestination(map, position, destination, distance > 0.0f, distance);
	}
	
	boolean canMove() {
		if (timeUnits > 0) {
			if (pathFinder.isSearchDone()) {
				return true;
			}
		}
		return false;
	}
	
	ModelPosition getMovePosition() {
		return pathFinder.getNextPosition();
	}
	
	
	void move(ModelPosition destination, ICharacterListener clistener) {
		
		position = destination;
		timeUnits--;
		clistener.moveTo(this);
	}
		
	boolean fireAt(ICharacter fireTarget, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		if (RuleBook.canFireAt(this, fireTarget, moveAndVisibility) == false) {
			a_listener.cannotFireAt(this, fireTarget);
			return false;
		}
		timeUnits -= getFireCost();
		
		if (RuleBook.DetermineFireSuccess(this, fireTarget, moveAndVisibility.targetHasCover(this, fireTarget))) {
			Character target = (Character)fireTarget;
			target.hitPoints -= getDamage();
			a_listener.fireAt(this, fireTarget, true);
			experience.experience++;
			return true;
		} else {
			a_listener.fireAt(this, fireTarget, false);
			return false;
		}
		
	}

	public float distance(ICharacter other) {
		return position.sub(other.getPosition()).length();
	}

	void watchMovement(ICharacter mover,	IMoveAndVisibility moveAndVisibility, ICharacterListener listener) {
		
		if (RuleBook.canFireAt(this, mover, moveAndVisibility) == true) {
			fireAt(mover, moveAndVisibility, listener);
		}
	}

	public boolean hasTimeToFire() {
		return timeUnits + watchTimeUnits >= getFireCost(); 
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

	void stopAllMovement() {
		getPathFinder().stopAllSearches();
	}

	
	
}
