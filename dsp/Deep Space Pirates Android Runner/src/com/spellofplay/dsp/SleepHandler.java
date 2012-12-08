package com.spellofplay.dsp;



import android.os.Handler;
import android.os.Message;

class SleepHandler extends Handler{
	
	boolean m_stop = false;
	private IUpdateable mUpdater = null;
	@Override
    public void handleMessage(Message msg) {
		if (m_stop == false) {
			mUpdater.update();
		} else {
			mUpdater = null;
			
			
		}
    }

    void sleep(IUpdateable aUpdateable, long delayMillis) {
    	mUpdater = aUpdateable;
    	this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), delayMillis);
    }
	
}
