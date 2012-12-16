package com.spellofplay.dsp.model.inner;

import java.util.HashMap;
import java.util.Map;

public class PersistanceStub implements IPersistance {

	Map<String, Integer> integers = new HashMap<String, Integer>();
	Map<String, String> strings = new HashMap<String, String>();
	
	@Override
	public int getInt(String key) {
		return integers.get(key).intValue();
	}

	@Override
	public String getString(String key) throws Exception {
		
		if (strings.containsKey(key))
			return strings.get(key);
		else
			throw new Exception("Key " + key + " not found " + dumpStrings());
	}

	private String dumpStrings() {
		StringBuilder ret = new StringBuilder();
		
		for (String key : strings.keySet()) {
			ret.append(key);
			ret.append(", ");
		}
		
		return ret.toString();
	}

	@Override
	public void putInt(String key, int value) {
		integers.put(key, value);
		
	}

	@Override
	public void putString(String key, String value) {
		strings.put(key, value);
	}

}
