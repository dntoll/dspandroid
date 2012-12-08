package com.spellofplay.dsp.model.inner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.spellofplay.dsp.model.CharacterIterable;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.IMoveAndVisibility;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.MultiMovementListeners;

public class CharacterCollection<T extends Character> implements Iterable<T>{

	private List<T> characters;
	
	CharacterCollection(List<T> characters) {
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

	public CharacterCollection<T> thatCanSee(IMoveAndVisibility visibility, ICharacter target) {
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
	
	
	public CharacterIterable getSafeIterator() {
		return new CharacterIterable(characters);
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



	void startNewRound() {
		for (T s : characters) {
			s.startNewRound();
		}
	}



	boolean occupies(ModelPosition pos) {
		for (T s : characters) {
			if (s.getPosition().equals(pos))  {
				return true;
			}
		}
		return false;
	}
	
	void remove(ModelPosition to) {
		for (T character : characters) {
			if (character.getPosition().equals(to))  {
				characters.remove(character);
				return;
			}
		}
	}



	boolean blocks(Line line) {
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

	public CharacterCollection<T> couldShootIfHadTime(ICharacter enemy, IMoveAndVisibility a_moveAndVisibility) {
		List<T> soldiersThatCanSee = new ArrayList<T>();
		for(T soldier : characters) {
			
			if (RuleBook.couldFireIfHadTime(soldier, enemy, a_moveAndVisibility)) {
				soldiersThatCanSee.add(soldier);
			}
		}
		return new CharacterCollection<T>(soldiersThatCanSee);
	}

	public CharacterCollection<T> canBeShotBy(ICharacter selectedSoldier, IMoveAndVisibility a_moveAndVisibility) {
		
		List<T> charactersThatCanBeShot = new ArrayList<T>();
		for(T character : characters) {
			
			if (RuleBook.canFireAt(selectedSoldier, character, a_moveAndVisibility)) {
				charactersThatCanBeShot.add(character);
			}
		}
		return new CharacterCollection<T>(charactersThatCanBeShot);
	}

	CharacterCollection<T> selectThoseThatCanSeenBy(CharacterCollection<Soldier> observers, IMoveAndVisibility visibility) {
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

	boolean containsAll(CharacterCollection<T> observedEnemies) {
		return  characters.containsAll(observedEnemies.characters);
	}
	
	void addAll(CharacterCollection<T> observedEnemies) {
		characters.addAll(observedEnemies.characters);
	}
	
	void stopAllMovement() {
		for(T mover : characters) {
			mover.stopAllMovement();
		}
	}

	


}