package com.spellofplay.dsp;



import android.os.Handler;
import android.os.Message;

class SleepHandler extends Handler{
	
	boolean doStop = false;
	private IUpdateable theUpdateable = null;
	@Override
    public void handleMessage(Message msg) {
		if (doStop == false) {
			theUpdateable.update();
		} else {
			theUpdateable = null;
			
			
		}
    }

    void sleep(IUpdateable aUpdateable, long delayMillis) {
    	theUpdateable = aUpdateable;
    	this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }
	
}
