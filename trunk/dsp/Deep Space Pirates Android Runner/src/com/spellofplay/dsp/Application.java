package com.spellofplay.dsp;


import com.spellofplay.dsp.controller.MasterController;
import com.spellofplay.dsp.model.inner.IPersistance;
import com.spellofplay.dsp.model.inner.ModelFacade;
import com.spellofplay.dsp.view.AndroidDraw;
import com.spellofplay.dsp.view.Input;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ViewConstructor")
class Application extends View implements IUpdateable {
	
	private MasterController masterController;
	private AndroidDraw androidDraw = new AndroidDraw();
	private Input input = new Input();
	private ModelFacade model = new ModelFacade();
	
	private Activity activity;
	
	private long lastTime = 0; //THE time in millis of last frame
	SleepHandler sleepHandler = new SleepHandler();
	
	
	Application(Context context, Activity cfTimerActivity) {
        super(context);
        
        Resources r = context.getResources();
		Drawable tilesDrawable = r.getDrawable(R.drawable.sprites);
		Drawable playerDrawable = r.getDrawable(R.drawable.player);
		
		ConcreteTexture texture = new ConcreteTexture(getBitmapFromDrawable(tilesDrawable));
		ConcreteTexture player = new ConcreteTexture(getBitmapFromDrawable(playerDrawable));
		
		
        masterController = new MasterController(input, texture, player, model, model);
        
        
        sleepHandler.sleep(this, 100);
        lastTime = System.currentTimeMillis();
        
        activity = cfTimerActivity;
        
   }

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		int width = drawable.getMinimumWidth();
		int height = drawable.getMinimumHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas2 = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas2);
		return bitmap;
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		if (input.onTouchEvent(event))
			return true;
		return super.onTouchEvent(event);
	   
	}
	
	
	@Override
	public boolean onKeyUp(int a_keyCode, KeyEvent event) {
		if (input.onKeyUp(a_keyCode, event))
			return true;
		return super.onKeyUp(a_keyCode, event);
	}
	
	@Override
	public boolean onKeyPreIme(int a_keyCode, KeyEvent event) {
		if (input.onKeyPreIme(a_keyCode, event))
			return true;
		return super.onKeyPreIme(a_keyCode, event);
	}
	 
	
	@Override
	public boolean onKeyDown(int a_keyCode, KeyEvent event) {
		if (input.onKeyDown(a_keyCode, event))
			return true;
		return super.onKeyDown(a_keyCode, event);
	}
	
	@Override
	public boolean onKeyShortcut(int a_keyCode, KeyEvent event) {
		if (input.onKeyShortcut(a_keyCode, event))
			return true;
		return super.onKeyShortcut(a_keyCode, event);
	}
	
	public void update() {
		invalidate();
    	sleepHandler.sleep(this, 10);
	}
	
	@Override
    public void onDraw(Canvas canvas) {
    	
		long now = System.currentTimeMillis();
        long elapsedTime = now - lastTime;
        float elapsedTimeSeconds = ((float)elapsedTime)/1000.0f;
        
        super.onDraw(canvas);
        
        androidDraw.setDrawTarget(canvas);
        if (masterController.doMenuOrGame(androidDraw, elapsedTimeSeconds) == false ) {
        	activity.finish();
        }
        lastTime = now;
	}
	
	
	
	
	
	void Stop(IPersistance persistence) {
		sleepHandler.doStop = true;
		
		masterController.showMenu();
		
		
		model.Save(persistence);
		masterController.save(persistence);
	}


	void Resume(IPersistance persistence) {
		sleepHandler.doStop = false;
		sleepHandler.sleep(this, 50);
		input.IsMouseClicked(); 
		
		try {
			model.Load(persistence);
			masterController.load(persistence);
		} catch (Exception e) {
			
		}
	}
	

}
