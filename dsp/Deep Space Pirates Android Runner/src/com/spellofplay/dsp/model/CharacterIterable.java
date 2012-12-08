package com.spellofplay.dsp.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CharacterIterable implements Iterable<ICharacter> {
	
	List<ICharacter> characters = new ArrayList<ICharacter>();
	//CharacterIterator iterator = new CharacterIterator();
	
	public <T extends ICharacter> CharacterIterable(List<T> in) {
		characters.addAll(in);
	}

	@Override
	public Iterator<ICharacter> iterator() {
		return characters.iterator();
	}
	

	public boolean isEmpty() {
		return characters.isEmpty();
	}

	
	
}
