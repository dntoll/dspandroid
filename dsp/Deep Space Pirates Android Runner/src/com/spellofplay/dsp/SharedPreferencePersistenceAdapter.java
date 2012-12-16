package com.spellofplay.dsp;

import android.content.SharedPreferences;

import com.spellofplay.dsp.model.inner.IPersistance;

public class SharedPreferencePersistenceAdapter implements IPersistance {

	SharedPreferences persistence;
	SharedPreferences.Editor editor;
	public SharedPreferencePersistenceAdapter(SharedPreferences settings) {
		persistence = settings;
		editor = persistence.edit();
	}

	@Override
	public int getInt(String key) {
		
		return persistence.getInt(key, 0);
	}

	@Override
	public String getString(String key) {
		return persistence.getString(key, "");
	}

	@Override
	public void putInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	@Override
	public void putString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}
	


}
