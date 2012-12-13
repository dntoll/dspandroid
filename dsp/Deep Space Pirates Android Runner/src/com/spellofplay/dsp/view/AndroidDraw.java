package com.spellofplay.dsp.view;

import com.spellofplay.common.view.Mesh;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Shader.TileMode;

public class AndroidDraw  {
	
	private final Paint bitmapPaint = new Paint();
	private final Paint shapes = new Paint();
	private final Paint guiText = new Paint();
	
	private Bitmap m_background;
	
	private Canvas m_drawTarget;
	
	public AndroidDraw() {
		bitmapPaint.setColor(Color.WHITE);
		guiText.setTextAlign(Align.CENTER);
	}
	
	public void setDrawTarget(Canvas a_can) {
		m_drawTarget = a_can;
	}
	
	
	
	public void drawText(String text, int x, int y, int color) {
		guiText.setColor(color);
		
		m_drawTarget.drawText(text, x, y, guiText );
	}
	
	void drawText(String gameTitle, int i, int j) {
		guiText.setColor(Color.WHITE);
		m_drawTarget.drawText(gameTitle, i, j, guiText );
	}

	
	void drawMeshToBackground(Mesh a_backgroundMeshBlocked, 
						 int a_width, 
						 int a_height, 
						 int a_scale, 
						 com.spellofplay.dsp.view.ITexture a_textureMap, boolean clear) {
		
		//First time create background, or if resolution change adapt
		if (m_background == null || m_background.getWidth() != a_width * a_scale) {
			m_background = Bitmap.createBitmap(a_width * a_scale, a_height * a_scale, Bitmap.Config.ARGB_8888);
		}
		
		
		bitmapPaint.setColor(Color.WHITE);
		bitmapPaint.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		
		
		Canvas c = new Canvas(m_background);
		
		if (clear) {
			c.drawColor(Color.BLACK);
		}
		bitmapPaint.setColor(Color.WHITE);
		
		c.drawVertices(a_backgroundMeshBlocked.m_mode, 
				a_backgroundMeshBlocked.GetVerticeCount(), a_backgroundMeshBlocked.m_verts, 0, 
				a_backgroundMeshBlocked.m_texs, 0, 
				a_backgroundMeshBlocked.m_colors, 0, 
				a_backgroundMeshBlocked.m_indices, 0, a_backgroundMeshBlocked.GetIndexCount(), 
				bitmapPaint);
		
		
		return;
	}
	
	void drawBackground(ViewPosition a_displacement) {
		shapes.setColor(Color.WHITE);
		m_drawTarget.drawBitmap(m_background, a_displacement.m_x, a_displacement.m_y, shapes);
	}

	
	void drawBitmap(com.spellofplay.dsp.view.ITexture a_textureMap, Rect src, Rect dst, int a_color, float a_rotationDegrees) {
		 
		m_drawTarget.save();
		m_drawTarget.rotate(a_rotationDegrees + 90, dst.exactCenterX(), dst.exactCenterY()); //center
		bitmapPaint.setColor(a_color);
		bitmapPaint.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		m_drawTarget.drawBitmap((Bitmap)a_textureMap.getTexture(), src, dst, bitmapPaint);
		
		m_drawTarget.restore();
	}
	
	void drawLine(ViewPosition vEpos, ViewPosition vsPos, int color) {
		shapes.setColor(color);
		shapes.setStrokeWidth(2.0f);
		m_drawTarget.drawLine(vEpos.m_x, vEpos.m_y, vsPos.m_x, vsPos.m_y, shapes);
	}
	
	
	
	void drawCircle(ViewPosition center, int radius, int color) {
		
		shapes.setColor(color);
		RectF oval = new RectF(center.m_x - radius, center.m_y - radius, center.m_x + radius, center.m_y + radius);
		
		m_drawTarget.drawOval(oval, shapes);
	}
	
	
	//TODO a little many doing same stuff
	void drawRect(int left, int top, int right, int bottom, int color) {
		
		shapes.setColor(color);
		m_drawTarget.drawRect(left, top, right, bottom, shapes);
	}
	
	void drawFog(Rect dst) {
		shapes.setColor(Color.argb(128, 0, 0, 0));
		m_drawTarget.drawRect(dst, shapes);
	}

	void drawRect(Rect dst, int color) {
		shapes.setColor(color);
		m_drawTarget.drawRect(dst, shapes);
		
	}

	void drawBlack(Rect dst) {
		shapes.setColor(Color.argb(255, 0, 0, 0));
		m_drawTarget.drawRect(dst, shapes);
	}

	public int getWindowWidth() {

		return m_drawTarget.getWidth();
	}
	public int getWindowHeight() {

		return m_drawTarget.getHeight();
	}

	

	

	
	

}
