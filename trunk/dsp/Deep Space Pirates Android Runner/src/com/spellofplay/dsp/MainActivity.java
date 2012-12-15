package com.spellofplay.dsp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

@SuppressWarnings("ucd")
public class MainActivity  extends Activity {
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	Application m_application;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	m_application = new Application(getApplicationContext(), this);
    	
        super.onCreate(savedInstanceState);
        
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(m_application);
        
    }
    
    @Override
    public void onPause() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	m_application.Stop(settings);
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	if (m_application != null && m_application.m_sleepHandler != null) {
    		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    		m_application.Resume(settings);
    	}
    	super.onResume();
    }
    
    @Override
    public boolean onKeyDown(int a_keyCode, KeyEvent event) {
    	return m_application.onKeyDown(a_keyCode, event);
    }
    @Override
    public boolean onKeyUp(int a_keyCode, KeyEvent event) {
    	return m_application.onKeyUp(a_keyCode, event);
    }
    
    
    
    
}