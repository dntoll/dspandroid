package com.spellofplay.dsp.view;

import com.spellofplay.common.view.Mesh;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

public class AndroidDraw  {
	public final Paint m_guiText = new Paint();
	private final Paint m_path = new Paint();
	
	private Bitmap m_background;
	
	Canvas m_drawTarget;
	
	public AndroidDraw() {
		m_path.setColor(Color.WHITE);
	}
	
	public void setDrawTarget(Canvas a_can) {
		m_drawTarget = a_can;
	}
	
	
	public void drawText(String gameTitle, int i, int j, Paint guiText) {
		
		m_drawTarget.drawText(gameTitle, i, j, guiText );
	}
	
	public void drawText(String gameTitle, int i, int j) {
		m_guiText.setColor(Color.WHITE);
		m_drawTarget.drawText(gameTitle, i, j, m_guiText );
	}

	
	public void drawMeshToBackground(Mesh a_backgroundMeshBlocked, 
						 int a_width, 
						 int a_height, 
						 int a_scale, com.spellofplay.dsp.view.ITexture a_textureMap) {
		
		//First time create background, or if resolution change adapt
		if (m_background == null || m_background.getWidth() != a_width * a_scale) {
			m_background = Bitmap.createBitmap(a_width * a_scale, a_height * a_scale, Bitmap.Config.ARGB_8888);
		}
		
		
		m_path.setColor(Color.WHITE);
		m_path.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		Canvas c = new Canvas(m_background);
		c.drawVertices(a_backgroundMeshBlocked.m_mode, 
				a_backgroundMeshBlocked.GetVerticeCount(), a_backgroundMeshBlocked.m_verts, 0, 
				a_backgroundMeshBlocked.m_texs, 0, 
				a_backgroundMeshBlocked.m_colors, 0, 
				a_backgroundMeshBlocked.m_indices, 0, a_backgroundMeshBlocked.GetIndexCount(), 
				m_path);
		
		
		return;
	}
	
	public void drawBackground(ViewPosition a_displacement) {
		m_guiText.setColor(Color.WHITE);
		m_drawTarget.drawBitmap(m_background, a_displacement.m_x, a_displacement.m_y, m_guiText);
	}

	
	public void drawBitmap(com.spellofplay.dsp.view.ITexture a_textureMap, Rect src, Rect dst, int a_color) {
		 
		
		m_path.setColor(a_color);
		m_path.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		m_drawTarget.drawBitmap((Bitmap)a_textureMap.getTexture(), src, dst, m_path);
		
	}
	
	public void drawBitmap(com.spellofplay.dsp.view.ITexture a_textureMap, Rect src, Rect dst, int a_color, float a_rotationDegrees) {
		 
		m_drawTarget.save();
		m_drawTarget.rotate(a_rotationDegrees, dst.exactCenterX(), dst.exactCenterY()); //center
		m_path.setColor(a_color);
		m_path.setShader(new BitmapShader((Bitmap) a_textureMap.getTexture(), TileMode.CLAMP, TileMode.CLAMP));
		
		m_drawTarget.drawBitmap((Bitmap)a_textureMap.getTexture(), src, dst, m_path);
		
		m_drawTarget.restore();
	}
	
	public void drawLine(ViewPosition vEpos, ViewPosition vsPos, int color) {
		m_guiText.setColor(color);
		
		m_drawTarget.drawLine(vEpos.m_x, vEpos.m_y, vsPos.m_x, vsPos.m_y, m_guiText);
	}
	
	public void drawCircle(ViewPosition center, int radius, int color) {
		
		m_guiText.setColor(color);
		RectF oval = new RectF(center.m_x - radius, center.m_y - radius, center.m_x + radius, center.m_y + radius);
		
		m_drawTarget.drawOval(oval, m_guiText);
	}

	public void drawRect(int left, int top, int right, int bottom,
			Paint paint) {
		m_drawTarget.drawRect(left, top, right, bottom, paint);
	}

	public int getWindowWidth() {

		return m_drawTarget.getWidth();
	}
	public int getWindowHeight() {

		return m_drawTarget.getHeight();
	}

	public void drawFog(Rect dst) {
		m_guiText.setColor(Color.argb(128, 0, 0, 0));
		m_drawTarget.drawRect(dst, m_guiText);
	}

	public void drawRect(Rect dst, int color) {
		m_guiText.setColor(color);
		m_drawTarget.drawRect(dst, m_guiText);
		
	}
	

}
