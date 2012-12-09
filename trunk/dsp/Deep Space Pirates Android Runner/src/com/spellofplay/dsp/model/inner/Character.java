package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.MultiMovementListeners;

public abstract class Character implements ICharacter  {
	private ModelPosition position = new ModelPosition();
	
	private PathFinder pathFinder = new PathFinder();
	
	private int maxTimeUnits = 3;
	private int watchTimeUnits = 0;
	protected int timeUnits = maxTimeUnits;
	protected int hitPoints = 5;

	Character(ModelPosition startPosition, int a_maxTimeUnits) {
		position.x = startPosition.x;
		position.y = startPosition.y;
		maxTimeUnits = timeUnits = a_maxTimeUnits;
	}
	
	public void doWatch() {
		watchTimeUnits = timeUnits;
		timeUnits = 0;
	}
	
	void reset(ModelPosition startLocation) {
		position = startLocation;
	}
	
	public ModelPosition getPosition() {
		return position;
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

	public void startNewRound() {
		timeUnits = maxTimeUnits;
		watchTimeUnits = 0;
		pathFinder.stopAllSearches();
	}
	
	public boolean hasWatch() {
		return watchTimeUnits >= getFireCost();
	}
	
	void setDestination(ModelPosition destination, IMoveAndVisibility map, float distance) {
		
		pathFinder.setDestination(map, position, destination, distance > 0.0f, distance);
		
	}

	void move(ICharacterListener clistener, MultiMovementListeners multiListener, IMoveAndVisibility moveAndVisibility) {
		
		if (timeUnits > 0) {
			if (pathFinder.isSearchDone()) {
				MoveToNextPosition(clistener, multiListener, moveAndVisibility);
			} 
		}
	}
	
	

	private void MoveToNextPosition(ICharacterListener clistener, MultiMovementListeners multiListener, IMoveAndVisibility moveAndVisibility) {
		
		ModelPosition pos = pathFinder.getNextPosition();
		
		if ( timeUnits <= 0) {
			return;
		}
		position = pos;
		timeUnits--;
		
		
		clistener.moveTo(this);
		multiListener.moveTo(this, moveAndVisibility, clistener);
	}
		
	public boolean fireAt(ICharacter fireTarget, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
				
		if (RuleBook.canFireAt(this, fireTarget, moveAndVisibility) == false) {
			a_listener.cannotFireAt(this, fireTarget);
			return false;
		}
		timeUnits -= getFireCost();
		
		if (RuleBook.DetermineFireSuccess(this, fireTarget, moveAndVisibility.targetHasCover(this, fireTarget))) {
			Character target = (Character)fireTarget;
			target.hitPoints -= getDamage();
			a_listener.fireAt(this, fireTarget, true);
			return true;
		} else {
			a_listener.fireAt(this, fireTarget, false);
			return false;
		}
		
	}

	public float distance(ICharacter other) {
		return position.sub(other.getPosition()).length();
	}

	public void watchMovement(ICharacter mover,
			IMoveAndVisibility moveAndVisibility, ICharacterListener listener) {
		
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

	public void stopAllMovement() {
		getPathFinder().stopAllSearches();
	}
	
}
