package com.spellofplay.dsp;


import com.spellofplay.dsp.controller.MasterController;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Application extends View   implements IUpdateable {
	
	MasterController m_master = null;//new MasterController(context)
	private Input m_input = new Input();
	
	private Activity m_activity;
	
	//TIMER
	long m_lastTime = 0; //THE time in millis of last frame
	public SleepHandler m_sleepHandler = new SleepHandler();
	
	
	public Application(Context context, Activity cfTimerActivity) {
        super(context);
        
        m_master = new MasterController(context, m_input);
        
        
        m_sleepHandler.sleep(this, 500);
        m_lastTime = System.currentTimeMillis();
        
        m_activity = cfTimerActivity;
        
   }
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		if (m_input.onTouchEvent(event))
			return true;
		return super.onTouchEvent(event);
	   
	}
	
	
	@Override
	public boolean onKeyUp(int a_keyCode, KeyEvent event) {
		if (m_input.onKeyUp(a_keyCode, event))
			return true;
		return super.onKeyUp(a_keyCode, event);
	}
	
	@Override
	public boolean onKeyPreIme(int a_keyCode, KeyEvent event) {
		if (m_input.onKeyPreIme(a_keyCode, event))
			return true;
		return super.onKeyPreIme(a_keyCode, event);
	}
	 
	
	@Override
	public boolean onKeyDown(int a_keyCode, KeyEvent event) {
		if (m_input.onKeyDown(a_keyCode, event))
			return true;
		return super.onKeyDown(a_keyCode, event);
	}
	
	@Override
	public boolean onKeyShortcut(int a_keyCode, KeyEvent event) {
		if (m_input.onKeyShortcut(a_keyCode, event))
			return true;
		return super.onKeyShortcut(a_keyCode, event);
	}
	
	public void update() {
		
		long now = System.currentTimeMillis();
        long elapsedTime = now - m_lastTime;
        float elapsedTimeSeconds = ((float)elapsedTime)/1000.0f;
        
        
		m_master.update(elapsedTimeSeconds);
		invalidate();
    	m_sleepHandler.sleep(this, 20);
    	m_lastTime = now;
	}
	
	@Override
    public void onDraw(Canvas canvas) {
    	
        super.onDraw(canvas);
        
        
        if (m_master.onDraw(new AndroidDraw(canvas)) == false ) {
        	m_activity.finish();
        }
	}
	
	public void Stop() {
		m_sleepHandler.m_stop = true;
		
		m_master.ShowMenu();
		
	}


	public void Resume() {
		m_sleepHandler.m_stop = false;
		m_sleepHandler.sleep(this, 50);
		m_input.IsMouseClicked(); 
	}
	

}
