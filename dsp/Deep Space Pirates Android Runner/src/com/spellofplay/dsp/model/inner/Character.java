package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.Experience;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ICharacterListener;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.RuleBook;

abstract class Character implements ICharacter  {
	private PathFinder pathFinder = new PathFinder();
	
	private ModelPosition position = new ModelPosition();
	protected SkillSet skills;
	private int watchTimeUnits = 0;
	protected int timeUnits;
	protected int hitPoints;
	protected Experience experience = new Experience();

	protected CharacterType type;
	
	Character(ModelPosition startPosition, CharacterType type) {
		skills = new SkillSet(type.getSkillArray());
		this.type = type;
		reset(startPosition);
	}
	
	public CharacterType getCharacterType() {
		return type;
	}
	
	
	public boolean equals(Character other) {
		if (position.equals(other.position) == false) {
			return false;
		}
		
		if (timeUnits != other.timeUnits)
			return false;
		
		if (hitPoints != other.hitPoints)
			return false;
		
		if (experience.experience != other.experience.experience)
			return false;
		
		if (skills.equals(other.skills) == false)
			return false;
		
		
		return true;
		
		
	}
	
	void doWatch() {
		watchTimeUnits = timeUnits;
		timeUnits = 0;
	}
	
	void reset(ModelPosition startLocation) {
		position = startLocation;
		startNewRound();
		hitPoints = skills.getHitPoints();
		experience = new Experience();
	}
	
	void startNewRound() {
		timeUnits = skills.getTimeUnits();
		watchTimeUnits = 0;
		pathFinder.stopAllSearches();
	}
	
	public ModelPosition getPosition() {
		return position;
	}
	
	public boolean canSpendExperience() {
		return skills.canSpendExperience(experience);
	}

	public float getRadius() {
		return 0.5f;
	}
	
	
	
	public SkillSet getSkills() {
		return skills;
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
		
	void fireAt(ICharacter fireTarget, IMoveAndVisibility moveAndVisibility, ICharacterListener a_listener) {
		if (RuleBook.canFireAt(this, fireTarget, moveAndVisibility) == false) {
			a_listener.cannotFireAt(this, fireTarget);
			return;
		}
		timeUnits -= getFireCost();
		
		if (RuleBook.DetermineFireSuccess(this, fireTarget, moveAndVisibility.targetHasCover(this, fireTarget))) {
			Character target = (Character)fireTarget;
			a_listener.fireAt(this, fireTarget, true);
			target.takeDamage(a_listener, getDamage());
			experience.experience++;
		} else {
			a_listener.fireAt(this, fireTarget, false);
		}
	}
	
	void takeDamage(ICharacterListener a_listener, int damage) {
		hitPoints -= damage;
		a_listener.takeDamage(this);
		
	}

	protected abstract int getDamage();

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

	@Override
	public Experience getExperience() {
		return experience;
	}

	public String SaveToString() {
		StringBuilder soldier = new StringBuilder();
		
		soldier.append(position.x);
		soldier.append(":");
		soldier.append(position.y);
		soldier.append(":");
		soldier.append(watchTimeUnits);
		soldier.append(":");
		soldier.append(timeUnits);
		soldier.append(":");
		soldier.append(hitPoints);
		soldier.append(":");
		soldier.append(experience.experience);
		soldier.append(":");
		soldier.append(skills.getCommaSeparatedString());
	
		return soldier.toString();
	}
	
	public void LoadFromString(String savedSoldier) {
		if (savedSoldier == "")
			return;
		try {
			String[] parts = savedSoldier.split(":");
			position.x = Integer.parseInt(parts[0]);
			position.y = Integer.parseInt(parts[1]);
			watchTimeUnits = Integer.parseInt(parts[2]);
			timeUnits = Integer.parseInt(parts[3]);
			hitPoints = Integer.parseInt(parts[4]);
			experience.experience = Integer.parseInt(parts[5]);
			String skills = parts[6];
			
			this.skills.fromCommaSeparatedString(skills);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw e;
		}
	}
	
}
