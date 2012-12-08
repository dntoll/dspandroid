package com.spellofplay.dsp;

import android.graphics.Bitmap;

class ConcreteTexture implements com.spellofplay.dsp.view.ITexture {

	
	private Bitmap m_bitmap;

	ConcreteTexture(Bitmap bitmap) {
		m_bitmap = bitmap;
	}

	@Override
	public Object getTexture() {

		return m_bitmap;
	}

}
