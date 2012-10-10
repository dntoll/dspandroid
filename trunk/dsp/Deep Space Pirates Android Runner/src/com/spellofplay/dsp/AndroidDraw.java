package com.spellofplay.dsp;

import com.spellofplay.common.view.Mesh;
import com.spellofplay.dsp.model.Soldier;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;

public class AndroidDraw implements com.spellofplay.dsp.view.IDraw {
	private final Paint m_guiText = new Paint();
	private final Paint m_path = new Paint();
	
	private Bitmap m_background;
	
	Canvas m_canvas;
	
	public AndroidDraw() {
		m_path.setColor(Color.WHITE);
	}
	
	public void preDraw(Canvas a_can) {
		m_canvas = a_can;
	}
	
	@Override
	public void drawText(String gameTitle, int i, int j) {
		m_guiText.setColor(Color.WHITE);
		
		m_canvas.drawText(gameTitle, i, j, m_guiText );
	}

	@Override
	public void drawMeshToBackground(Mesh a_backgroundMeshBlocked, 
						 int a_width, 
						 int a_height, 
						 int a_scale, com.spellofplay.dsp.view.ITexture a_textureMap) {
		
		//First time create background, or if resolution change adapt
		if (m_background == null || m_background.getWidth() != a_width * a_scale) {
			m_background = Bitmap.createBitmap(a_width * a_scale, a_height * a_scale, Bitmap.Config.ARGB_8888);
		}
		
		Canvas c = new Canvas(m_background);
		c.drawARGB(255, 0, 0, 0);
		
		m_path.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		c.drawVertices(a_backgroundMeshBlocked.m_mode, 
				a_backgroundMeshBlocked.GetVerticeCount(), a_backgroundMeshBlocked.m_verts, 0, 
				a_backgroundMeshBlocked.m_texs, 0, 
				a_backgroundMeshBlocked.m_colors, 0, 
				a_backgroundMeshBlocked.m_indices, 0, a_backgroundMeshBlocked.GetIndexCount(), 
				m_path);
		
		//m_canvas.drawBitmap(m_background, 0, 0.0f, m_guiText);
		
		
		//m_canvas.drawBitmap((Bitmap) a_textureMap.getTexture(), 0, 0.0f, m_guiText);
		
		
		return;
	}
	@Override
	public void drawBackground() {
		m_canvas.drawBitmap(m_background, 0, 0.0f, m_guiText);
	}

	@Override
	public void drawBitmap(com.spellofplay.dsp.view.ITexture a_textureMap, Rect src, Rect dst) {
		
		m_path.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		m_canvas.drawBitmap((Bitmap)a_textureMap.getTexture(), src, dst, m_path);
	}
	

}
