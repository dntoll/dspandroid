package com.spellofplay.dsp;

import android.graphics.Bitmap;

class ConcreteTexture implements com.spellofplay.dsp.view.ITexture {

	
	private Bitmap bitmap;

	ConcreteTexture(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public Object getTexture() {

		return bitmap;
	}

}
