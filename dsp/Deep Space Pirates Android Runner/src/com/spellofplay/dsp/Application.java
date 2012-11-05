package com.spellofplay.dsp;


import com.spellofplay.dsp.controller.MasterController;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.Input;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Application extends View   implements IUpdateable {
	
	MasterController m_master = null;//new MasterController(context)
	AndroidDraw m_draw = new AndroidDraw();
	private Input m_input = new Input();
	
	private Activity m_activity;
	
	//TIMER
	long m_lastTime = 0; //THE time in millis of last frame
	public SleepHandler m_sleepHandler = new SleepHandler();
	
	
	public Application(Context context, Activity cfTimerActivity) {
        super(context);
        
        Resources r = context.getResources();
		Drawable tilesDrawable = r.getDrawable(R.drawable.sprites);
		Drawable playerDrawable = r.getDrawable(R.drawable.player);
		ConcreteTexture player, texture;
		{
			Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
			Canvas canvas2 = new Canvas(bitmap);
			tilesDrawable.setBounds(0, 0, 256, 256);
			tilesDrawable.draw(canvas2);
	        texture = new ConcreteTexture(bitmap);
        
		}
		{
			Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
	        Canvas canvas2 = new Canvas(bitmap);
	        playerDrawable.setBounds(0, 0, 256, 256);
	        playerDrawable.draw(canvas2);
	        player = new ConcreteTexture(bitmap);
		}
        m_master = new MasterController(context, m_input, texture, player);
        
        
        m_sleepHandler.sleep(this, 100);
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
		
		
        
        
		invalidate();
    	m_sleepHandler.sleep(this, 10);
    	
	}
	
	@Override
    public void onDraw(Canvas canvas) {
    	
		long now = System.currentTimeMillis();
        long elapsedTime = now - m_lastTime;
        float elapsedTimeSeconds = ((float)elapsedTime)/1000.0f;
        
        super.onDraw(canvas);
        
        m_draw.preDraw(canvas);
        if (m_master.onDraw(m_draw, elapsedTimeSeconds) == false ) {
        	m_activity.finish();
        }
        m_lastTime = now;
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
