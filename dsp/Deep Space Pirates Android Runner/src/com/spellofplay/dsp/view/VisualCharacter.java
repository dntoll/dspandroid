package com.spellofplay.dsp.view;

import android.graphics.Color;
import android.graphics.Rect;
import com.spellofplay.dsp.model.ICharacter;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Vector2;
import com.spellofplay.dsp.model.inner.CharacterType;

class VisualCharacter {
	private static final Rect SOLDIER = new Rect(0, 0, 255, 255);
	private static final Rect ENEMY = new Rect(0, 128, 32, 128 + 32);
	private static final Rect DEAD_ENEMY = new Rect(64, 128, 96, 128 + 32);
	private static final float MOVEMENT_TIME = 0.2f;
	private static final float ROTATION_SPEED = 360.0f;
	private static final int TRANSPARENT = Color.argb(128, 255, 255, 255);
	
	private float m_movementTimer = 0.0f;
	private float m_attackTimer = 0.0f;
	private float m_rotation = 0;
	private float m_targetRotation = 0;
	private boolean m_showDeadEnemy = false;
	private ICharacter m_modelCharacter;
	private ModelPosition m_lastPositionSeen = new ModelPosition();
	
	VisualCharacter(ICharacter character) {
		m_modelCharacter = character;
		m_lastPositionSeen.x = character.getPosition().x;
		m_lastPositionSeen.y = character.getPosition().y;
		
		if (character.getHitPoints() <= 0) {
			m_showDeadEnemy = true;
		}
	}
	
	void drawEnemySpotted(AndroidDraw drawable, Camera camera, ITexture enemyTexture) {
		boolean drawGui = true;

		ViewPosition vEpos = getVisualPosition(camera);
		int color = Color.WHITE;
		
		drawCharacter(vEpos, drawable, camera, enemyTexture, ENEMY, color, drawGui, true);
	}
	
	void drawDeadEnemy(AndroidDraw drawable, Camera camera, ITexture enemyTexture) {
		ViewPosition vEpos = getVisualPosition(camera);
		int color = Color.WHITE;
		
		if (m_showDeadEnemy)
			drawCharacter(vEpos, drawable, camera, enemyTexture, DEAD_ENEMY, color, false, true);
		else 
			drawCharacter(vEpos, drawable, camera, enemyTexture, ENEMY, color, false, true);
	}
	
	void drawEnemyNotSpotted(AndroidDraw drawable, Camera camera, ITexture enemyTexture, Vector2 lastSeenPosition) {
		boolean drawGui = false;
		int color =  TRANSPARENT;
		ViewPosition vEpos = camera.toViewPos(lastSeenPosition); 
		drawCharacter(vEpos, drawable, camera, enemyTexture, ENEMY, color, drawGui, false);
	}

	private void drawGUI(AndroidDraw drawable, Rect dst) {
		dst.left += 3;
		dst.right -= 6;
		
		dst.top = dst.top - 4;
		dst.bottom = dst.top + 4;
		drawable.drawRect(dst, Color.BLACK);
		
		float percentHp = (float)m_modelCharacter.getHitPoints() / (float)m_modelCharacter.getSkills().getMaxHitPoints();
		int width = (int)(((float)(dst.right - dst.left - 2)) * percentHp);
		dst.top = dst.top +1;
		dst.bottom = dst.top +2;
		dst.left++;
		dst.right = dst.left + width;
		drawable.drawRect(dst, Color.RED);
		
		CharacterType type = m_modelCharacter.getCharacterType();
		drawable.drawText(type.toString(), dst.left, dst.top);
		
	}

	void drawSoldier(AndroidDraw drawable, Camera camera, ITexture player, ICharacter target ) {
		
		ViewPosition vpos = getVisualPosition(camera);
		
		if (target != null) {
			m_targetRotation = m_modelCharacter.getPosition().sub(target.getPosition()).getRotation();
		}
		drawCharacter(vpos, drawable, camera, player, SOLDIER, Color.WHITE, true, true);
	}
	
