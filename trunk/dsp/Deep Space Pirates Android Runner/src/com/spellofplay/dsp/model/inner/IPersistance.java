package com.spellofplay.dsp.model.inner;

public interface IPersistance {

	int getInt(String string);

	String getString(String string) throws Exception;

	void putInt(String string, int currentLevel);

	void putString(String string, String savedSoldier);

}
