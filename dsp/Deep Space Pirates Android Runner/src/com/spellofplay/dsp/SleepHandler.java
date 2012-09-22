package com.spellofplay.dsp;



import android.os.Handler;
import android.os.Message;

public class SleepHandler extends Handler{
	
	public boolean m_stop = false;
	IUpdateable mUpdater = null;
	@Override
    public void handleMessage(Message msg) {
		if (m_stop == false) {
			mUpdater.update();
		} else {
			mUpdater = null;
			
			
		}
    }

    public void sleep(IUpdateable aUpdateable, long delayMillis) {
    	mUpdater = aUpdateable;
    	this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }
	
}
