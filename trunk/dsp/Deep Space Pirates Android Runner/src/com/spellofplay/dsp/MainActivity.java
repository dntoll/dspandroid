package com.spellofplay.dsp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class MainActivity  extends Activity {
	Application m_application;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	m_application = new Application(getApplicationContext(), this);
    	
        super.onCreate(savedInstanceState);
        
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.main);
        
        setContentView(m_application);
        
    }
    
    @Override
    public void onPause() {
    	//
    	m_application.Stop();
    	//m_master = null;
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	if (m_application != null && m_application.m_sleepHandler != null) {
    		m_application.Resume();
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