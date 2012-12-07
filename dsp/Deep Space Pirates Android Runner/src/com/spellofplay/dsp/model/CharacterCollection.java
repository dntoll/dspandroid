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

	public CharacterCollection<T> thatCanSee(IMoveAndVisibility visibility, Character target) {
		List<T> soldiersThatCanSee = new ArrayList<T>();
		
		for (T character : characters) {
			if ( visibility.lineOfSight(character, target) ) {
				soldiersThatCanSee.add(character);
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
		for (T character : characters) {
			if (character.hasWatch()) {
				multiListener.addListener( character );
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
	
	public void remove(ModelPosition to) {
		for (T character : characters) {
			if (character.getPosition().equals(to))  {
				characters.remove(character);
				return;
			}
		}
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
	




public CharacterCollection<T> selectThoseThatCanSeenBy(CharacterCollection<Soldier> observers, IMoveAndVisibility visibility) {
		List<T> charactersSeen = new ArrayList<T>();
		
		for(Soldier observer : observers) {
			for(T theObserved : characters) {
				
				if (visibility.lineOfSight(observer, theObserved)) {
					charactersSeen.add(theObserved);
				}
			}
			
		}
		return new CharacterCollection<T>(charactersSeen);
	}



public boolean containsAll(CharacterCollection<T> observedEnemies) {
	return  characters.containsAll(observedEnemies.characters);
}



public void addAll(CharacterCollection<T> observedEnemies) {
	characters.addAll(observedEnemies.characters);
}



public void stopAllMovement() {
	for(T mover : characters) {
		mover.getPathFinder().stopAllSearches();
	}
}


}