	private void drawCharacter(ViewPosition vpos, 
							  AndroidDraw drawable, 
							  Camera camera, 
							  ITexture player, 
							  Rect source, int color, boolean drawgui, boolean rotate) {
		if (rotate) {
			
			if (m_lastPositionSeen.equals(m_modelCharacter.getPosition()) == false) {
				m_targetRotation = m_lastPositionSeen.sub(m_modelCharacter.getPosition()).getRotation();
			} 
		}
		
		
		Rect dst = camera.toRect(vpos);
		
		drawable.drawBitmap(player, source, dst, color, m_rotation);
		if (drawgui) {
			drawGUI(drawable, dst);
		}
	}
	
	

	ViewPosition getVisualPosition(Camera camera) {
		ViewPosition vpos = null;//camera.toViewPos(m_modelCharacter.getPosition());
		
		if (m_movementTimer > 0) {
			
			
			//interpolate
			Vector2 modelInterPolatedPosition = getInterpolatedModelPosition();
			vpos = camera.toViewPos(modelInterPolatedPosition);
					
		} else {
			vpos = camera.toViewPos(m_modelCharacter.getPosition());
		}
		return vpos;
	}

	public Vector2 getInterpolatedModelPosition() {
		float vmxpos = (float)m_lastPositionSeen.x * (1.0f - (MOVEMENT_TIME - m_movementTimer)/MOVEMENT_TIME) +   
		               (float)m_modelCharacter.getPosition().x * (MOVEMENT_TIME - m_movementTimer) / MOVEMENT_TIME;
		float vmypos = (float)m_lastPositionSeen.y * (1.0f - (MOVEMENT_TIME - m_movementTimer)/MOVEMENT_TIME) +   
					   (float)m_modelCharacter.getPosition().y * (MOVEMENT_TIME - m_movementTimer) / MOVEMENT_TIME;
		
		Vector2 modelInterPolatedPosition = new Vector2(vmxpos, vmypos);
		return modelInterPolatedPosition;
	}

	public boolean update(float a_elapsedTime) {
		
		//Normalize
		m_rotation = fixAngle(m_rotation);
		m_targetRotation = fixAngle(m_targetRotation);

		//Find direction and rotate
		float difference = getDifference(m_rotation, m_targetRotation);
		if (difference > ROTATION_SPEED * a_elapsedTime) {
			m_rotation += ROTATION_SPEED * a_elapsedTime;
		} else if (difference < -ROTATION_SPEED* a_elapsedTime) {
			m_rotation -= ROTATION_SPEED * a_elapsedTime;
		} else {
			m_rotation = m_targetRotation;
		}
		
		if (m_rotation != m_targetRotation)
			return false;
		
		m_movementTimer -= a_elapsedTime;
		if (m_movementTimer < 0) {
			m_lastPositionSeen = m_modelCharacter.getPosition();
		}
		
		m_attackTimer -= a_elapsedTime;
		
		return m_movementTimer < 0.0f && m_attackTimer < 0.0f;
	}

	private float fixAngle(float a_rotation) {
		while (a_rotation > 360) {
			a_rotation -= 360;
		}
		while (a_rotation < 0) {
			a_rotation += 360;
		}
		
		return a_rotation;
	}

	private float getDifference(float a, float b) {
		float difference = b-a;
		
		if (difference > 180) {
			difference = - (360 - difference);
		}
		if (difference < -180) {
			difference = (360 + difference);
		}
		
		return difference;
	}

	public void moveTo() {
		m_movementTimer = MOVEMENT_TIME;
	}

	public void attack() {
		m_attackTimer = 0.5f;
	}

	void doAnimateHit() {
		if (m_modelCharacter.getHitPoints() <= 0) {
			m_showDeadEnemy = true;
		}
	}

	
}

