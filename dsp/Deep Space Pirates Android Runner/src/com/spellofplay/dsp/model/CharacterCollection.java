package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CharacterCollection<T extends Character> implements Iterable<T>{

	List<T> characters;
	public CharacterCollection(List<T> characters) {
		this.characters = characters;
	}

	

	public int size() {
		return characters.size();
	}
	
	public boolean isTime() {
		for (T s : characters) {
			if (s.getTimeUnits() > 0) {
				return true;
			}
		}
		return false;
	}

	public CharacterCollection<T> thatCanSee(IMoveAndVisibility visibility, Enemy enemy) {
		List<T> soldiersThatCanSee = new ArrayList<T>();
		
		for (T soldier : characters) {
			if ( visibility.lineOfSight(soldier, enemy) ) {
				soldiersThatCanSee.add(soldier);
			}
		}
		return new CharacterCollection<T>(soldiersThatCanSee);
	}



	@Override
	public Iterator<T> iterator() {
		return characters.iterator();
	}



	public MultiMovementListeners getMovementListeners() {
		MultiMovementListeners multiListener = new MultiMovementListeners();
		for (T soldier : characters) {
			if (soldier.hasWatch()) {
				multiListener.addListener( soldier );
			}
		}
		return multiListener;
	}



	public void startNewRound() {
		for (T s : characters) {
			s.startNewRound();
		}
	}



	public boolean occupies(ModelPosition pos) {
		for (T s : characters) {
			if (s.getPosition().equals(pos))  {
				return true;
			}
		}
		return false;
	}



	public void remove(Character character) {
		characters.remove(character);
	}



	public boolean blocks(Line line) {
		for (T s : characters) {
			if (line.distance(s.getPosition().toCenterTileVector()) < s.getRadius() * 0.8f) { 
				return true;
			}
		}
		return false;
	}



	public T getClosest(ModelPosition position) {
		float closestDistance = Float.MAX_VALUE;
		T closest = null;
		for (T s : characters) {
			float distance  = s.getPosition().sub(position).length();
			if (distance < closestDistance) {
				closestDistance = distance;
				closest = s;
			}
		}
				
		return closest;
	}



	public boolean isEmpty() {
		return size() == 0;
	}



	public CharacterCollection<T> canShoot(Character enemy, IMoveAndVisibility a_moveAndVisibility) {
		List<T> soldiersThatCanSee = new ArrayList<T>();
		for(T soldier : characters) {
			
			if (RuleBook.canFireAt(soldier, enemy, a_moveAndVisibility)) {
				soldiersThatCanSee.add(soldier);
			}
		}
		return new CharacterCollection<T>(soldiersThatCanSee);
	}



	public CharacterCollection<T> canBeShotBy(Character selectedSoldier, IMoveAndVisibility a_moveAndVisibility) {
		
		List<T> charactersThatCanBeShot = new ArrayList<T>();
		for(T character : characters) {
			
			if (RuleBook.canFireAt(selectedSoldier, character, a_moveAndVisibility)) {
				charactersThatCanBeShot.add(character);
			}
		}
		return new CharacterCollection<T>(charactersThatCanBeShot);
	}

}
